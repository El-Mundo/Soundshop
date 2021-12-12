package interactable;

import java.io.File;
import java.io.IOException;

import base.AudioOutput;
import base.SpectrogramDrawing;
import resources.AudioResources;
import resources.GraphicResouces;
import scenes.FileSelectScene;
import scenes.SoundEditScene;

public class ProcessButton extends Button {

	public ProcessButton(int x, int y, float scale) {
		super(GraphicResouces.PROCESS_BUTTON, x, y, scale);
	}

	@Override
	public void interact() {
		try {
			File f = new File(FileSelectScene.pathString);
			if(!f.canRead()) throw new IOException();
			
			AudioOutput.playBytesAsAudio(AudioResources.BYTE_SFX_HINT);
			
			new SoundEditScene(SpectrogramDrawing.main);
		} catch (IOException e) {
			e.printStackTrace();
			FileSelectScene.guideline.setColor(180, 0, 0);
			FileSelectScene.guideline.update("Cannot read input audio file!");
			//AudioResources.SFX_WARNING.play();
			AudioOutput.playBytesAsAudio(AudioResources.BYTE_SFX_WARNING);
		}
	}


}
