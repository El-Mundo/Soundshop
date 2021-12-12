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
		clear();
		
		translate(SpectrogramDrawing.main.transitionFactor + width, 0);
		rect(-1, -1, width + 2, height + 2);
		
		spectrogram.draw(this, 20, 20, width-40, height-40);
		
		for(Interactable i : interactables) {
			i.display(this);
		}
	}
}
