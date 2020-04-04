package com.resnik.util.text;

import com.resnik.util.logger.Log;
import com.resnik.util.objects.structures.CountList;
import com.resnik.util.text.similarity.Similarity;
import org.junit.Test;

import java.util.Arrays;

import static com.resnik.util.text.similarity.Similarity.*;
import static com.resnik.util.text.similarity.WordFrequency.*;

public class TestText {

    public static final String TAG = TestText.class.getSimpleName();

    @Test
    public void testSimilarity(){
        int[][] numVals = lcsNumArray("hello", "how are you");
        for(int[] row : numVals){
            Log.v(TAG,Arrays.toString(row));
        }
        Log.v(TAG,lcs("hello", "how are you"));
    }

    @Test
    public void testFrequency(){
        CountList<String> countList = countListFromStrings(
                "hello world my name is mike", "i", "like", "peaches", "do you like peaches?",
                "my", "name", "is", "not mike"
        );
        Log.v(TAG,countList);
    }

    @Test
    public void testCosine(){
        Log.v(TAG,Similarity.cosine("Julie loves John more than Linda loves John", "Jane loves John more than Julie loves John?"));
    }

}
