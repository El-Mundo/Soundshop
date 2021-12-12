package interactable;

import java.io.File;
import java.io.IOException;

import base.AudioOutput;
import base.SpectrogramDrawing;
import resources.AudioResources;
import resources.GraphicResouces;
import scenes.FileSelectScene;

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
			
			SpectrogramDrawing.loadingSceneInBkg = true;

		} catch (IOException e) {
			e.printStackTrace();
			loadingFailed();
		}
	}
	
	private static void loadingFailed() {
		FileSelectScene.guideline.setColor(180, 0, 0);
		FileSelectScene.guideline.update("Cannot read input audio file!");
		//AudioResources.SFX_WARNING.play();
		AudioOutput.playBytesAsAudio(AudioResources.BYTE_SFX_WARNING);
	}


}
