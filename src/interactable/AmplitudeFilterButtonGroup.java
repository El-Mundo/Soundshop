package interactable;

import sound.DrawSpectrogram;

public class AmplitudeFilterButtonGroup extends ButtonGroup {

	public AmplitudeFilterButtonGroup() {
		super(1);
	}

	@Override
	public void interact() {
		DrawSpectrogram.filterAmplitude = (sel != 0);
	}

}
