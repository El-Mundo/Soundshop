package interactable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
			BufferedReader reader = new BufferedReader(new FileReader(f));
			int i;
			while((i = reader.read()) != -1) {
				System.out.print(i);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
