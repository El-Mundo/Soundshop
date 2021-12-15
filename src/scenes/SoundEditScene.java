package scenes;

import java.io.IOException;

import base.DrawString;
import base.Scene;
import base.SpectrogramDrawing;
import interactable.AudioPlayButton;
import interactable.BackButton;
import interactable.ButtonGroup;
import interactable.FilterButtonGroup;
import interactable.GaussianSetter;
import interactable.Interactable;
import interactable.MotionBlurSetter;
import interactable.RadialSetter;
import interactable.SavePngButton;
import interactable.SaveWavButton;
import interactable.Setter;
import processing.core.PApplet;
import processing.core.PImage;
import resources.GraphicResouces;
import sound.DrawSpectrogram;

public class SoundEditScene extends Scene {
	
	protected DrawSpectrogram spectrogram;
	protected Interactable[] interactables;
	public DrawString console;
	
	protected Setter gaussianSetter, motionSetter, radialSetter;
	private SavePngButton savePngButton;
	private SaveWavButton saveWavButton;
	protected FilterButtonGroup filterButtonGroup;
	public int state;
	
	public float posX = 0.0f;
	
	public boolean isPlaying;
	
	public void initInteractables() {
		console = new DrawString("Info will be printed here.");
		
		interactables = new Interactable[6];
		interactables[0] = new BackButton(16, 400, 2);
		interactables[1] = new AudioPlayButton(656, 400, 2, this);
		
		gaussianSetter = new GaussianSetter(232, 400, spectrogram);
		radialSetter = new RadialSetter(232, 400, spectrogram);
		motionSetter = new MotionBlurSetter(232, 400, spectrogram);
		savePngButton = new SavePngButton(232, 400, 2, spectrogram);
		saveWavButton = new SaveWavButton(400, 400, 2, spectrogram);
		
		filterButtonGroup = new FilterButtonGroup(this);
		interactables[2] = new ButtonGroup.GroupedButton(GraphicResouces.FILTER_GAUSSIAN, 90, 448, 2, filterButtonGroup);
		interactables[3] = new ButtonGroup.GroupedButton(GraphicResouces.FILTER_MOTION, 260, 448, 2, filterButtonGroup);
		interactables[4] = new ButtonGroup.GroupedButton(GraphicResouces.FILTER_RADIAL, 400, 448, 2, filterButtonGroup);
		interactables[5] = new ButtonGroup.GroupedButton(GraphicResouces.SAVE_BUTTON, 540, 448, 2, filterButtonGroup);
		
		isPlaying = false;
		state = 0;
	}
	
	public void play() {
		parent.thread("playAudioInBackgroound");
	}
	
	public void playAudio() {
		spectrogram.playSound();
	}
	
	public void quit() {
		spectrogram.endSound(SpectrogramDrawing.minim.getLineOut());
		isPlaying = false;
	}
	
	public SoundEditScene(PApplet parent, PImage image) throws IOException {
		super(720, 480, parent);
		//Apply default settings if a png is loaded (sample rate 44100)
		spectrogram = new DrawSpectrogram(image, this);
		initInteractables();
		SpectrogramDrawing.main.transitToSoundEditting(this);
		spectrogram.setDefaultValues();
	}
	
	public SoundEditScene(PApplet parent) throws IOException {
		super(720, 480, parent);
		spectrogram = new DrawSpectrogram(this);
		initInteractables();
		SpectrogramDrawing.main.transitToSoundEditting(this);
	}

	@Override
	public void drawContent() {
		//background(0);
		fill(255, 245, 157);
		clear();
		
		translate(SpectrogramDrawing.main.transitionFactor + width, 0);
		rect(-1, -1, width + 2, height + 2);
		
		spectrogram.draw(this, 6, 6, width-12, height-120);
		noFill();
		strokeWeight(1);
		stroke(255, 0, 0);
		float rx = 6 + 708 * posX;
		line(rx, 6, rx, 366);
		stroke(251, 192, 45);
		strokeWeight(4);
		rect(6, 6, width-12, height-120);
		
		if(console.getWidth(16) > 720) {
			console.draw(this, 360 - console.getWidth(8) / 2, height - 112, 8);
		}else {
			console.draw(this, 360 - console.getWidth(16) / 2, height - 112, 16);
		}
		
		for(Interactable i : interactables) {
			i.display(this);
		}
		
		switch(state) {
		case 0:
			gaussianSetter.display(this);
			break;
		case 1:
			motionSetter.display(this);
			break;
		case 2:
			radialSetter.display(this);
			break;
		case 3:
			saveWavButton.display(this);
			savePngButton.display(this);
			break;
		}
		
		if(SpectrogramDrawing.clicked) {
			int mx = SpectrogramDrawing.cursorX;
			int my = SpectrogramDrawing.cursorY;
			if(mx > 6 && mx < 714) {
				if(my > 6 && my < 366) {
					posX = ((float)mx - 6.0f) / 708.0f;
				}
			}
		}
	}
	
	public void updateStagedImage() {
		spectrogram.setSourceImage(spectrogram.getImage());
		gaussianSetter.reset();
		radialSetter.reset();
		motionSetter.reset();
		saveWavButton.reset();
		spectrogram.log("Info will be printed here.");
	}
	
	public String savePng() {
		String tar = "";
		try {
			String orString = FileSelectScene.selectedFile.getAbsolutePath();
			if(orString.length() > 4) {
				tar = orString.substring(0, orString.length() - 4).concat("-SOUNDSHOP.png");
			}else {
				tar = FileSelectScene.selectedFile.getParent() + "SOUNDSHOP.png";
			}
			spectrogram.getImage().save(tar);
			return tar;
		}catch(Exception e) {
			spectrogram.log("An error occurred when saving png!", true);
			return tar;
		}
	}
	
	public String saveWav() {
		String tar = "";
		try {
			String orString = FileSelectScene.selectedFile.getAbsolutePath();
			if(orString.length() > 4) {
				tar = orString.substring(0, orString.length() - 4).concat("-SOUNDSHOP.wav");
			}else {
				tar = FileSelectScene.selectedFile.getParent() + "SOUNDSHOP.wav";
			}
			spectrogram.saveWav(tar);
			return tar;
		}catch(Exception e) {
			spectrogram.log("Cannot record the audio.", true);
			return tar;
		}
	}
	
}
