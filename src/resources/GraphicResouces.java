package resources;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import processing.core.PImage;

public class GraphicResouces {
	public final static byte[] BYTE_BUTTON_CORNER = {
		-119,80,78,71,13,10,26,10,0,0,0,13,73,72,68,82,0,0,0,16,
		0,0,0,16,8,6,0,0,0,31,-13,-1,97,0,0,0,1,115,82,71,
		66,0,-82,-50,28,-23,0,0,0,84,73,68,65,84,56,-115,99,100,64,3,
		-5,-51,120,90,-47,-59,-16,1,70,108,-102,29,79,125,-87,34,-39,0,100,-51,
		-1,-13,73,116,1,-122,102,77,18,12,-64,-91,-103,49,-125,56,3,88,112,105,
		-2,90,47,78,-108,1,76,24,78,34,65,51,-36,5,120,21,104,-14,-111,-26,
		2,82,52,19,52,-128,24,48,106,-64,96,48,0,0,-102,31,31,21,25,-86,
		-53,-45,0,0,0,0,73,69,78,68,-82,66,96,-126
	};
	public final static PImage BUTTON_CORNER = byteToImage(BYTE_BUTTON_CORNER);
	
	private static PImage byteToImage(byte[] bytes) {
	    try {
	    	ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
	    	BufferedImage bi = ImageIO.read(bais);
	        return new PImage(bi);
	    }catch (IOException e) {
	        throw new RuntimeException(e);
	    }
	}

}
