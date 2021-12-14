package sound;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import base.SpectrogramDrawing;
import ddf.minim.AudioInput;
import ddf.minim.AudioRecorder;
import ddf.minim.Minim;
import ddf.minim.UGen;
import ddf.minim.analysis.FourierTransform;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import scenes.FileSelectScene;
import scenes.SoundEditScene;

public class DrawSpectrogram {
//https://stackoverflow.com/questions/39295589/creating-spectrogram-from-wav-using-fft-in-java
	
	private PImage image, sourceImage;
	private SpectralGen player;
	//Calculated in FFT
	private static int timeInterval, sampleRate;
	private SoundEditScene parentScene;
	public static boolean filterAmplitude = true;

	public DrawSpectrogram(SoundEditScene parent) throws IOException {
		this.image = draw(FileSelectScene.selectedFile);
		//this.image = (new TestSpec(FileSelectScene.selectedFile.getAbsolutePath())).analyzeAllSamples();
		//this.image = analyzeAllSamples(FileSelectScene.selectedFile);
		this.parentScene = parent;
		initPlayer();
	}
	
	private void initPlayer() {
		this.player = new SpectralGen(1024, sampleRate);
		//Copy the original image
		this.sourceImage = image.copy();
	}
	
	public void log(String content) {
		parentScene.console.setColor(0, 0, 0);
		this.parentScene.console.update(content);
	}
	
	public void log(String content, boolean red) {
		if(red) {
			parentScene.console.setColor(255, 0, 0);
		}else {
			parentScene.console.setColor(0, 0, 0);
		}
		this.parentScene.console.update(content);
	}
	
	public void playSound() {
		ddf.minim.AudioOutput out = SpectrogramDrawing.minim.getLineOut(Minim.MONO, 2048);
		player.patch(out);
		
		for(int x=(int)PApplet.map(parentScene.posX, 0.0f, 1.0f, 0, image.width); x<image.width; x++) {
			float[] buf = new float[1];
			for(int y=0; y<image.height; y++) {
				Color color = (new Color(image.get(x, y)));
				player.setBand(PApplet.min(y, 511), (float)color.getRed());
				buf[0] = color.getRed();
			}
			//Shut this thread down when playing is stopped
			if(!parentScene.isPlaying || SpectrogramDrawing.forceQuit) {
				endSound(out);
				return;
			}
			parentScene.posX = (float)x / (float)image.width;
			
			try {
				Thread.sleep(timeInterval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		endSound(out);
		parentScene.posX = 0;
	}
	
	AudioInput in;
	AudioRecorder recorder;
	
	public void saveWav(String path) throws Exception {
		ddf.minim.AudioOutput out = SpectrogramDrawing.minim.getLineOut(Minim.MONO, 2048);
		in = SpectrogramDrawing.minim.getLineIn(Minim.MONO, 2048);
		recorder = SpectrogramDrawing.minim.createRecorder(in, path);
		player.patch(out);
		
		recorder.beginRecord();
		for(int x=(int)PApplet.map(parentScene.posX, 0.0f, 1.0f, 0, image.width); x<image.width; x++) {
			float[] buf = new float[1];
			for(int y=0; y<image.height; y++) {
				Color color = (new Color(image.get(x, y)));
				player.setBand(PApplet.min(y, 511), (float)color.getRed());
				buf[0] = color.getRed();
			}
			if(SpectrogramDrawing.forceQuit) {
				endSound(out);
				recorder.endRecord();
				return;
			}
			parentScene.posX = (float)x / (float)image.width;
			
			try {
				Thread.sleep(timeInterval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		endSound(out);
		parentScene.posX = 0;
		recorder.endRecord();
		recorder.save();
	}
	
	public void setDefaultValues() {
		timeInterval = 16;
		sampleRate = 44100;
	}
	
	public void endSound(ddf.minim.AudioOutput out) {
		player.unpatch(out);
		parentScene.isPlaying = false;
	}
	
	public DrawSpectrogram(PImage image, SoundEditScene parent) throws IOException {
		this.image = image;
		this.getImage().filter(PImage.GRAY);
		//We want the image to always have 512 pixels on each column for inverse FFT
		if(this.getImage().height != 512) {
			this.getImage().resize(image.width, 512);
		}
		this.parentScene = parent;
		initPlayer();
	}
	
	public void draw(PGraphics g, int x, int y) {
		g.image(getImage(), x, y);
	}
	
	public void draw(PGraphics g, int x, int y, int w, int h) {
		g.image(getImage(), x, y, w, h);
	}
	
	public int getWidth() {
		return getImage().width;
	}
	
	public int getHeight() {
		return getImage().height;
	}

	public static int getColor(double power) {
		int RGB = SpectrogramDrawing.main.color((int) (power * 255));
        return RGB;
    }
    
	
    
    class SpectralGen extends UGen {
	//https://github.com/ddf/Minim/blob/e294e2881a20340603ee0156cb9188c15b5915c2/examples/Analysis/FFT/Inverse/Inverse.pde
      ddf.minim.analysis.FFT     fft;
      int     timeSize;
      // how big the "overlap" window is.
      int     windowSize;
      int     outIndex;
      float[] output;
      float[] inverse;
      // we have to reconfigure the fft before each inverse call, since the inverse call modifies the interal data
      // so have a buffer of silence we pass into forward and our own array of spectral amplitudes
      float[] silence;
      float[] amplitudes;
      
      public SpectralGen(int specSize, int sampleRate) {
        fft      = new ddf.minim.analysis.FFT(specSize, sampleRate);
        timeSize = specSize;
        windowSize = specSize/2;
        output = new float[specSize];
        inverse  = new float[specSize];
        amplitudes = new float[specSize/2];
        silence = new float[specSize];
        outIndex = output.length;
      }
      
      public int size() {
        return amplitudes.length;
      }
      
      public void setBand(int b, float amp) {
        amplitudes[b] = amp;
      }
      
      public float getBand(int b) {
        return amplitudes[b];
      }
      
      protected void uGenerate(float[] out) {
         if (outIndex % windowSize == 0) {
           fft.forward(silence);
           for(int i = 0; i < amplitudes.length; ++i) {
             fft.setBand(i, amplitudes[i]*0.6f);
           }
           
           fft.inverse(inverse);
           FourierTransform.HAMMING.apply(inverse);
           
           if (outIndex == output.length) {
             outIndex = 0;
           } 
           
           for(int s = 0; s < timeSize; ++s) {
             int o = (s + outIndex) % timeSize;
             output[o] += inverse[s];
           }
         }
         
         for(int i = 0; i < out.length; ++i) {
           out[i] = output[outIndex];
         }
         output[outIndex] = 0;
         
         ++outIndex;
      }
    }
    
    public static PImage draw(File selectedFile) throws IOException {
        //get raw double array containing .WAV data
        WavConvertor audioTest = new WavConvertor(selectedFile, true);
        double[] rawData = audioTest.getByteArray();
        int length = rawData.length;
        
        FileSelectScene.clearFFTInfo();

        //initialize parameters for FFT
        int WS = 1024; //WS = window size
        int OF = 8;    //OF = overlap factor
        int windowStep = WS/OF;

        //calculate FFT parameters
        double SR = audioTest.getSR();
        double time_resolution = WS / SR;
        //double frequency_resolution = SR / WS;
        double highest_detectable_frequency = SR / 2.0;
        double lowest_detectable_frequency = 5.0 * SR/WS;
        sampleRate = (int)SR;
        
        //Use 480 instead of 1000 for better performance
        timeInterval = (int)(time_resolution * 480);

        /*System.out.println("time_resolution:              " + timeInterval + " ms");
        System.out.println("frequency_resolution:         " + frequency_resolution + " Hz");
        System.out.println("highest_detectable_frequency: " + highest_detectable_frequency + " Hz");
        System.out.println("lowest_detectable_frequency:  " + lowest_detectable_frequency + " Hz");*/
        
        FileSelectScene.addFFTInfoLine("\nspectro:1024");
        FileSelectScene.addFFTInfoLine("res:"+timeInterval+"ms");
        FileSelectScene.addFFTInfoLine("freq max:"+(int)highest_detectable_frequency+"ms");
        FileSelectScene.addFFTInfoLine("freq min:"+(int)lowest_detectable_frequency+"ms");

        //initialize plotData array
        int nX = (length - WS) / windowStep;
        int nY = WS / 2;
        double[][] plotData = new double[nX][nY]; 

        //apply FFT and find MAX and MIN amplitudes
        double maxAmp = Double.MIN_VALUE;
        double minAmp = Double.MAX_VALUE;

        double amp_square = 0.0;

        double[] inputImag = new double[length];
        double threshold = 1.0;
        
        FileSelectScene.addFFTInfoLine("\nperforming FFT...");
        
        int progress = 0, total = nX * nY;

        for (int i = 0; i < nX; i ++) {
            java.util.Arrays.fill(inputImag, 0.0);
            double[] WS_array = math.FFT.fft(java.util.Arrays.copyOfRange(rawData, i * windowStep, i * windowStep+WS), inputImag, true);
            for (int j = 0; j < nY; j++){
            	if(filterAmplitude) {
                	amp_square = (WS_array[2*j] * WS_array[2*j]) + (WS_array[2*j+1] * WS_array[2*j+1]);
                }else {
                	amp_square = WS_array[2*j] + WS_array[2*j+1];
                }
                /*if (amp_square == 0.0){
                    plotData[i][j] = amp_square;
                }else {
                    //plotData[i][j] = 10 * Math.log10(amp_square);
                	plotData[i][nY-j-1] = 10 * Math.log10(Math.max(amp_square, threshold));
                }*/
                plotData[i][j] = Math.max(amp_square, threshold);

                //find MAX and MIN amplitude
                if (plotData[i][j] > maxAmp)
                    maxAmp = plotData[i][j];
                else if (plotData[i][j] < minAmp)
                    minAmp = plotData[i][j];
                
                FileSelectScene.showFFTProgress(progress, total);
                progress++;
            }
        }

        System.out.println("---------------------------------------------------");
        System.out.println("Maximum amplitude: " + maxAmp);
        System.out.println("Minimum amplitude: " + minAmp);
        System.out.println("---------------------------------------------------");
        
        FileSelectScene.addFFTInfoLine("\namp max:"+(int)maxAmp+"db");
        FileSelectScene.addFFTInfoLine("amp min:"+(int)minAmp+"db");

        //Normalization
        double diff = maxAmp - minAmp;
        for (int i = 0; i < nX; i++) {
            for (int j = 0; j < nY; j++){
                plotData[i][j] = (plotData[i][j] - minAmp) / diff;
                //if(plotData[i][j] < 0.9) plotData[i][j] = 0;
            }
        }
        
        FileSelectScene.addFFTInfoLine("\nnormalizing...");
        
        //ADDED HERE: We normalize again for each sample
        diff = maxAmp - minAmp;
        for (int i = 0; i < nX; i++) {
        	double sampleMax = Double.MIN_VALUE;
            double sampleMin = Double.MAX_VALUE;
            for (int j = 0; j < nY; j++){
            	if(plotData[i][j] < sampleMin) {
                	sampleMin = plotData[i][j];
            	}
            	if(plotData[i][j] > sampleMax) {
            		sampleMax = plotData[i][j];
            	}
            }
            diff = sampleMax - sampleMin;
            for (int j = 0; j < nY; j++){
            	plotData[i][j] = (plotData[i][j] - sampleMin) / diff;
            }
        }
        
        FileSelectScene.addFFTInfoLine("rasterizing...");

        //plot image
        PImage img = new PImage(nX, nY);
        double ratio;
        progress = 0;
        for(int x = 0; x<nX; x++){
            for(int y = 0; y<nY; y++){
                ratio = plotData[x][y];
                //theImage.setRGB(x, y, new Color(red, green, 0).getRGB());
                img.set(x, y, getColor(ratio));
                progress ++;
                FileSelectScene.showFFTProgress(progress, total);
            }
        }
        
        FileSelectScene.addFFTInfoLine("\ncomplete.");
        
        return img;
    }

	public PImage getImage() {
		return image;
	}
	
	public void setImage(PImage img) {
		image = img;
	}
	
	public PImage getSourceImage() {
		return sourceImage.copy();
	}
	
	public void setSourceImage(PImage img) {
		sourceImage = img;
		image = sourceImage.copy();
	}
	
	/*public static PImage analyzeAllSamples(File selectedFile) {
		//Processing's default FFT function only analyzes the samples that are already loaded in the audio buffer,
		//so we use this referenced function to load all samples at initial load.
		//https://forum.processing.org/one/topic/how-to-generate-a-simple-waveform-of-an-entire-sound-file.html
		
		AudioSample jingle = SpectrogramDrawing.minim.loadSample(selectedFile.getAbsolutePath(), 2048);
		
		FileSelectScene.clearFFTInfo();
		   
		float[] leftChannel = jingle.getChannel(AudioSample.LEFT);
		  
		int fftSize = 1024;
		float[] fftSamples = new float[fftSize];
		ddf.minim.analysis.FFT fft = new ddf.minim.analysis.FFT(fftSize, jingle.sampleRate());
		
		FileSelectScene.addFFTInfoLine("\nspectro:1024");
		FileSelectScene.addFFTInfoLine("sr:"+(int)jingle.sampleRate());
		sampleRate = (int)jingle.sampleRate();
		
		int totalChunks = (leftChannel.length / fftSize) + 1;
		
		float[][] spectra = new float[totalChunks][fftSize/2];
		float min = Float.MAX_VALUE;
		float max = Float.MIN_VALUE;
		
		FileSelectScene.addFFTInfoLine("samples:"+totalChunks);
		FileSelectScene.addFFTInfoLine("\nperforming FFT...");
		long progress = 0, spec = fftSize / 2, total = totalChunks * spec;
		
		for(int chunkIdx = 0; chunkIdx < totalChunks; ++chunkIdx) {
			int chunkStartIndex = chunkIdx * fftSize;
			int chunkSize = PApplet.min(leftChannel.length - chunkStartIndex, fftSize);
			
			for(int i=0; i<chunkSize; i++) {
				if(i >= fftSamples.length) break;
				if(i+chunkStartIndex < leftChannel.length)
					fftSamples[i] = leftChannel[i+chunkStartIndex];
			}
		      
		    if (chunkSize < fftSize)
		    	java.util.Arrays.fill(fftSamples, chunkSize, fftSamples.length - 1, 0.0f);
		    
		    fft.forward(fftSamples);
		   
		    for(int i = 0; i < spec; ++i) {
		      spectra[chunkIdx][i] = fft.getBand(i);
		      if(spectra[chunkIdx][i] > max) max = spectra[chunkIdx][i];
		      if(spectra[chunkIdx][i] < min) min = spectra[chunkIdx][i];
		      
		      progress ++;
		      FileSelectScene.showFFTProgress(progress, total);
		    }
	    }
		jingle.close();
		
		FileSelectScene.addFFTInfoLine("\namp max:"+max);
		FileSelectScene.addFFTInfoLine("amp min:"+min);
		FileSelectScene.addFFTInfoLine("\nrasterizing...");
		
		progress = 0;
		
		PImage img = new PImage(spectra.length, spectra[0].length, PImage.RGB);
		for(int s = 0; s < spectra.length; s++) {
		    for(int i = 0; i < spectra[s].length-1; i++) {
		    	int rgb = (int) PApplet.map(spectra[s][i], min, 2, 0, 255);
		    	if(rgb>255)rgb=255;if(rgb<0)rgb=0;
		        img.pixels[i*img.width+s] = (new Color(rgb, rgb, rgb)).getRGB();
		        
		        progress++;
		        FileSelectScene.showFFTProgress(progress, total);
		    }
		}
		img.updatePixels();
		
		FileSelectScene.addFFTInfoLine("\ncomplete.");
		return img;
	}*/
    
    /*class TestSpec {
    	ddf.minim.analysis.FFT fft;
    	float[] spectrum;
    	float maxPower;
    	float[][] spectra;
    	String path;
    	
	    public TestSpec(String path) {
	    	this.path = path;
	    	AudioSample jingle = SpectrogramDrawing.minim.loadSample(path, 2048);
	        this.fft = new ddf.minim.analysis.FFT(SPEC_SIZE, jingle.sampleRate());
	        this.spectrum = new float[SPEC_SIZE];
	        
	        // calculate an integrated window function
	        float[] wbuf = new float[fft.timeSize()];
	        for (int i = 0; i < fft.timeSize(); ++i) wbuf[i] = 1f;
	        ddf.minim.analysis.FFT wfft = new ddf.minim.analysis.FFT(fft.timeSize(), 44100);
	        wfft.forward(wbuf);
	        wfft.inverse(wbuf);
	        this.maxPower = 0f;
	        for (int i = 0; i < fft.timeSize(); ++i) this.maxPower += wbuf[i];
	    }
	    
	    public PImage analyzeAllSamples() {
	    //https://forum.processing.org/one/topic/how-to-generate-a-simple-waveform-of-an-entire-sound-file.html
	      AudioSample jingle = SpectrogramDrawing.minim.loadSample(path, 2048);
	      float[] leftChannel = jingle.getChannel(AudioSample.LEFT);
	      float[] fftSamples = new float[SPEC_SIZE];
	      
	      // now we'll analyze the samples in chunks
	      int totalChunks = (leftChannel.length / SPEC_SIZE) + 1;
	      // allocate a 2-dimentional array that will hold all of the spectrum data for all of the chunks.
	      // the second dimension if fftSize/2 because the spectrum size is always half the number of samples analyzed.
	      spectra = new float[totalChunks][SPEC_SIZE/2];
	      PImage img = new PImage(totalChunks, SPEC_SIZE/2);
	      
	      for(int chunkIdx = 0; chunkIdx < totalChunks; ++chunkIdx) {
	        int chunkStartIndex = chunkIdx * SPEC_SIZE;
	       
	        // the chunk size will always be fftSize, except for the 
	        // last chunk, which will be however many samples are left in source
	        int chunkSize = PApplet.min(leftChannel.length - chunkStartIndex, SPEC_SIZE);
	       
	        // copy first chunk into our analysis array
	        for(int i=0; i<chunkSize; i++) {
	            fftSamples[i] = leftChannel[i+chunkStartIndex];
	        }
	          
	        // if the chunk was smaller than the fftSize, we need to pad the analysis buffer with zeroes        
	        if (chunkSize < SPEC_SIZE) {
	          //Arrays.fill(fftSamples, chunkSize, fftSamples.length - 1, 0.0);
	        	for(int i=chunkSize; i<fftSamples.length-1; i++)
	        		fftSamples[i] = 0;
	        }
	        
	        // now analyze this buffer
	        fft.forward(fftSamples);
	       
	        analyzeSample(img, chunkIdx);
	      }
	      
	      jingle.close();
	      return img;
	    }
    
	    private final static int SPEC_SIZE = 1024;
	    
	    public void analyzeSample(PImage img, int x) {
	    //https://github.com/sabamotto/Spectrogram
	      fft.forward(spectrum);
	      for (int i = 0; i < SPEC_SIZE; ++i) spectrum[i] = fft.getBand(i);
		  int shei = (512 - 32) / 2;
		  float prevFreqIndex = 0f;
		  //float maxFreq = 0f;
		  float maxPeek = 0f;
		  for (int i = 0; i < shei; ++i) {
		    float freqRatio = (float)(i) / shei;
		    float freqIndex = getIndex(freqRatio);
		    
		    // MEMO: Strictly calculation, it should be an integrated power
		    //float power = is.getPower(freqIndex);
		    float power = getMaxPower(prevFreqIndex, freqIndex); // Peek
		    prevFreqIndex = freqIndex;
		    
		    //Color c = new Color(PApplet.min(PApplet.max(0, 360 * power * power), 255), 100, PApplet.max(PApplet.min(180 * power, 255), 0));
		    Color c = new Color((int) PApplet.map(power, 0, maxPower, 0, 255));
		    img.set(x, shei - i, c.getRGB());
		    this.spectrum[i] = c.getRGB();
		    
		    if (maxPeek < power) {
		      //maxFreq = freqIndex;
		      maxPeek = power;
		    }
		  }
	    }
	    
	    private float getIndex(float g) {
	        float index = 0f;
	        index = PApplet.pow(SPEC_SIZE, g);
	        
	        if (index < 0) index = 0;
	        else if (index >= SPEC_SIZE - 1) index = SPEC_SIZE - 1;
	        return index;
	    }
	    
	    private float getPower(float index) {
		    float power = 0f;
		    power = this.spectrum[PApplet.floor(index)];
		    
		    return this.convertPowerToLinOrLog(power);
		}
		  
		private float getMaxPower(float beginIndex, float endIndex) {
			if (endIndex-beginIndex < 0) {
			      float tmp = beginIndex;
			      beginIndex = endIndex;
			      endIndex = tmp;
			    }
			    
			    if (endIndex-beginIndex < 1f) {
			      return this.getPower(endIndex);
			    }
			    
			    float power = 0f;
			    int floorBeginIndex = PApplet.floor(beginIndex);
			    if (beginIndex > floorBeginIndex) {
			        power = this.spectrum[floorBeginIndex];
			    }
			    for (int i = PApplet.ceil(beginIndex); i < endIndex; i++) {
			      power = PApplet.max(power, this.spectrum[i]);
			    }
			    
			    return this.convertPowerToLinOrLog(power);
		}
		
		private float convertPowerToLinOrLog(float power) {
		    //if (power <= 1) power = 0f; // enabled signal filter
		    return PApplet.log(power) / PApplet.log(this.maxPower);
		}
	}*/

}
