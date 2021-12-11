package scenes;

import base.Scene;
import processing.core.PApplet;

public class SoundEditScene extends Scene {
	
	@SuppressWarnings("unused")
	private byte[] source;
	
	public SoundEditScene(byte[] encodedAudio, PApplet parent) {
		super(720, 480, parent);
		source = encodedAudio;
	}

	@Override
	public void drawContent() {
		// TODO Auto-generated method stub
		
	}
}
