package scenes;

import java.io.IOException;

import base.DrawString;
import base.Scene;
import base.SpectrogramDrawing;
import interactable.AudioPlayButton;
import interactable.BackButton;
import interactable.Interactable;
import processing.core.PApplet;
import processing.core.PImage;
import sound.DrawSpectrogram;

public class SoundEditScene extends Scene {
	
	private DrawSpectrogram spectrogram;
	private Interactable[] interactables;
	private DrawString console;
	
	public boolean isPlaying;
	
	private void initInteractables() {
		console = new DrawString("aaaa");
		
		interactables = new Interactable[2];
		interactables[0] = new BackButton(16, 400, 2);
		interactables[1] = new AudioPlayButton(656, 400, 2, this);
		
		isPlaying = false;
	}
	
	public SoundEditScene(PApplet parent, PImage image) throws IOException {
		super(720, 480, parent);
		spectrogram = new DrawSpectrogram(image);
		initInteractables();
		SpectrogramDrawing.main.transitToSoundEditting(this);
	}
	
	public SoundEditScene(PApplet parent) throws IOException {
		super(720, 480, parent);
		spectrogram = new DrawSpectrogram();
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
		stroke(251, 192, 45);
		strokeWeight(4);
		rect(6, 6, width-12, height-120);
		
		console.draw(this, 360 - console.getWidth(16) / 2, height - 112, 16);
		
		for(Interactable i : interactables) {
			i.display(this);
		}
	}
}
