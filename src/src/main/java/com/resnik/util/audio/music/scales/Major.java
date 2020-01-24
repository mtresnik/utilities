package com.resnik.util.audio.music.scales;

import com.resnik.util.audio.music.Note;
import com.resnik.util.audio.music.Step;

public class Major extends Scale {

    public Major(Note startNote) {
        super(startNote);
    }

    public Major(Note startNote, long startTime) {
        super(startNote, startTime);
    }

    public Major(Note startNote, DIRECTION direction) {
        super(startNote, direction);
    }

    public Major(Note startNote, long startTime, DIRECTION direction) {
        super(startNote, startTime, direction);
    }

    @Override
    public int[] getStepsUp() {
        return new int[]{Step.WHOLE, Step.WHOLE, Step.HALF, Step.WHOLE, Step.WHOLE, Step.WHOLE, Step.HALF};
    }

    @Override
    public Scale generate(Note startNote, long startTime, DIRECTION direction) {
        return new Major(startNote, startTime, direction);
    }

    public static class Pentatonic extends Major {

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
            return new int[]{Step.WHOLE, Step.WHOLE, Step.MINOR_THIRD, Step.WHOLE, Step.MINOR_THIRD};
        }

        @Override
        public Scale generate(Note startNote, long startTime, DIRECTION direction) {
            return new Pentatonic(startNote, startTime, direction);
        }
    }
}
