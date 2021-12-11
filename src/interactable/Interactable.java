package interactable;

import base.SpectrogramDrawing;
import processing.core.PGraphics;
import processing.core.PImage;

public abstract class Interactable {
	
	private final static float PRESS_SCALE = 0.9f;
	
	protected int x, y, width, height;
	protected PImage image;
	public boolean inactive;

	public Interactable(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.image = null;
		this.inactive = false;
	}
	
	public Interactable(PImage image, int x, int y) {
		this.image = image;
		this.x = x;
		this.y = y;
		this.width = image.width;
		this.height = image.height;
		this.inactive = false;
	}
	
	public Interactable(PImage image, int x, int y, float scale) {
		this(image, x, y);
		this.width = (int) ((float)image.width * scale);
		this.height = (int) ((float)image.height * scale);
	}

	public boolean mouseSelected(PGraphics g) {
		if(SpectrogramDrawing.cursorX > x && SpectrogramDrawing.cursorX < x + width) {
			if(SpectrogramDrawing.cursorY > y && SpectrogramDrawing.cursorY < y + height) {
				return true;
			}
		}
		return false;
	}
	
	public void display(PGraphics g) {
		if(inactive) {
			g.tint(220, 120, 0);
			if(image != null) {
				g.image(this.image, this.x, this.y, this.width, this.height);
			}else {
				g.rect(this.x, this.y, this.width, this.height);
			}
			g.noTint();
		}else if(mouseSelected(g)) {
			g.push();
			int tempW = width;
			int tempH = height;
			if(SpectrogramDrawing.clicked) {
				g.tint(80);
				tempW = (int) (width * PRESS_SCALE);
				tempH = (int) (height * PRESS_SCALE);
			}else {
				g.tint(170);
				if(SpectrogramDrawing.pClicked) {
					interact();
				}
			}
			if(image != null) {
				g.image(this.image, this.x + (width - tempW) / 2, this.y + (height - tempH) / 2, tempW, tempH);
			}else {
				g.rect(this.x, this.y, this.width, this.height);
			}
			g.noTint();
			g.pop();
		}else {
			if(image != null) {
				g.image(this.image, this.x, this.y, this.width, this.height);
			}else {
				g.rect(this.x, this.y, this.width, this.height);
			}
		}
	}
	
	public abstract void interact();
	
}
