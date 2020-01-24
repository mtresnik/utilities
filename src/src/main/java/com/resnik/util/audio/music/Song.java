package com.resnik.util.audio.music;

import javax.sound.midi.*;
import java.util.ArrayList;

public class Song extends ArrayList<NoteCollection> {

    final long MPQ;
    final static long DEFAULT_MPQ = 60*10000;
    int loopCount;

    public Song(int loopCount){
        this.MPQ = DEFAULT_MPQ;
        this.loopCount = loopCount;
    }

    public Song(float BPM, int loopCount){
        this.MPQ = (long)(BPM*10000);
        this.loopCount = loopCount;
    }

    public void play() throws MidiUnavailableException, InvalidMidiDataException, InterruptedException {
        Sequencer sequencer = MidiSystem.getSequencer();
        sequencer.open();

        Sequence sequence = new Sequence(Sequence.PPQ, 1);
        Synthesizer synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();
        Receiver receiver = synthesizer.getReceiver();
        sequencer.setSequence(sequence);
        sequencer.setTempoInMPQ(this.MPQ);
        System.out.println("mpq:" + sequencer.getTempoInMPQ());
        System.out.println("bpm:" + sequencer.getTempoInBPM());
        System.out.println("factor:" + sequencer.getTempoFactor());

        for(int index = 0; index < this.size(); index++){
            NoteCollection collection = this.get(index);
            collection.createTrack(sequence);
        }
        sequencer.setSequence(sequence);
        sequencer.setLoopCount(loopCount);
        sequencer.start();
    }

}
