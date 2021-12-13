package interactable;

import base.AudioOutput;
import base.SpectrogramDrawing;
import resources.AudioResources;
import resources.GraphicResouces;
import sound.DrawSpectrogram;

public class SaveWavButton extends Button {
	private DrawSpectrogram spectrogram;
	boolean triggered = false;

	public SaveWavButton(int x, int y, float scale, DrawSpectrogram spectrogram) {
		super(GraphicResouces.FORMAT_WAV, x, y, scale);
		this.spectrogram = spectrogram;
	}

	@Override
	public void interact() {
		if(triggered) {
			triggered = false;
			AudioOutput.playBytesAsAudio(AudioResources.BYTE_SFX_HINT);
			String path = SpectrogramDrawing.main.soundEditScene.saveWav();
			spectrogram.log("Wav saved at " + path);
			AudioOutput.playBytesAsAudio(AudioResources.BYTE_SFX_HINT);
		}else {
			AudioOutput.playBytesAsAudio(AudioResources.BYTE_SFX_HINT);
			spectrogram.log("This will start a recorder and force to play once, please click again to process.", true);
			triggered = true;
		}
	}
	
	public void reset() {
		triggered = false;
	}

}
