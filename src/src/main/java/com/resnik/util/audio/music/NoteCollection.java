package com.resnik.util.audio.music;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.Arrays;

public class NoteCollection extends ArrayList<Note> implements Playable {


    public int instrument(){
        return this.first().instrument;
    }

    @Override
    public boolean add(Note note){
        return super.add(note);
    }

    public boolean add(NoteCollection collection){
        boolean ret = true;
        for(Note note : collection){
            ret &= this.add(note);
        }
        return ret;
    }

    @Override
    public MidiEvent[] getStartEvents() throws InvalidMidiDataException {
        MidiEvent[] retArray = new MidiEvent[this.size()];
        for(int i = 0; i < this.size(); i++){
            retArray[i] = this.get(i).startEvent();
        }
        return retArray;
    }

    @Override
    public MidiEvent[] getEndEvents() throws InvalidMidiDataException {
        MidiEvent[] retArray = new MidiEvent[this.size()];
        for(int i = 0; i < this.size(); i++) {
            retArray[i] = this.get(i).endEvent();
        }
        return retArray;
    }

    public Track createTrack(Sequence sequence) throws InvalidMidiDataException {
        System.out.println(Arrays.toString(getAllEvents()));
        Track retTrack = sequence.createTrack();
        MidiEvent[] starts = this.getStartEvents(), ends = this.getEndEvents();
        for(int i = 0; i < starts.length; i++){
            retTrack.add(starts[i]);
            retTrack.add(ends[i]);
        }
        return retTrack;
    }


    public Note last(){
        return this.get(this.size() - 1);
    }

    public Note first(){
        return this.get(0);
    }
}
