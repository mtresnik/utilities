package com.resnik.util.audio.music.scales;

import com.resnik.util.audio.music.Note;
import com.resnik.util.audio.music.Step;

public class ChromaticScale extends Scale {

    public ChromaticScale(Note startNote) {
        super(startNote);
    }

    public ChromaticScale(Note startNote, long startTime) {
        super(startNote, startTime);
    }

    public ChromaticScale(Note startNote, DIRECTION direction) {
        super(startNote, direction);
    }

    public ChromaticScale(Note startNote, long startTime, DIRECTION direction) {
        super(startNote, startTime, direction);
    }

    @Override
    public int[] getStepsUp() {
        return new int[]{Step.HALF, Step.HALF, Step.HALF, Step.HALF, Step.HALF, Step.HALF, Step.HALF, Step.HALF, Step.HALF, Step.HALF, Step.HALF};
    }

    @Override
    public Scale generate(Note startNote, long startTime, DIRECTION direction) {
        return new ChromaticScale(startNote, startTime, direction);
    }
}
