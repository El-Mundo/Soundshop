package interactable;

import base.SpectrogramDrawing;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import resources.GraphicResouces;

public abstract class Setter extends Interactable {
	boolean selected = false, pSelected = false;
	float value = 0.0f;
	int pixelWidth = 0;
	private PImage bkg;
	int rx;

	public Setter(int x, int y, float scale) {
		super(GraphicResouces.SETTER, x, y, scale);
		this.bkg = GraphicResouces.SETTER_BKG;
		this.pixelWidth = 256 - 16;
		this.rx = x;
	}
	
	public void reset() {
		value = 0.0f;
	}
	
	@Override
	public void display(PGraphics g) {
		g.image(this.bkg, this.rx, this.y + 4, pixelWidth + 16, 16);
		
		x = (int) (this.rx + this.value * pixelWidth);
		if(inactive || SpectrogramDrawing.main.transiting) {
			g.tint(220, 120, 0);
			if(image != null) {
				g.image(this.image, x, this.y, this.width, this.height);
			}else {
				g.rect(x, this.y, this.width, this.height);
			}
			g.noTint();
		}else if(mouseSelected(g) || selected) {
			g.push();
			int tempW = width;
			int tempH = height;
			if(SpectrogramDrawing.clicked) {
				g.tint(80);
				tempW = (int) (width * 0.9f);
				tempH = (int) (height * 0.9f);
				this.selected = true;
			}else {
				g.tint(170);
			}
			if(image != null) {
				g.image(this.image, x + (width - tempW) / 2, this.y + (height - tempH) / 2, tempW, tempH);
			}else {
				g.rect(x, this.y, this.width, this.height);
			}
			g.noTint();
			g.pop();
		}else {
			if(image != null) {
				g.image(this.image, x, this.y, this.width, this.height);
			}else {
				g.rect(x, this.y, this.width, this.height);
			}
		}
		if(!SpectrogramDrawing.clicked) {
			this.selected = false;
			if(pSelected) interact();
		}else if(selected) {
			updateValue();
		}
		pSelected = selected;
	}
	
	private void updateValue() {
		value = ((float)SpectrogramDrawing.cursorX - (float)this.rx) / (float)pixelWidth;
		value = PApplet.max(0.0f, value);
		value = PApplet.min(1.0f, value);
	}

	@Override
	public abstract void interact();

}
