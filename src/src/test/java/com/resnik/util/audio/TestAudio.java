package com.resnik.util.audio;


import com.resnik.util.audio.music.Note;
import com.resnik.util.audio.music.NoteCollection;
import com.resnik.util.audio.music.Pitch;
import com.resnik.util.audio.music.Song;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import java.io.IOException;

public class TestAudio {


    public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException, InterruptedException, IOException {

        NoteCollection collection = new NoteCollection();
        for(int i = 0; i < 40; i+=4){
            collection.add(new Note(20, 0, Pitch.C(3), 50, i, 1));
            collection.add(new Note(20, 1, Pitch.E(3), 50, i, 2));
            collection.add(new Note(20, 2, Pitch.G(3), 50, i, 4));
        }
        Song song = new Song(60, 3);
        song.add(collection);
//        song.save("res/test.mid");
        song.play(Sequencer.LOOP_CONTINUOUSLY);

    }

}
