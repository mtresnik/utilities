package com.resnik.util.text;

import com.resnik.util.objects.structures.CountList;
import org.junit.Test;

import java.util.Arrays;

import static com.resnik.util.text.Similarity.*;
import static com.resnik.util.text.WordFrequency.*;

public class TestText {

    @Test
    public void testSimilarity(){
        int[][] numVals = lcsNumArray("hello", "how are you");
        for(int[] row : numVals){
            System.out.println(Arrays.toString(row));
        }
        System.out.println(lcs("hello", "how are you"));
    }

    @Test
    public void testFrequency(){
        CountList<String> countList = getCountListFromList(
                "hello world my name is mike", "i", "like", "peaches", "do you like peaches?",
                "my", "name", "is", "not mike"
        );
        System.out.println(countList);
    }

}
