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

        //shutdown script
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
               System.out.println("exit hook");
            }
        }, "Shutdown-thread"));


        String seed = "https://en.wikipedia.org/wiki/Main_Page";

//        links Queue
        Queue<String> url_queue = new LinkedList<>();
        url_queue.add(seed);



        while (!url_queue.isEmpty()) {

            try {
                FileWriter f = new FileWriter("C:\\Users\\Mark\\Desktop\\APT Project\\APT-Project\\APT-Project\\src\\Crawler\\processed_links.txt",true);
                String url = url_queue.peek();
                url_queue.remove();
                Connection.Response res = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
                        .timeout(10000)
                        .ignoreHttpErrors(true)
                        .execute();

                System.out.println("URL: "+url+"Status code: "+ res.statusCode());

                if(res.statusCode()>=400)
                {
                    continue;
                }

                Document doc = res.parse();
                Elements tags = doc.select("a[href]");
                //          Title of the Document
                String Title = doc.title();
                //            all anchor tags in document
                ArrayList<String> links = new ArrayList<String>();
                //`         All Keywords in the document
                String Keywords = doc.body().text();
                for (org.jsoup.nodes.Element tag : tags) {
                    if (tag.attr("href").startsWith("http")) {
                        url_queue.add(tag.attr("href"));
                    }
                }

                f.write(url + "," + Title + "," + Keywords + "\n");
                f.flush();
                f.close();


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }




    }
}