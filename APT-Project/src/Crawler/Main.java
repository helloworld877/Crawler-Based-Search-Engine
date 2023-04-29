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

public class Main {
    public static void main(String[] args) {


//        get seeds for the program
        ArrayList<String> seeds = new Seed_Getter().Get_Seeds("Seeds.bak");
        String seed = seeds.get(0);

//        links Queue
        Queue<String> url_queue = new LinkedList<>();
        url_queue.add(seed);
        //shutdown script
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {

               new Seed_Getter().Set_Seeds("Seeds.bak",url_queue);
            }
        }, "Shutdown-thread"));



        while (!url_queue.isEmpty()) {

            try {
//                output file for the processed links
//                the format of the file is : link,title, space separated Keywords
                FileWriter f = new FileWriter("processed_links.txt",true);
//                get ur to process
                String url = url_queue.peek();
                url_queue.remove();
//                connect to url to get the HTML page
                Connection.Response res = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
                        .timeout(10000)
                        .ignoreHttpErrors(true)
                        .execute();

                System.out.println("URL: "+url+"Status code: "+ res.statusCode());

//                check if connection resulted in error
                if(res.statusCode()>=400)
                {
                    continue;
                }

//                parse the HTML
                Document doc = res.parse();
//                getting the anchor tags in the document
                Elements tags = doc.select("a[href]");
                //          Title of the Document
                String Title = doc.title();
                //            all anchor tags in document
                ArrayList<String> links = new ArrayList<String>();
                //`         All Keywords in the document
                String Keywords = doc.body().text();
                for (org.jsoup.nodes.Element tag : tags) {
//                    extracting links to add to the queue to be processed
                    if (tag.attr("href").startsWith("http")) {
                        url_queue.add(tag.attr("href"));
                    }
                }
//              add the the result of processing the link to the text file
                f.write(url + "," + Title + "," + Keywords + "\n");
                f.flush();
                f.close();


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }




    }
}