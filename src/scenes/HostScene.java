package scenes;

import java.io.IOException;

import base.DrawString;
import base.SpectrogramDrawing;
import interactable.BackButton;
import interactable.HostPlaybackButton;
import interactable.Interactable;
import processing.core.PApplet;
import processing.core.PImage;

public class HostScene extends SoundEditScene {
	
	private DrawString clientString;
	
	@Override
	public void initInteractables() {
		console = new DrawString("Press the play button to start the clients.");
		
		interactables = new Interactable[2];
		interactables[0] = new BackButton(16, 400, 2);
		interactables[1] = new HostPlaybackButton(344, 400, 2, this);
		
		isPlaying = false;
		state = 0;
		
		clientString = new DrawString(" TEST \nSERVER");
		clientString.setColor(180, 0, 0);
	}
	
	public HostScene(PApplet parent, PImage image) throws IOException {
		super(parent, image);
	}
	
	public HostScene(PApplet parent) throws IOException {
		super(parent);
	}
	
	@Override
	public void updateStagedImage() {
		spectrogram.setSourceImage(spectrogram.getImage());
		spectrogram.log("Info will be printed here.");
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
		
		clientString.draw(this, 580, height - 80, 16);
	}
	
}
