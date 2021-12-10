package base;

import java.awt.Graphics2D;
import java.awt.RenderingHints;

import processing.awt.PGraphicsJava2D;
import processing.core.PApplet;

public abstract class Scene extends PGraphicsJava2D {

	public Scene(int width, int height, PApplet parent) {
		super();
		this.setParent(parent);
		this.setPrimary(false);
		this.setSize(width, height);
	}
	
	public void draw() {
		beginDraw();
		antialiase();
		drawContent();
		endDraw();
	}
	
	public abstract void drawContent();
	
	private void antialiase() {
		//Processing's anti-aliasing params can only be changed in settings() in non-PDE mode,
		//so I used JAVA's native anti-aliasing functions in the Graphics2D class.
		Graphics2D nativGraphics2d;
		switch (WaveformDrawing.antialiasingLevel) {
		case 1:
			//Apply bilinear filter to rendered image
			nativGraphics2d = ((Graphics2D)getNative());
			nativGraphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			nativGraphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			break;
			
		case 2:
			//Apply tri-linear filter to rendered image
			nativGraphics2d = ((Graphics2D)getNative());
			nativGraphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			nativGraphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			break;
			
		default:
			//Nothing done if no anti-aliasing needed
			break;
		}
	}
}
