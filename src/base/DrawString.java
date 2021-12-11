package base;

import processing.core.PGraphics;
import resources.GraphicResouces;

public class DrawString {
	public char[] content;
	public int[] color;

	public DrawString(String content) {
		update(content);
		color = new int[3];
		color[0] = color[1] = color[2] = 0;
	}
	
	public void draw(PGraphics g, int x, int y, int size) {
		g.tint(color[0], color[1], color[2]);
		int dx = 0, dy = 0;
		for(char c : content) {
			byte p = (byte)(c - 32);
			if(0 <= p && p < 100) {
				g.image(GraphicResouces.LETTERS[p], x + dx, y + dy, size, size);
			}else if(p == -22) {
				dy += size;
				dx = -size;
			}else {
				g.image(GraphicResouces.LETTERS[31], x + dx, y + dy, size, size);
			}
			dx += size;
		}
		g.noTint();
	}
	
	public void draw(PGraphics g, int x, int y) {
		draw(g, x, y, 8);
	}
	
	public void update(String newContent) {
		content = newContent.toCharArray();
	}
	
	public void setColor(int r, int g, int b) {
		color[0] = r;
		color[1] = g;
		color[2] = b;
	}

}
