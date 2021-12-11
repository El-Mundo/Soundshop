package base;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioOutput {
	
	public static void initAudioDevice() {
		playBytesAsAudio(AUDIO_STARTING_DATA);
	}
	
	public static void playBytesAsAudio(final byte[] data) {
        //http://www.onjava.com/onjava/excerpt/jenut3_ch17/examples/SoundPlayer.java
		try {
			final AudioInputStream ain = AudioSystem.getAudioInputStream(new ByteArrayInputStream(data));
			try {
				final DataLine.Info info = new DataLine.Info(Clip.class, ain.getFormat());
				final Clip clip = (Clip) AudioSystem.getLine(info);
				clip.open(ain);
				clip.start();
			}catch (LineUnavailableException e) {
				e.printStackTrace();
			}finally {
				try {
					ain.close();
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		}catch(UnsupportedAudioFileException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
    }
	
	//These bytes will force JAVA to setup audio device
	private final static byte[] AUDIO_STARTING_DATA = {
			82,73,70,70,2,1,0,0,87,65,86,69,102,109,116,32,16,0,0,0,1,0,1,0,68,-84,0,0,68,-84,
			0,0,1,0,8,0,100,97,116,97,-35,0,0,0,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,
			-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,
			-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,
			-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,
			-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,
			-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,
			-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,
			-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,-128,0
	};

}
