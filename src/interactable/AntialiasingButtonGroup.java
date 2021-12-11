package interactable;

import base.SpectrogramDrawing;

public class AntialiasingButtonGroup extends ButtonGroup {

	public AntialiasingButtonGroup() {
		super(0);
	}

	@Override
	public void interact() {
		if(sel >= 0 && sel < 3)
			SpectrogramDrawing.antialiasingLevel = sel;
	}

}
