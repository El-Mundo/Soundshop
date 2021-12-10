package interactable;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import base.WaveformDrawing;
import resources.GraphicResouces;
import scenes.FileSelectScene;

public class FileSelectionButton extends Button {
	
	private JFileChooser chooser;
	
	final private static FileNameExtensionFilter EXTENSION_FILTER = new FileNameExtensionFilter("PCM wav audio (*.wav)", "wav");

	public FileSelectionButton(int x, int y, float scale) {
		super(GraphicResouces.FILE_SELECTOR, x, y, scale);
		chooser = new JFileChooser();
		chooser.setFileFilter(EXTENSION_FILTER);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
	}

	@Override
	public void interact() {
		chooser.showDialog(WaveformDrawing.nativeWindow, "Select a file");
		//Get selected file
		if(chooser.getSelectedFile()!=null) {
			FileSelectScene.pathString = chooser.getSelectedFile().getAbsolutePath();
			FileSelectScene.workingPath.update(FileSelectScene.pathString);
			System.out.println(FileSelectScene.pathString);
		}
	}

}
