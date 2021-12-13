package interactable;

import base.SpectrogramDrawing;
import sound.DrawSpectrogram;

public class RadialSetter extends Setter {
	private DrawSpectrogram spectrogram;

	public RadialSetter(int x, int y, DrawSpectrogram spectrogram) {
		super(x, y, 2);
		this.spectrogram = spectrogram;
	}

	@Override
	public void interact() {
		SpectrogramDrawing.filter(value, spectrogram, SpectrogramDrawing.Filters.RADIAL);
		spectrogram.log("processing image...");
	}



}
