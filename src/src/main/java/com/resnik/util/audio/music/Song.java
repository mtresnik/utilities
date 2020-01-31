package com.resnik.util.audio.music;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Song extends ArrayList<NoteCollection> {

    final float BPM;
    final static long DEFAULT_MPQ = 60*10000;
    int beatsPerMeasure;
    int sixteenthNotesPerMeasure;

    public Song(float BPM, int beatsPerMeasure){
        this.BPM = BPM;
        this.beatsPerMeasure = beatsPerMeasure;
        this.sixteenthNotesPerMeasure = beatsPerMeasure* 4;
    }

    public void play(int loopCount) throws MidiUnavailableException, InvalidMidiDataException, InterruptedException {
        Sequencer sequencer = MidiSystem.getSequencer();
        sequencer.open();

        Sequence sequence = new Sequence(Sequence.PPQ, 1);
        Synthesizer synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();
        sequencer.setSequence(sequence);
//        sequencer.setTempoInBPM(BPM);
        sequence.createTrack().add(tempoEvent((long)BPM));
        for(int index = 0; index < this.size(); index++){
            NoteCollection collection = this.get(index);
            collection.createTrack(sequence);
        }
        sequencer.setSequence(sequence);
        sequencer.setLoopCount(loopCount);
        sequencer.start();
    }

    public MidiEvent tempoEvent(long tempo){
        long mpqn = (long)(6e+7 / tempo / sixteenthNotesPerMeasure);

        MetaMessage metaMessage = new MetaMessage();

        // create the tempo byte array
        byte[] array = new byte[] { 0, 0, 0 };

        for (int i = 0; i < 3; i++) {
            int shift = (3 - 1 - i) * 8;
            array[i] = (byte) (mpqn >> shift);
        }

        // now set the message
        try {
            metaMessage.setMessage(81, array, 3);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return new MidiEvent(metaMessage, 0);
    }

    public void save(String fileLocation) throws InvalidMidiDataException, MidiUnavailableException, IOException {
        File file = new File(fileLocation);
        if(!file.exists()){
            file.getParentFile().mkdirs();
        }
        Sequencer sequencer = MidiSystem.getSequencer();
        sequencer.open();
        Sequence sequence = new Sequence(Sequence.PPQ, 1);
        Synthesizer synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();
        sequencer.setSequence(sequence);

        sequence.createTrack().add(tempoEvent((long)BPM));
        for(int index = 0; index < this.size(); index++){
            NoteCollection collection = this.get(index);
            collection.createTrack(sequence);
        }
        sequencer.setSequence(sequence);
        MidiSystem.write(sequence,1,file);
        synthesizer.close();
        sequencer.close();
    }

}
