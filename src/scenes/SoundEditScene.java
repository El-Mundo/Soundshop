package scenes;

import java.io.IOException;

import base.DrawString;
import base.Scene;
import base.SpectrogramDrawing;
import interactable.AudioPlayButton;
import interactable.BackButton;
import interactable.GaussianSetter;
import interactable.Interactable;
import interactable.Setter;
import processing.core.PApplet;
import processing.core.PImage;
import sound.DrawSpectrogram;

public class SoundEditScene extends Scene {
	
	private DrawSpectrogram spectrogram;
	private Interactable[] interactables;
	public DrawString console;
	
	private Setter gaussianSetter;
	private int state;
	
	public float posX = 0.0f;
	
	public boolean isPlaying;
	
	private void initInteractables() {
		console = new DrawString("Info will be printed here.");
		
		interactables = new Interactable[2];
		interactables[0] = new BackButton(16, 400, 2);
		interactables[1] = new AudioPlayButton(656, 400, 2, this);
		
		gaussianSetter = new GaussianSetter(232, 400, spectrogram);
		
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
	}
	
	public SoundEditScene(PApplet parent, PImage image) throws IOException {
		super(720, 480, parent);
		spectrogram = new DrawSpectrogram(image, this);
		initInteractables();
		SpectrogramDrawing.main.transitToSoundEditting(this);
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
		
		console.draw(this, 360 - console.getWidth(16) / 2, height - 112, 16);
		
		for(Interactable i : interactables) {
			i.display(this);
		}
		
		switch(state) {
		case 0:
			gaussianSetter.display(this);
			break;
		case 1:
			
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
}
