package com.resnik.util.audio.music;

import javax.sound.midi.*;
import java.util.Objects;

public class Note implements Cloneable{

    public final long startTime, duration;
    public int instrument;
    public int pitch, volume;
    public int channel;

    public Note(int instrument, int channel, int pitch, int volume, long startTime, long duration) {
        this.instrument = instrument;
        this.channel = channel;
        this.pitch = pitch;
        this.volume = volume;
        this.startTime = startTime;
        this.duration = duration;
    }

    public final ShortMessage startMessage() throws InvalidMidiDataException {
        return new ShortMessage(ShortMessage.NOTE_ON, channel, pitch, volume);
    }

    public MidiEvent startEvent() throws InvalidMidiDataException {
        return new MidiEvent(this.startMessage(), this.startTime);
    }

    public final ShortMessage endMessage() throws InvalidMidiDataException {
        return new ShortMessage(ShortMessage.NOTE_OFF, channel, pitch, volume);
    }

    public MidiEvent endEvent() throws InvalidMidiDataException {
        return new MidiEvent(this.endMessage(), this.startTime + this.duration);
    }

    public long nextStart(){
        return startTime + duration;
    }

    @Override
    public Note clone(){
        return new Note(instrument, channel, pitch, volume, startTime, duration);
    }

    public Note clone(final long nextStart){
        return new Note(instrument, channel, pitch, volume, nextStart, duration);
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
