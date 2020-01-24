package com.resnik.util.audio;

import com.resnik.util.audio.music.Note;
import com.resnik.util.audio.music.NoteCollection;
import com.resnik.util.audio.music.Song;
import com.resnik.util.audio.music.scales.Major;
import com.resnik.util.audio.music.scales.Scale;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;

public class TestAudio {

    public static void testScales() throws MidiUnavailableException, InvalidMidiDataException, InterruptedException {
        // Only piano is working rn
        long start_time = 0;
        long duration = 1;
        int PIANO = 1;
        int pitch = 40 + 12;
        int volume = 50;
        Note note = new Note(PIANO, pitch, volume, start_time, duration);
        NoteCollection track1 = new NoteCollection();
        track1.add(new Major(note, Scale.DIRECTION.UP));
        NoteCollection track2 = new NoteCollection();
        Note otherNote = new Note(4, 40 + 12, 50, 0, 1);
        System.out.println(otherNote);
        Scale scale2 = new Major(otherNote,0, Scale.DIRECTION.DOWN);
        System.out.println(scale2);
        track2.add(scale2);
        Song song = new Song(60, Sequencer.LOOP_CONTINUOUSLY);
        song.add(track1);
        song.add(track2);
        song.play();
    }

    public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException, InterruptedException {
        testScales();
    }

}
