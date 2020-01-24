package com.resnik.util.audio.music;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface Playable {

    MidiEvent[] getStartEvents() throws InvalidMidiDataException;

    MidiEvent[] getEndEvents() throws InvalidMidiDataException;

    default MidiEvent[] getAllEvents() throws InvalidMidiDataException{
        MidiEvent[] starts = this.getStartEvents();
        MidiEvent[] ends = this.getEndEvents();
        List<MidiEvent> yoke = new ArrayList<>();
        yoke.addAll(Arrays.asList(starts));
        yoke.addAll(Arrays.asList(ends));
        return yoke.toArray(new MidiEvent[yoke.size()]);
    }

}
