package scenes;

import java.io.File;

import base.Scene;
import processing.core.PApplet;
import resources.GraphicResouces;

public class FileSelectScene extends Scene {
	public File file;
	int f = 0, p = 1;

	public FileSelectScene(int width, int height, PApplet parent) {
		super(width, height, parent);
	}

	@Override
	public void drawContent() {
		background(f);
		f += p;
		if(f > 250) p = -1;
		else if(f < 5) p = 1;
		image(GraphicResouces.BUTTON_CORNER, 360 - 64, 240 - 64, 64, 64);
	}

}
