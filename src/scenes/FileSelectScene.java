package scenes;

import java.io.File;

import base.DrawString;
import base.Scene;
import base.SpectrogramDrawing;
import interactable.AntialiasingButtonGroup;
import interactable.Button;
import interactable.ButtonGroup;
import interactable.FileSelectionButton;
import interactable.ProcessButton;
import processing.core.PApplet;
import resources.GraphicResouces;

public class FileSelectScene extends Scene {
	public File file;
	Button[] buttons;
	AntialiasingButtonGroup smoothSetting;
	public static DrawString workingPath = new DrawString("/..");
	public static String pathString = "";
	private final static DrawString GUI_SELECTED_FILE = new DrawString("Selected File:");

	public FileSelectScene(int width, int height, PApplet parent) {
		super(width, height, parent);
		
		smoothSetting = new AntialiasingButtonGroup();
		
		buttons = new Button[5];
		buttons[0] = new ButtonGroup.GroupedButton(GraphicResouces.SMOOTH_OFF, 128, 8, 2, smoothSetting);
		buttons[1] = new ButtonGroup.GroupedButton(GraphicResouces.SMOOTH_2X, 192, 8, 2, smoothSetting);
		buttons[2] = new ButtonGroup.GroupedButton(GraphicResouces.SMOOTH_3X, 240, 8, 2, smoothSetting);
		buttons[3] = new FileSelectionButton(512, 352, 2);
		buttons[4] = new ProcessButton(512, 424, 2);
	}

	@Override
	public void drawContent() {
		background(255, 245, 157);
		
		stroke(251, 192, 45);
		strokeWeight(4);
		
		translate(SpectrogramDrawing.main.transitionFactor, 0);
		
		image(GraphicResouces.SMOOTH, 16, 10, 96, 20);
		line(-1, 40, width + 1, 40);
		
		line(-1, 344, width + 1, 344);
		GUI_SELECTED_FILE.draw(this, 8, 352, 16);
		workingPath.draw(this, 8, 384, 16);
		
		for(Button b : buttons) {
			b.display(this);
		}
	}

}
