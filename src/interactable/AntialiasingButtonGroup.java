package interactable;

import base.WaveformDrawing;

public class AntialiasingButtonGroup extends ButtonGroup {

	public AntialiasingButtonGroup() {
		super(0);
	}

	@Override
	public void interact() {
		if(sel >= 0 && sel < 3)
			WaveformDrawing.antialiasingLevel = sel;
	}

}
