package org.Indexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class keywordProcessor {

    private ArrayList<String> uniqueKeywords;

    public keywordProcessor(ArrayList<linkData> Data){

        uniqueKeywords = new ArrayList<String>();

        HashSet<String> unique_set = new HashSet<String>();

        for (linkData l: Data) {
            unique_set.addAll(Arrays.asList(l.getSTEMMED_KEYWORDS()));

        }

        uniqueKeywords.addAll(unique_set);

    }

    public ArrayList<String> getUniqueKeywords() {
        return uniqueKeywords;
    }
}
