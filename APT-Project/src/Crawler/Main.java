package Crawler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//      Max Number of pages to crawl
        final int MAX_PAGES = 1000;

//      processed links file
        FileWriter f= null;
//      total processed links
        final Wrapper<Integer> total_processed_links = new Wrapper<>(0);
//      get seeds for the program
        ArrayList<String> seeds = new Seed_Getter().Get_Seeds("Seeds.bak");
        total_processed_links.set(Integer.parseInt(seeds.get(0)) );
         seeds = new ArrayList<>( seeds.subList(1, seeds.size()));

        long start = System.currentTimeMillis();


//        links Queue
        Queue<String> url_queue = new LinkedList<>(seeds);

        //shutdown script
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                // some time passes
                long end = System.currentTimeMillis();
                long elapsedTime = end - start;
                long hours = (elapsedTime / (1000 * 60 * 60)) % 24;
                long minutes = (elapsedTime / (1000 * 60)) % 60;
                long seconds = (elapsedTime / 1000) % 60;

                System.out.println(hours + ":" + minutes + ":" + seconds);
                if(total_processed_links.get()<MAX_PAGES)
                {

                new Seed_Getter().Set_Seeds("Seeds.bak", url_queue, total_processed_links.get());
                }

            }
        }, "Shutdown-thread"));

        Scanner sc =new Scanner(System.in);
        System.out.println("how many threads you want to run ?");
        int threads=sc.nextInt();


        try {
            f = new FileWriter("processed_links.txt",true);
            for (int i = 0; i < threads; i++) {
                link_Processor thread = new link_Processor(url_queue, f, total_processed_links, MAX_PAGES);
                thread.start();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }







    }
}