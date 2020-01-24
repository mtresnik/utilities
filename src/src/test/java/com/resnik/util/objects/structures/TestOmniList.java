package com.resnik.util.objects.structures;

import org.junit.Test;

public class TestOmniList {


    @Test
    public void testOmniList() {
        OmniList<Double> om = new OmniList();
        om.add("test", 0.0, 0.45, 99.0, 55.0);
        om.add("hey", 1.0, 5.0, 0.5, -1.0, 55.9);
        System.out.println(om);
        om.sortSub(Double::compare);
        System.out.println(om);
        om.sort(String.CASE_INSENSITIVE_ORDER);
        System.out.println(om);
        om.add("hey", null);
        System.out.println(om);
    }
}
