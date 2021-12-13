package base;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import ddf.minim.ugens.Damp;
import ddf.minim.ugens.Frequency;
import ddf.minim.ugens.Oscil;
import ddf.minim.ugens.Waves;
import resources.MidiResources;

public class AudioOutput {
	private static Sequencer sequencer;
	private static boolean midiAvailable = false;
	private static Sequence sequence;
	
	public static void initAudioDevice() {
		playBytesAsAudio(AUDIO_STARTING_DATA);
		
		//Initialize MIDI device here
		try {
			sequencer = MidiSystem.getSequencer(false);
			if (sequencer == null) {
	            System.err.println("Sequencer device not supported");
	            return;
			}
			sequencer.open();
			sequence = MidiSystem.getSequence(new ByteArrayInputStream(MidiResources.BYTE_MUSIC));
			sequencer.setSequence(sequence);
			sequencer.getTransmitter().setReceiver(new MidiReceiver());
			sequencer.setLoopCount(0);
			
			midiAvailable = true;
		} catch (MidiUnavailableException | InvalidMidiDataException | IOException e) {
			e.printStackTrace();
			midiAvailable = false;
		}
	}
	
	public static void stopMusic() {
		if(midiAvailable) {
			sequencer.stop();
			sequencer.close();
			SpectrogramDrawing.minimOut.close();
		}
	}
	
	public static void playMusic() {
		if(midiAvailable) {
			//sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
			try {
				sequencer.open();
				sequencer.setSequence(sequence);
				sequencer.getTransmitter().setReceiver(new MidiReceiver());
				sequencer.setLoopCount(0);
				sequencer.start();
				sequencer.setTickPosition(0);
				SpectrogramDrawing.minimOut = SpectrogramDrawing.minim.getLineOut();
				sequencer.start();
			} catch (MidiUnavailableException | InvalidMidiDataException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void playBytesAsAudio(final byte[] data) {
        //https://stackoverflow.com/questions/14945217/playing-sound-from-a-byte-array
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
	
	private static class MidiReceiver implements Receiver {
	  public void close() {}
	  
	  public void send(MidiMessage msg, long timeStamp) {
	    if (msg instanceof ShortMessage) {
	      ShortMessage sm = (ShortMessage)msg;
	      if(sm.getCommand() == ShortMessage.NOTE_ON) {
	        int note = sm.getData1();
	        int vel = sm.getData2();
	        int chn = sm.getChannel();
	        if(chn == 0) {
	        	SpectrogramDrawing.minimOut.playNote(0, 128.0f, new Synth(note, vel, 0.04f, 0));
	        }else if(chn == 1) {
	        	SpectrogramDrawing.minimOut.playNote(0, 128.0f, new Synth(note, vel, 0.03f, 1));
	        }else if(chn == 2) {
	        	SpectrogramDrawing.minimOut.playNote(0, 128.0f, new Synth(note, vel, 0.03f, 2));
	        }
	      }
	    }
	  }
	}
	  
	private static class Synth implements ddf.minim.ugens.Instrument {
	  Oscil wave;
	  Damp env;
	  int noteNumber;
	  
	  Synth(int note, int velocity, float vol, int chn) {
	    noteNumber = note;
	    float freq = Frequency.ofMidiNote(noteNumber).asHz();
	    float amp = (float)(velocity-1) / 1008.0f;
	    
	    if(chn == 2)
	    	wave = new Oscil(freq, amp, Waves.SAW);
	    else
	    	wave = new Oscil(freq, amp, Waves.QUARTERPULSE);
	    env = new Damp(0.001f, 0.5f, vol);
	    
	    wave.patch(env);
	  }
	  
	  public void noteOn(float dur) {
	    env.activate();
	    env.patch(SpectrogramDrawing.minimOut);
	  }
	  
	  public void noteOff(){
	    env.unpatchAfterDamp(SpectrogramDrawing.minimOut);
	  }
	}

}
