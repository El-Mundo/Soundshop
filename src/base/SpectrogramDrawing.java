package base;

/*
 * @version 1.0.4
 * @author Shuangyuan Cao
 * @since 0.0.1
 * 
 * Github page:
 * https://github.com/El-Mundo/Soundshop
 * 
 * References:
 * 1. Aung's code for drawing spectrogram with FFT
 * https://stackoverflow.com/questions/39295589/creating-spectrogram-from-wav-using-fft-in-java
 * 2. Princeton University's code for Fast Fourier Transform and complex numbers
 * https://introcs.cs.princeton.edu/java/97data/FFT.java.html
 * 3. Joachim Sauer's code for concating arrays
 * https://stackoverflow.com/questions/80476/how-can-i-concatenate-two-arrays-in-java
 * 4. Jin Lim's code for streaming byte arrays as audio
 * https://stackoverflow.com/questions/14945217/playing-sound-from-a-byte-array
 * 
 * Libraries:
 * 1. Processing 3.5.4 for display
 * https://processing.org/
 * 2. Minim Audio for audio processing
 * http://code.compartmental.net/minim/index.html
 * 3. JH Labs for image filtering
 * http://www.jhlabs.com/index.html
 */

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

import com.jhlabs.image.BoxBlurFilter;
import com.jhlabs.image.MotionBlurOp;

import ddf.minim.Minim;
import processing.awt.PSurfaceAWT.SmoothCanvas;
import processing.core.PApplet;
import processing.core.PImage;
import resources.AudioResources;
import resources.GraphicResouces;
import scenes.FileSelectScene;
import scenes.SoundEditScene;
import sound.DrawSpectrogram;

public class SpectrogramDrawing extends PApplet {
	
	private final static int DEFAULT_WIDTH = 720, DEFAULT_HEIGHT = 480;
	private final static float DEFAULT_SCALE = 0.75F, DEFAULT_RATIO = (float)DEFAULT_WIDTH / (float)DEFAULT_HEIGHT;
	private final static Dimension WINDOW_MINIMUM = new Dimension(DEFAULT_WIDTH / 2, DEFAULT_HEIGHT / 2);
	
	private static float frameOffsetX = 0, frameOffsetY = 0;
	private static float frameScale = 1.0f;
	
	public static int antialiasingLevel = 0;
	private static int state = 0;
	public boolean transiting = false;
	
	public static int cursorX, cursorY, pCursorX, pcursorY;
	public static boolean clicked = false, pClicked = false;
	
	public static Frame nativeWindow;
	
	public FileSelectScene fileSelectScene;
	public SoundEditScene soundEditScene;
	
	public static Minim minim;
	public static ddf.minim.AudioOutput minimOut;
	
	//From 0-DEFAULT_WIDTH this factor controls the transition process
	public int transitionFactor = 0, transitionController = 0;
	
	public static SpectrogramDrawing main;

	public static void main(String args[]) {
		AudioOutput.initAudioDevice();
		PApplet.main("base.SpectrogramDrawing");
	}
	
	@Override
	public void settings() {
		//Adapt display rate to the screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int height = (int)(screenSize.height * DEFAULT_SCALE);
		frameScale = (float)height / (float)DEFAULT_HEIGHT;
		int width = (int)(DEFAULT_WIDTH * frameScale);
		size(width, height, JAVA2D);
		
		//Allocate this PApplet object's address to the static 'main' instance
		main = this;
		
		//Disable anti-aliasing at beginning
		noSmooth();
	}
	
	@Override
	public void setup() {
		surface.setIcon(GraphicResouces.ICON);
		
		//Initialize the first scene
		fileSelectScene = new FileSelectScene(DEFAULT_WIDTH, DEFAULT_HEIGHT, this);
				
		//To make the window adaptable for most devices
		surface.setResizable(true);
		//Add a listener so that resizing the window can be detected
		nativeWindow = ((SmoothCanvas)(surface.getNative())).getFrame();
		nativeWindow.addComponentListener(resizeListener);
		nativeWindow.setMinimumSize(WINDOW_MINIMUM);
		
		//Initialize Minim library
		minim = new Minim(this);
		minimOut = minim.getLineOut(Minim.MONO, 2048, 44100);
		
		AudioOutput.playMusic();
	}
	
	@Override
	public void draw() {
		if(loadingSceneInBkg) {
			loadingSceneInBkg = false;
			thread("loadSceneInBackgroound");
		}
		
		background(255, 245, 157);
		translate(frameOffsetX, frameOffsetY);
		scale(frameScale);
		
		switch (state) {
		case 1: //Sound editing
			transiting = false;
			if(soundEditScene != null) {
				soundEditScene.draw();
				image(soundEditScene, 0, 0);
			}
			break;
			
		case 2: //Transition to sound editing
			transiting = true;
			fileSelectScene.draw();
			image(fileSelectScene, 0, 0);
			if(soundEditScene != null) {
				soundEditScene.draw();
				image(soundEditScene, 0, 0);
			}
			
			transitionFactor += transitionController;
			
			if(transitionFactor <= -DEFAULT_WIDTH) {
				transitionFactor = -DEFAULT_WIDTH;
				state = 1;
			}else if(transitionFactor >= 0) {
				transitionFactor = 0;
				state = 0;
			}
			
			break;
			
		default: //File selection
			transiting = false;
			fileSelectScene.draw();
			image(fileSelectScene, 0, 0);
			break;
		}
		
		pCursorX = cursorX;
		pcursorY = cursorY;
		pClicked = clicked;
		cursorX = (int)((float)(this.mouseX - frameOffsetX) / frameScale);
		cursorY = (int)((float)(this.mouseY - frameOffsetY) / frameScale);
		circle(cursorX, cursorY, 6.0f / frameScale);
	}
	
	@Override
	public void mousePressed() {
		clicked = true;
	}
	
	@Override
	public void mouseReleased() {
		clicked = false;
	}
	
	@Override
	public void mouseExited() {
		clicked = false;
	}
	
	@Override
	public void focusLost() {
		clicked = false;
		pClicked = false;
	}
	
	public void resize() {
		//Use the shorter edge to define the scale factor
		if((float)width / (float)height < DEFAULT_RATIO) {
			//When the new ratio is higher than the default, use width for resizing
			frameScale = (float)width / DEFAULT_WIDTH;
			frameOffsetY = ((float)height - (float)DEFAULT_HEIGHT * frameScale) * 0.5f;
			frameOffsetX = 0;
		}else {
			//When the new ratio is wider than the default, use height for resizing
			frameScale = (float)height / DEFAULT_HEIGHT;
			frameOffsetX = ((float)width - (float)DEFAULT_WIDTH * frameScale) * 0.5f;
			frameOffsetY = 0;
		}
	}
	
	private static ComponentListener resizeListener = new ComponentListener() {
		@Override
		public void componentResized(ComponentEvent e) {
			((SpectrogramDrawing)main).resize();
		}
		
		@Override
		public void componentShown(ComponentEvent e) {}
		@Override
		public void componentMoved(ComponentEvent e) {}
		@Override
		public void componentHidden(ComponentEvent e) {}
	};
	
	public void transitToSoundEditting(SoundEditScene scene) {
		soundEditScene = scene;
		transitionController = -25;
		state = 2;
		AudioOutput.stopMusic();
	}
	
	public void transitToFileSelection() {
		if(soundEditScene != null) soundEditScene.quit();
		transitionController = 25;
		state = 2;
		AudioOutput.playMusic();
	}
	
	public static boolean loadingSceneInBkg = false;
	
	public void loadSceneInBackgroound() {
		try {
			fileSelectScene.setProcessButtonEnabled(false);
			if(FileSelectScene.pathString.toLowerCase().endsWith(".wav"))
				new SoundEditScene(this);
			else
				new SoundEditScene(this, this.loadImage(FileSelectScene.pathString));
			fileSelectScene.setProcessButtonEnabled(true);
		} catch (Exception e) {
			e.printStackTrace();
			FileSelectScene.guideline.setColor(180, 0, 0);
			FileSelectScene.guideline.update("Failed applying FFT to the input file.");
			AudioOutput.playBytesAsAudio(AudioResources.BYTE_SFX_WARNING);
			fileSelectScene.setProcessButtonEnabled(true);
		}
	}
	
	public void playAudioInBackgroound() {
		if(soundEditScene != null) {
			soundEditScene.playAudio();
		}
	}
	
	private static float filterValue;
	private static DrawSpectrogram filterSpectrogram;
	public static int type;
	private static boolean filtering = false;
	public static enum Filters {
			GAUSSIAN, MOTION, RADIAL
	};
	
	public static void filter(float value, DrawSpectrogram spectrogram, Filters type) {
		if(filtering) return;
		
		filterValue = value;
		filterSpectrogram = spectrogram;
		filtering = true;
		if(type == Filters.GAUSSIAN) {
			main.thread("gaussianBlurInBackgroound");
		}else if(type == Filters.MOTION) {
			main.thread("motionBlurInBackgroound");
		}else if(type == Filters.RADIAL) {
			main.thread("radialBlurInBackgroound");
		}
	}
	
	public void gaussianBlurInBackgroound() {
		PImage buf = filterSpectrogram.getSourceImage();
		int l = (int) (3 * filterValue);
		buf.filter(PImage.BLUR, 3.0f * filterValue);
		filterSpectrogram.setImage(buf);
		filterSpectrogram.log("Applied Gaussian Blur level " + l + " to image.");
		filtering = false;
	}
	
	public void motionBlurInBackgroound() {
		PImage src = filterSpectrogram.getSourceImage();
		//JH Labs's Box Filter provides a fast process similar to motion blur on horizontal/vertical
		float mag = src.width * 0.02f * filterValue;
		BoxBlurFilter filter = new BoxBlurFilter(mag, 0, 1);
		BufferedImage buf = new BufferedImage(src.width, src.height, BufferedImage.TYPE_INT_RGB);
		buf = filter.filter((BufferedImage)src.getNative(), buf);
		filterSpectrogram.setImage(new PImage(buf));
		filterSpectrogram.log("Applied Motion Blur level " + (int)mag + " to image.");
		filtering = false;
	}
	
	public void radialBlurInBackgroound() {
		//This filter sometimes has unexpected results, so we want to ignore changes with this filter that are too small.
		if(filterValue > 0.001f) {
			PImage src = filterSpectrogram.getSourceImage();
			float mag = src.width * 0.0002f * filterValue;
			MotionBlurOp filter = new MotionBlurOp(3, 0, mag, mag);
			BufferedImage buf = new BufferedImage(src.width, src.height, BufferedImage.TYPE_INT_RGB);
			buf = filter.filter((BufferedImage)src.getNative(), buf);
			filterSpectrogram.setImage(new PImage(buf));
			filterSpectrogram.log("Applied Radial Blur degree " + mag + " to image.");
		}else {
			filterSpectrogram.setImage(filterSpectrogram.getSourceImage());
			filterSpectrogram.log("Applied Radial Blur degree 0 to image.");
		}
		filtering = false;
	}

}
