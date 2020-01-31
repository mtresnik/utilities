package com.resnik.util.audio.music;

public class Pitch {

    private static final IllegalArgumentException OCTAVE_OUT_OF_RANGE = new IllegalArgumentException("Octave out of range.");

    private static int C = 0;
    private static int C_SHARP = 1;
    private static int D = 2;
    private static int D_SHARP = 3;
    private static int E = 4;
    private static int F = 5;
    private static int F_SHARP = 6;
    private static int G = 7;
    private static int G_SHARP = 8;
    private static int A = 9;
    private static int A_SHARP = 10;
    private static int B = 11;

    public static final int HALF_STEP = 1;
    public static final int WHOLE_STEP = 2;


    public static int SHARP(int key){
        return key + 1;
    }

    public static int FLAT(int key){
        return key - 1;
    }

    public static int C(int octave){
        if(octave < -1 || octave > 9) throw OCTAVE_OUT_OF_RANGE;
        return C + (octave + 1)*12;
    }

    public static int C_SHARP(int octave){
        return SHARP(C(octave));
    }

    public static int D_FLAT(int octave){
        return C_SHARP(octave);
    }

    public static int D(int octave){
        if(octave < -1 || octave > 9) throw OCTAVE_OUT_OF_RANGE;
        return D + (octave + 1)*12;
    }

    public static int D_SHARP(int octave){
        return SHARP(D(octave));
    }

    public static int E_FLAT(int octave){
        return D_SHARP(octave);
    }

    public static int E(int octave){
        if(octave < -1 || octave > 9) throw OCTAVE_OUT_OF_RANGE;
        return E + (octave + 1)*12;
    }

    public static int E_SHARP(int octave){
        return F(octave);
    }

    public static int F_FLAT(int octave){
        return E(octave);
    }

    public static int F(int octave){
        if(octave < -1 || octave > 9) throw OCTAVE_OUT_OF_RANGE;
        return F + (octave + 1)*12;
    }

    public static int F_SHARP(int octave){
        return SHARP(F(octave));
    }

    public static int G_FLAT(int octave){
        return F_SHARP((octave));
    }

    public static int G(int octave){
        if(octave < -1 || octave >= 9) throw OCTAVE_OUT_OF_RANGE;
        return G + (octave + 1)*12;
    }

    public static int G_SHARP(int octave){
        return SHARP(G(octave));
    }

    public static int A_FLAT(int octave){
        return G_SHARP(octave);
    }

    public static int A(int octave){
        if(octave < -1 || octave >= 9) throw OCTAVE_OUT_OF_RANGE;
        return A + (octave + 1)*12;
    }

    public static int A_SHARP(int octave){
        return SHARP(A(octave));
    }

    public static int B_FLAT(int octave){
        return A_SHARP(octave);
    }

    public static int B(int octave){
        if(octave < -1 || octave >= 9) throw OCTAVE_OUT_OF_RANGE;
        return B + (octave + 1)*12;
    }

    public static int B_SHARP(int octave){
        if(octave < -1 || octave >= 9) throw OCTAVE_OUT_OF_RANGE;
        return SHARP(B(octave));
    }

    public static int C_FLAT(int octave){
        return B(octave);
    }

}
