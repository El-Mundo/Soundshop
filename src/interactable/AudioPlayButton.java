package interactable;

import resources.GraphicResouces;
import scenes.SoundEditScene;

public class AudioPlayButton extends Button {
	SoundEditScene parentScene;

	public AudioPlayButton(int x, int y, float scale, SoundEditScene parent) {
		super(GraphicResouces.PLAY_BUTTON, x, y, scale);
		this.parentScene = parent;
	}

	@Override
	public void interact() {
		if(parentScene.isPlaying) {
			
			this.image = GraphicResouces.PLAY_BUTTON;
			parentScene.isPlaying = false;
		}else {
			
			this.image = GraphicResouces.PAUSE_BUTTON;
			parentScene.isPlaying = true;
		}
	}



}
