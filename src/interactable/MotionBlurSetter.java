package interactable;

import base.SpectrogramDrawing;
import sound.DrawSpectrogram;

public class MotionBlurSetter extends Setter {
	private DrawSpectrogram spectrogram;

	public MotionBlurSetter(int x, int y, DrawSpectrogram spectrogram) {
		super(x, y, 2);
		this.spectrogram = spectrogram;
	}

	@Override
	public void interact() {
		SpectrogramDrawing.filter(value, spectrogram, SpectrogramDrawing.Filters.MOTION);
		spectrogram.log("processing image...");
	}

}
