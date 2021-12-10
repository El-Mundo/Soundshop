package scenes;

import java.io.File;

import base.DrawString;
import base.Scene;
import interactable.AntialiasingButtonGroup;
import interactable.Button;
import interactable.ButtonGroup;
import interactable.FileSelectionButton;
import processing.core.PApplet;
import resources.GraphicResouces;

public class FileSelectScene extends Scene {
	public File file;
	Button[] buttons;
	AntialiasingButtonGroup smoothSetting;
	public static DrawString workingPath = new DrawString(" !\"#\\%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLM\nNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~");
	public static String pathString = "";

	public FileSelectScene(int width, int height, PApplet parent) {
		super(width, height, parent);
		
		smoothSetting = new AntialiasingButtonGroup();
		
		buttons = new Button[4];
		buttons[0] = new ButtonGroup.GroupedButton(GraphicResouces.SMOOTH_OFF, 128, 16, 2, smoothSetting);
		buttons[1] = new ButtonGroup.GroupedButton(GraphicResouces.SMOOTH_2X, 192, 16, 2, smoothSetting);
		buttons[2] = new ButtonGroup.GroupedButton(GraphicResouces.SMOOTH_3X, 240, 16, 2, smoothSetting);
		buttons[3] = new FileSelectionButton(width / 2, height / 2, 2);
	}

	@Override
	public void drawContent() {
		background(255);
		
		image(GraphicResouces.SMOOTH, 16, 18, 96, 20);
		workingPath.draw(this, 0, 360, 16);
		
		for(Button b : buttons) {
			b.display(this);
		}
	}

}
