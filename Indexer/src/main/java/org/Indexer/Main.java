package org.Indexer;

import org.DBInterface_Index.DBInterface_Index;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;


public class Main {
    public static void main(String[] args) {

//      read data from file and organize it
        ArrayList<linkData> crawlerData = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("processed_links.txt"));
            String line;

            while ((line = br.readLine()) != null) {

                String[] Data = line.split("~~~", -1);

                crawlerData.add(new linkData(Data[0], Data[1], Data[2], Data[3]));
//                System.out.println(Data[0]);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ArrayList<String> uniqueKeywords = new keywordProcessor(crawlerData).getUniqueKeywords();


        HashMap<String, Integer> from_link = new HashMap<String, Integer>();
        HashMap<String, Integer> to_link = new HashMap<String, Integer>();

//        int count=1;
        br = null;
        try {
            br = new BufferedReader(new FileReader("popularity.txt"));
            String line;

            while ((line = br.readLine()) != null) {

                String[] Data = line.split("~~~", -1);
                if (Data.length < 2) {
                    continue;
                }
                if (from_link.containsKey(Data[0])) {
                    from_link.put(Data[0], from_link.get(Data[0]) + 1);

                } else {
                    from_link.put(Data[0], 1);

                }

                if (to_link.containsKey(Data[1])) {
                    to_link.put(Data[1], to_link.get(Data[1]) + 1);

                } else {
                    to_link.put(Data[1], 1);

                }


//                System.out.println(count++);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("finished reading file");

//        System.out.println(uniqueKeywords.size());

        HashMap<String, ArrayList<urlOccurrence>> indexTable = new HashMap<>();


        for (String keyword : uniqueKeywords) {
            indexTable.put(keyword, new ArrayList<>());
        }

        for (linkData l : crawlerData) {
//            for each link we get the stemmed keywords then we group them by unique keywords and then add the number of occurrences
            Arrays.stream(l.getSTEMMED_KEYWORDS())
                    .collect(Collectors.groupingBy(s -> s))
                    .forEach((k, v) -> {
                        indexTable.get(k).add(new urlOccurrence(l.getURL(), v.size(),from_link.get(l.getURL()),to_link.get(l.getURL()),l.getKEYWORDS() ));
                    });


        }


        new DBInterface_Index(indexTable);




    }
}