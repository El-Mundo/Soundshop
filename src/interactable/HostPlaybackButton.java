package interactable;

import processing.core.PGraphics;
import resources.GraphicResouces;
import scenes.HostScene;

public class HostPlaybackButton extends Button {

	HostScene parentScene;

	public HostPlaybackButton(int x, int y, float scale, HostScene parent) {
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
			parentScene.posX = 0.0f;
			if(NetworkButton.room != null)
				NetworkButton.room.sendStopMessageToClients();
		}else {
			parentScene.play();
			parentScene.isPlaying = true;
			if(NetworkButton.room != null)
				NetworkButton.room.sendPlayMessageToClients();
		}
	}

}
