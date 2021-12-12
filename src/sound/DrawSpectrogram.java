package sound;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import base.SpectrogramDrawing;
import ddf.minim.AudioSample;
import math.FFT;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import scenes.FileSelectScene;

public class DrawSpectrogram {
//https://stackoverflow.com/questions/39295589/creating-spectrogram-from-wav-using-fft-in-java
	
	private PImage image;

	public DrawSpectrogram() throws IOException {
		this.image = analyzeAllSamples(FileSelectScene.selectedFile);
	}
	
	public DrawSpectrogram(PImage image) throws IOException {
		this.image = image;
		this.image.filter(PImage.GRAY);
		//We want the image to always have 512 pixels on each column for inverse FFT
		if(this.image.height != 512) {
			this.image.resize(image.width, 512);
		}
	}
	
	public void draw(PGraphics g, int x, int y) {
		g.image(image, x, y);
	}
	
	public void draw(PGraphics g, int x, int y, int w, int h) {
		g.image(image, x, y, w, h);
	}
	
	public int getWidth() {
		return image.width;
	}
	
	public int getHeight() {
		return image.height;
	}
	
	public static PImage analyzeAllSamples(File selectedFile) {
		//Processing's default FFT function only analyzes the samples that are already loaded in the audio buffer,
		//so we use this referenced function to load all samples at initial load.
		//https://forum.processing.org/one/topic/how-to-generate-a-simple-waveform-of-an-entire-sound-file.html
		
		AudioSample jingle = SpectrogramDrawing.minim.loadSample(selectedFile.getAbsolutePath(), 2048);
		   
		float[] leftChannel = jingle.getChannel(AudioSample.LEFT);
		  
		int fftSize = 1024;
		float[] fftSamples = new float[fftSize];
		ddf.minim.analysis.FFT fft = new ddf.minim.analysis.FFT(fftSize, jingle.sampleRate());
		
		int totalChunks = (leftChannel.length / fftSize) + 1;
		
		float[][] spectra = new float[totalChunks][fftSize/2];
		float min = Float.MAX_VALUE;
		float max = Float.MIN_VALUE;
		
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
		   
		    for(int i = 0; i < fftSize/2; ++i) {
		      spectra[chunkIdx][i] = fft.getBand(i);
		      if(spectra[chunkIdx][i] > max) max = spectra[chunkIdx][i];
		      if(spectra[chunkIdx][i] < min) min = spectra[chunkIdx][i];
		    }
	    }
		jingle.close();
		
		PImage img = new PImage(spectra.length, spectra[0].length, PImage.RGB);
		for(int s = 0; s < spectra.length; s++) {
		    for(int i = 0; i < spectra[s].length-1; i++) {
		    	int rgb = (int) PApplet.map(spectra[s][i], min, 1, 0, 255);
		    	if(rgb>255)rgb=255;if(rgb<0)rgb=0;
		        img.pixels[i*img.width+s] = (new Color(rgb, rgb, rgb)).getRGB();
		    }
		}
		img.updatePixels();
		return img;
	}

	public static byte getColor(double power) {
		byte RGB = (byte)(power * 255.0);
        return RGB;
    }

    public static PImage draw(File selectedFile) throws IOException {
        //get raw double array containing .WAV data
        WavConvertor audioTest = new WavConvertor(selectedFile, true);
        double[] rawData = audioTest.getByteArray();
        int length = rawData.length;

        //initialize parameters for FFT
        int WS = 1024; //WS = window size
        int OF = 8;    //OF = overlap factor
        int windowStep = WS/OF;

        //calculate FFT parameters
        double SR = audioTest.getSR();
        double time_resolution = WS / SR;
        double frequency_resolution = SR / WS;
        double highest_detectable_frequency = SR / 2.0;
        double lowest_detectable_frequency = 5.0 * SR/WS;

        System.out.println("time_resolution:              " + time_resolution*1000 + " ms");
        System.out.println("frequency_resolution:         " + frequency_resolution + " Hz");
        System.out.println("highest_detectable_frequency: " + highest_detectable_frequency + " Hz");
        System.out.println("lowest_detectable_frequency:  " + lowest_detectable_frequency + " Hz");
        String[] lineTemp = new String[5];
        lineTemp[0] = "freq: "+frequency_resolution+"hz";
        lineTemp[1] = "max freq:"+highest_detectable_frequency;
        lineTemp[2] = "min_freq:"+lowest_detectable_frequency;
        for(int i=0;i<lineTemp.length;i++) 
        	if(lineTemp[i] != null) if(lineTemp[i].length() > 18) lineTemp[i] = lineTemp[i].substring(0, 15).concat("...");
        FileSelectScene.soundInfo.update("FFT Info:\n"+lineTemp[0]+"\n"
        		+lineTemp[1]+"\n"+lineTemp[2]);

        //initialize plotData array
        int nX = (length - WS) / windowStep;
        int nY = WS;
        double[][] plotData = new double[nX][nY]; 

        //apply FFT and find MAX and MIN amplitudes
        double maxAmp = Double.MIN_VALUE;
        double minAmp = Double.MAX_VALUE;

        double amp_square = 0.0;

        double[] inputImag = new double[length];
        double threshold = 1.0;
        
        FileSelectScene.soundInfo.update("FFT Info:\n"+lineTemp[0]+"\n"
        		+lineTemp[1]+"\n"+lineTemp[2]+"\n\nperforming FFT...");
        int progress = 0, total = nX * nY;

        for (int i = 0; i < nX; i ++) {
            Arrays.fill(inputImag, 0.0);
            double[] WS_array = FFT.fft(Arrays.copyOfRange(rawData, i * windowStep, i * windowStep+WS), inputImag, true);
            for (int j = 0; j < nY; j++){
                amp_square = (WS_array[2*j] * WS_array[2*j]) + (WS_array[2*j+1] * WS_array[2*j+1]);
                if (amp_square == 0.0){
                    plotData[i][j] = amp_square;
                }else {
                    //plotData[i][j] = 10 * Math.log10(amp_square);
                	plotData[i][nY-j-1] = 10 * Math.log10(Math.max(amp_square,threshold));
                }

                //find MAX and MIN amplitude
                if (plotData[i][j] > maxAmp)
                    maxAmp = plotData[i][j];
                else if (plotData[i][j] < minAmp)
                    minAmp = plotData[i][j];
                
                FileSelectScene.soundInfo.update("FFT Info:\n"+lineTemp[0]+"\n"
                		+lineTemp[1]+"\n"+lineTemp[2]+"\n\nperforming FFT..."+"\n("+progress+"/"+total+")");
                progress++;
            }
        }

        System.out.println("---------------------------------------------------");
        System.out.println("Maximum amplitude: " + maxAmp);
        System.out.println("Minimum amplitude: " + minAmp);
        System.out.println("---------------------------------------------------");
        lineTemp[3] = "amp max: "+maxAmp;
        lineTemp[4] = "amp min:"+minAmp;
        for(int i=0;i<lineTemp.length;i++) 
        	if(lineTemp[i] != null) if(lineTemp[i].length() > 18) lineTemp[i] = lineTemp[i].substring(0, 15).concat("...");
        FileSelectScene.soundInfo.update("FFT Info:\n"+lineTemp[0]+"\n"
        		+lineTemp[1]+"\n"+lineTemp[2]+"\n\nperforming FFT..."+"\n\n"+
        		lineTemp[3]+"\n"+lineTemp[4]);
        System.out.println(lineTemp[3]);

        //Normalization
        double diff = maxAmp - minAmp;
        for (int i = 0; i < nX; i++) {
            for (int j = 0; j < nY; j++){
                plotData[i][j] = (plotData[i][j] - minAmp) / diff;
                //if(plotData[i][j] < 0.9) plotData[i][j] = 0;
            }
        }
        
        FileSelectScene.soundInfo.update("FFT Info:\n"+lineTemp[0]+"\n"
        		+lineTemp[1]+"\n"+lineTemp[2]+"\n\nperforming FFT..."+"\n\n"+
        		lineTemp[3]+"\n"+lineTemp[4]+"\n\n"+"normalizing...");
        
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
        
        FileSelectScene.soundInfo.update("FFT Info:\n"+lineTemp[0]+"\n"
        		+lineTemp[1]+"\n"+lineTemp[2]+"\n\nperforming FFT..."+"\n\n"+
        		lineTemp[3]+"\n"+lineTemp[4]+"\n\n"+"normalizing..."+"\nrasterising...");

        //plot image
        PImage img = SpectrogramDrawing.main.createImage(nX, nY, PConstants.ALPHA);
        double ratio;
        for(int x = 0; x<nX; x++){
            for(int y = 0; y<nY; y++){
                ratio = plotData[x][y];
                //theImage.setRGB(x, y, new Color(red, green, 0).getRGB());
                img.set(x, y, getColor(ratio));
            }
        }
        
        FileSelectScene.soundInfo.update("FFT Info:\n"+lineTemp[0]+"\n"
        		+lineTemp[1]+"\n"+lineTemp[2]+"\n\nperforming FFT..."+"\n\n"+
        		lineTemp[3]+"\n"+lineTemp[4]+"\n\n"+"normalizing..."+"\nrasterising..."+"\n\ncomplete.");
        
        return img;
    }

}
