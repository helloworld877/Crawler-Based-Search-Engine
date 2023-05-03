package org.Indexer;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

//      read data from file and organize it
        ArrayList<linkData> crawlerData=new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("processed_links.txt"));
            String line;

            while((line=br.readLine())!=null)
            {

                String[] Data=line.split("~~~",-1);

            crawlerData.add(new linkData(Data[0],Data[1],Data[2],Data[3] ));
//                System.out.println(Data[0]);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ArrayList<String> uniqueKeywords = new keywordProcessor(crawlerData).getUniqueKeywords();

        System.out.println(uniqueKeywords.size());

        HashMap<String, ArrayList<urlOccurrence>> indexTable = new HashMap<>();


        for (String keyword : uniqueKeywords) {
            indexTable.put(keyword, new ArrayList<>());
        }

        for (linkData l : crawlerData)
        {
        Arrays.stream(l.getSTEMMED_KEYWORDS())
                .collect(Collectors.groupingBy(s -> s))
                .forEach((k, v) -> {
                    indexTable.get(k).add(new urlOccurrence(l.getURL(),v.size()));
                });


        }
        




    }
}