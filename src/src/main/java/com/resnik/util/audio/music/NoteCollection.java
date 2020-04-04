package com.resnik.util.audio.music;

import com.resnik.util.logger.Log;
import com.resnik.util.math.plot.histogram.HistogramData;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.Arrays;

public class NoteCollection extends ArrayList<Note> implements Playable {

    public static final String TAG = NoteCollection.class.getSimpleName();

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
        Log.v(TAG,Arrays.toString(getAllEvents()));
        Track retTrack = sequence.createTrack();
        MidiEvent[] starts = this.getStartEvents(), ends = this.getEndEvents();
        for(int i = 0; i < starts.length; i++){
            retTrack.add(new MidiEvent(new ShortMessage(ShortMessage.PROGRAM_CHANGE, this.get(i).channel, this.get(i).instrument, 0),0));
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
