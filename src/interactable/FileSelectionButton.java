package interactable;

import java.awt.FileDialog;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import base.SpectrogramDrawing;
import resources.GraphicResouces;
import scenes.FileSelectScene;

public class FileSelectionButton extends Button {
	
	private JFileChooser chooser;
	
	final private static FileNameExtensionFilter EXTENSION_FILTER = new FileNameExtensionFilter("PCM wav audio (*.wav)", "wav");
	final private static FileNameExtensionFilter EXTENSION_FILTER_2 = new FileNameExtensionFilter("PNG image (*.png)", "png");

	public FileSelectionButton(int x, int y, float scale) {
		super(GraphicResouces.FILE_SELECTOR, x, y, scale);
		chooser = new JFileChooser();
		chooser.setFileFilter(EXTENSION_FILTER);
		chooser.addChoosableFileFilter(EXTENSION_FILTER_2);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
	}

	@Override
	public void interact() {
		File file = null;
		//Get file chooser based on the operating system
		if(System.getProperty("os.name").contains("Mac")) {
			//Replace with apple's native functions
			FileDialog fileChooser = new FileDialog(SpectrogramDrawing.nativeWindow, "Open File");
			fileChooser.setMode(FileDialog.LOAD);
			fileChooser.setFilenameFilter(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(".wav") || name.toLowerCase().endsWith(".png");
				}
			});
			fileChooser.setVisible(true);
			
			file = new File(fileChooser.getDirectory(), fileChooser.getFile());
		}else {
			chooser.showOpenDialog(SpectrogramDrawing.nativeWindow);
			if(chooser.getSelectedFile() != null) {
				file = chooser.getSelectedFile();
			}
		}
		
		//Get selected file
		if(file != null) {
			FileSelectScene.selectedFile = file;
			String path = file.getAbsolutePath();
			FileSelectScene.pathString = path;
			for(int i = 1; i <= 6; i ++) {
				int tar = i * 30;
				if(path.length() > tar) {
					path = path.substring(0, tar) + '\n' + path.substring(tar, path.length());
					if(i == 6 && path.length() >= 180) {
						path = path.substring(0, 177);
						path += "...";
					}
				}
			}
			FileSelectScene.workingPath.update(path);
		}
	}

}
