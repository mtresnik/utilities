package com.resnik.util.audio.music.scales;

import com.resnik.util.audio.music.Note;
import com.resnik.util.audio.music.Step;

public class Minor extends Scale {

    public Minor(Note startNote) {
        super(startNote);
    }

    public Minor(Note startNote, long startTime) {
        super(startNote, startTime);
    }

    public Minor(Note startNote, DIRECTION direction) {
        super(startNote, direction);
    }

    public Minor(Note startNote, long startTime, DIRECTION direction) {
        super(startNote, startTime, direction);
    }

    @Override
    public int[] getStepsUp() {
        return new int[]{Step.WHOLE, Step.HALF, Step.WHOLE, Step.WHOLE, Step.HALF, Step.WHOLE, Step.WHOLE};
    }

    @Override
    public Scale generate(Note startNote, long startTime, DIRECTION direction) {
        return new Minor(startNote, startTime, direction);
    }

    public static class Pentatonic extends Minor {

        public Pentatonic(Note startNote) {
            super(startNote);
        }

        public Pentatonic(Note startNote, long startTime) {
            super(startNote, startTime);
        }

        public Pentatonic(Note startNote, DIRECTION direction) {
            super(startNote, direction);
        }

        public Pentatonic(Note startNote, long startTime, DIRECTION direction) {
            super(startNote, startTime, direction);
        }

        @Override
        public int[] getStepsUp() {
            return new int[]{Step.MINOR_THIRD, Step.WHOLE, Step.WHOLE, Step.MINOR_THIRD, Step.WHOLE};
        }

        @Override
        public Scale generate(Note startNote, long startTime, DIRECTION direction) {
            return new Pentatonic(startNote, startTime, direction);
        }
    }
}
