package base;

import processing.core.PGraphics;
import resources.GraphicResouces;

public class DrawString {
	public char[] content;

	public DrawString(String content) {
		update(content);
	}
	
	public void draw(PGraphics g, int x, int y, int size) {
		int dx = 0, dy = 0;
		for(char c : content) {
			byte p = (byte)(c - 32);
			if(0 <= p && p < 100) {
				g.image(GraphicResouces.LETTERS[p], x + dx, y + dy, size, size);
			}else if(p == -22) {
				dy += size;
				dx = -size;
			}
			dx += size;
		}
	}
	
	public void draw(PGraphics g, int x, int y) {
		draw(g, x, y, 8);
	}
	
	public void update(String newContent) {
		content = newContent.toCharArray();
	}

}
