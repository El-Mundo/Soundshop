package interactable;

import processing.core.PGraphics;
import resources.GraphicResouces;
import scenes.SoundEditScene;

public class AudioPlayButton extends Button {
	SoundEditScene parentScene;

	public AudioPlayButton(int x, int y, float scale, SoundEditScene parent) {
		super(GraphicResouces.PLAY_BUTTON, x, y, scale);
		this.parentScene = parent;
	}
	
	@Override
	public void display(PGraphics g) {
		super.display(g);
		if(parentScene.isPlaying) {
			this.image = GraphicResouces.PAUSE_BUTTON;
		}else {
			this.image = GraphicResouces.PLAY_BUTTON;
		}
	}

	@Override
	public void interact() {
		if(parentScene.isPlaying) {
			parentScene.isPlaying = false;
		}else {
			parentScene.play();
			parentScene.isPlaying = true;
		}
	}



}
