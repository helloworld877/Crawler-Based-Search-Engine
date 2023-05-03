package Crawler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;



public class Main {
    public static void main(String[] args) {
//      Max Number of pages to crawl
        final int MAX_PAGES = 1000;

//      processed links file
        FileWriter f = null;
        FileWriter popularity_table;
//      total processed links
        final Wrapper<Integer> total_processed_links = new Wrapper<>(0);
//      get seeds for the program
        ArrayList<String> seeds = new Seed_Getter().Get_Seeds("Seeds.bak",0);
        total_processed_links.set(Integer.parseInt(seeds.get(0)));
        seeds = new ArrayList<>(seeds.subList(1, seeds.size()));

        long start = System.currentTimeMillis();





//        links Queue
        Queue<String> url_queue = new LinkedList<>(seeds);
        BlockingQueue<String> url_blocking_queue = new LinkedBlockingDeque<>(url_queue);




        ArrayList<String> visited = new Seed_Getter().Get_Seeds("visited.bak",1);
        visited = new ArrayList<>(visited.subList(1, visited.size()));
        ArrayList<String> finalVisited = visited;
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
                if (total_processed_links.get() < MAX_PAGES) {
                    Queue<String> queue = new LinkedList<>();
                    queue.addAll(finalVisited);

                    Queue<String> url_queue_bak = url_blocking_queue;


                    new Seed_Getter().Set_Seeds("Seeds.bak", url_queue_bak, total_processed_links.get());
                    new Seed_Getter().Set_Seeds("visited.bak", queue, 0);

                }


            }
        }, "Shutdown-thread"));

        Scanner sc = new Scanner(System.in);
        System.out.println("how many threads you want to run ?");
        int threads = sc.nextInt();


        try {
            f = new FileWriter("processed_links.txt", true);
            popularity_table=new FileWriter("popularity.txt",true);
            for (int i = 0; i < threads; i++) {
                link_Processor thread = new link_Processor(url_blocking_queue, f, total_processed_links, MAX_PAGES, visited,popularity_table);
                thread.start();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}