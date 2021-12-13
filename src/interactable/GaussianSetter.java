package interactable;

import base.SpectrogramDrawing;
import sound.DrawSpectrogram;

public class GaussianSetter extends Setter {
	private DrawSpectrogram spectrogram;

	public GaussianSetter(int x, int y, DrawSpectrogram spectrogram) {
		super(x, y, 2);
		this.spectrogram = spectrogram;
	}
	
	@Override
	public void interact() {
		System.out.println(value);
		//Use Processing's Gausisan Blur instead of a homebrew one for better results
		SpectrogramDrawing.filter(value, spectrogram, SpectrogramDrawing.Filters.GAUSSIAN);
		spectrogram.log("processing image...");
	}

}
