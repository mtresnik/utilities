package com.resnik.util.audio.music;

import javax.sound.midi.*;
import java.util.Objects;

public class Note implements Cloneable{

    public final long startTime, duration;
    public int instrument;
    public int pitch, volume;

    public Note(int instrument, int pitch, int volume, long startTime, long duration) {
        this.startTime = startTime;
        this.duration = duration;
        this.instrument = instrument;
        this.pitch = pitch;
        this.volume = volume;
    }

    public final ShortMessage startMessage() throws InvalidMidiDataException {
        return new ShortMessage(ShortMessage.NOTE_ON, instrument, pitch, volume);
    }

    public MidiEvent startEvent() throws InvalidMidiDataException {
        return new MidiEvent(this.startMessage(), this.startTime);
    }

    public final ShortMessage endMessage() throws InvalidMidiDataException {
        return new ShortMessage(ShortMessage.NOTE_OFF, instrument, pitch, volume);
    }

    public MidiEvent endEvent() throws InvalidMidiDataException {
        return new MidiEvent(this.endMessage(), this.startTime + this.duration);
    }

    public void play(int loopCount) throws MidiUnavailableException, InvalidMidiDataException, InterruptedException {
        Sequencer sequencer = MidiSystem.getSequencer();
        sequencer.open();

        Sequence sequence = new Sequence(Sequence.PPQ, 10);
        Synthesizer synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();

        sequencer.setSequence(sequence);
        Track track = sequence.createTrack();
        sequencer.setTempoInBPM(60.0f);

        track.add(this.startEvent());
        track.add(this.endEvent());
        sequencer.setSequence(sequence);
        sequencer.setLoopCount(loopCount);
        sequencer.start();
        while (true) {
            if (!sequencer.isRunning()) {
                sequencer.close();
                System.exit(1);
            }
            Thread.sleep(1000);
        }

    }

    public long nextStart(){
        return startTime + duration;
    }

    @Override
    public Note clone(){
        return new Note(instrument, pitch, volume, startTime, duration);
    }

    public Note clone(final long nextStart){
        return new Note(instrument, pitch, volume, nextStart, duration);
    }

    @Override
    public String toString() {
        return "Note{" +
                "startTime=" + startTime +
                ", duration=" + duration +
                ", instrument=" + instrument +
                ", pitch=" + pitch +
                ", volume=" + volume +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return startTime == note.startTime &&
                duration == note.duration &&
                instrument == note.instrument &&
                pitch == note.pitch &&
                volume == note.volume;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, duration, instrument, pitch, volume);
    }
}
