package com.resnik.util.math.binary;

import com.resnik.util.objects.arrays.ArrayUtils;

public class VeryLongWord extends BitCollection {

    public VeryLongWord(LongWord l1, LongWord l2){
        super(ArrayUtils.yoke(l1.bitRep(), l2.bitRep()));
    }

}
