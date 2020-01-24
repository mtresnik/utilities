package com.resnik.util.audio.music.scales;

import com.resnik.util.audio.music.Note;
import com.resnik.util.audio.music.NoteCollection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Scale extends NoteCollection implements Cloneable{

    public Note startNote;
    public DIRECTION direction;

    public enum DIRECTION{
        UP, DOWN, BOTH;
    }

    public Scale(Note startNote){
        this(startNote, 0L);
    }

    public Scale(Note startNote, long startTime){
        this(startNote, startTime, DIRECTION.BOTH);
    }

    public Scale(Note startNote, DIRECTION direction) {
        this(startNote, 0L, direction);
    }

    public Scale(Note startNote, long startTime, DIRECTION direction) {
        this.startNote = startNote.clone(startTime);
        this.direction = direction;
        switch (direction){
            case UP:
                this.addAll(Arrays.asList(this.ascend()));
                break;
            case DOWN:
                this.addAll(Arrays.asList(this.descend()));
                break;
            case BOTH:
                this.addAll(Arrays.asList(this.ascend()));
                this.addAll(Arrays.asList(this.descend()));
                break;
        }
    }

    public static Note nextNote(Note prevNote, int step){
        Note next = prevNote.clone(prevNote.nextStart());
        next.pitch = prevNote.pitch + step;
        return next;
    }

    public abstract int[] getStepsUp();

    public int[] getStepsDown(){
        int[] stepsUp = this.getStepsUp();
        int[] retArray = new int[stepsUp.length];
        for(int i = 0; i < stepsUp.length; i++){
            retArray[i] = -1 * stepsUp[stepsUp.length - 1 - i];
        }
        return retArray;
    }

    public static Note[] followPath(Note _startNote, int[] steps){
        List<Note> noteList = new ArrayList<>();
        noteList.add(_startNote);
        Note currNote = _startNote;
        for(int i = 0; i < steps.length; i++){
            int step = steps[i];
            Note nextNote = nextNote(currNote, step);
            noteList.add(nextNote);
            currNote = nextNote;
        }
        return noteList.toArray(new Note[noteList.size()]);
    }

    public Note[] ascend() {
        return followPath(this.startNote, this.getStepsUp());
    }

    public Note[] descend() {
        return followPath(this.topNote().clone(this.startNote.startTime), this.getStepsDown());
    }

    public Note topNote(){
        Note[] ascended = this.ascend();
        return ascended[ascended.length - 1];
    }

    public abstract Scale generate(Note startNote, long startTime, DIRECTION direction);

    public Scale clone(){
        return this.generate(startNote.clone(), 0, direction);
    }



}
