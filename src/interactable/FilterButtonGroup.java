package interactable;

import scenes.SoundEditScene;

public class FilterButtonGroup extends ButtonGroup {
	SoundEditScene parentScene;

	public FilterButtonGroup(SoundEditScene parentScene) {
		super(0);
		this.parentScene = parentScene;
	}

	@Override
	public void interact() {
		if(sel >= 0 && sel < 4)
			parentScene.state = sel;
		parentScene.updateStagedImage();
	}

}
