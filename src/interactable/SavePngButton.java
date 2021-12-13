package interactable;

import base.AudioOutput;
import base.SpectrogramDrawing;
import resources.AudioResources;
import resources.GraphicResouces;
import sound.DrawSpectrogram;

public class SavePngButton extends Button {
	private DrawSpectrogram spectrogram;

	public SavePngButton(int x, int y, float scale, DrawSpectrogram spectrogram) {
		super(GraphicResouces.FORMAT_PNG, x, y, scale);
		this.spectrogram = spectrogram;
	}

	@Override
	public void interact() {
		String path = SpectrogramDrawing.main.soundEditScene.savePng();
		spectrogram.log("Png saved at " + path);
		AudioOutput.playBytesAsAudio(AudioResources.BYTE_SFX_HINT);
	}

}
