package scenes;

import java.io.IOException;

import base.DrawString;
import base.SpectrogramDrawing;
import interactable.BackButton;
import interactable.ButtonGroup;
import interactable.FilterButtonGroup;
import interactable.GaussianSetter;
import interactable.Interactable;
import interactable.MotionBlurSetter;
import interactable.RadialSetter;
import network.ClientThread;
import processing.core.PApplet;
import processing.core.PImage;
import resources.GraphicResouces;

public class ClientScene extends SoundEditScene {
	
	private DrawString clientString;
	
	@Override
	public void initInteractables() {
		console = new DrawString("Info will be printed here.");
		
		interactables = new Interactable[4];
		interactables[0] = new BackButton(16, 400, 2);
		
		gaussianSetter = new GaussianSetter(232, 400, spectrogram);
		radialSetter = new RadialSetter(232, 400, spectrogram);
		motionSetter = new MotionBlurSetter(232, 400, spectrogram);
		
		filterButtonGroup = new FilterButtonGroup(this);
		interactables[1] = new ButtonGroup.GroupedButton(GraphicResouces.FILTER_GAUSSIAN, 90, 448, 2, filterButtonGroup);
		interactables[2] = new ButtonGroup.GroupedButton(GraphicResouces.FILTER_MOTION, 260, 448, 2, filterButtonGroup);
		interactables[3] = new ButtonGroup.GroupedButton(GraphicResouces.FILTER_RADIAL, 400, 448, 2, filterButtonGroup);
		
		isPlaying = false;
		state = 0;
		
		clientString = new DrawString("CLIENT");
		clientString.setColor(180, 0, 0);
	}
	
	public ClientScene(PApplet parent, PImage image) throws IOException {
		super(parent, image);
	}
	
	public ClientScene(PApplet parent) throws IOException {
		super(parent);
	}
	
	@Override
	public void updateStagedImage() {
		spectrogram.setSourceImage(spectrogram.getImage());
		gaussianSetter.reset();
		radialSetter.reset();
		motionSetter.reset();
		spectrogram.log("Wating for the host to start.");
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
		}
		
		clientString.draw(this, 580, height - 64, 16);
		
		if(SpectrogramDrawing.main.getState() == 1) {
			//Do not process messages from the host when quitting
			if(ClientThread.clientMessage.contains("11") && !isPlaying) {
				posX = 0.0f;
				play();
				isPlaying = true;
			}else if(ClientThread.clientMessage.contains("12") && isPlaying) {
				posX = 0.0f;
				isPlaying = false;
			}
		}
	}
	
}
