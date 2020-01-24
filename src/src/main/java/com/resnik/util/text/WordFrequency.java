package com.resnik.util.text;

import com.resnik.util.objects.structures.CountList;
import com.resnik.util.objects.structures.CountObject;

import java.util.ArrayList;
import java.util.List;

public class WordFrequency {

    public static CountList<String> getCountListFromList(kotlin.jvm.functions.Function2<CountObject<String>, Object, Boolean> subEquals, String ... allWords){
        CountList<String> retList = new CountList<>();
        if(subEquals != null){
        }
        if(allWords.length == 0){
            return retList;
        }
        List<String> allElements = new ArrayList<>();
        for(String currString : allWords){
            String[] splitString = currString.split(" ");
            for(String sub : splitString){
                allElements.add(sub);
            }
        }
        retList.addAllElements(allElements);
        return retList;
    }

    public static CountList<String> getCountListFromList(String ... allWords){
        return getCountListFromList(null, allWords);
    }

}
