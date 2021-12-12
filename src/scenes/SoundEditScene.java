package scenes;

import java.io.IOException;

import base.Scene;
import base.SpectrogramDrawing;
import interactable.BackButton;
import interactable.Interactable;
import processing.core.PApplet;
import sound.DrawSpectrogram;

public class SoundEditScene extends Scene {
	
	private DrawSpectrogram spectrogram;
	private Interactable[] interactables;
	
	public SoundEditScene(PApplet parent) throws IOException {
		super(720, 480, parent);
		spectrogram = new DrawSpectrogram();
		SpectrogramDrawing.main.transitToSoundEditting(this);
		
		interactables = new Interactable[1];
		interactables[0] = new BackButton(16, 400, 2);
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
		
		for(Interactable i : interactables) {
			i.display(this);
		}
	}
}
