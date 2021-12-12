package interactable;

import base.SpectrogramDrawing;
import resources.GraphicResouces;

public class BackButton extends Button {

	public BackButton(int x, int y, float scale) {
		super(GraphicResouces.BACK_BUTTON, x, y, scale);
	}

	@Override
	public void interact() {
		SpectrogramDrawing.main.transitToFileSelection();
	}

}
