package Crawler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;

public class link_Processor extends Thread {

    private Queue<String> url_queue;
    private FileWriter f;

    private Wrapper<Integer> total_processed_links;

    private int MAX_PAGES;

    public link_Processor(Queue<String> url_queue, FileWriter f, Wrapper<Integer> total_processed_links, int MAX_PAGES) {
        this.url_queue = url_queue;
        this.f = f;
        this.total_processed_links = total_processed_links;
        this.MAX_PAGES = MAX_PAGES;
    }

    public void run() {
        Boolean condition;
        synchronized (url_queue) {
            condition = !url_queue.isEmpty();
        }
        synchronized (total_processed_links) {
            condition = condition && (total_processed_links.get() < MAX_PAGES);
        }

        while (condition) {

            try {
//                output file for the processed links
//                the format of the file is : link,title, space separated Keywords


//              get url to process
                String url;
                synchronized (url_queue) {
                    url = url_queue.peek();
                    url_queue.remove();
                }
//                connect to url to get the HTML page
                Connection.Response res = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
                        .timeout(10000)
                        .ignoreHttpErrors(true)
                        .execute();



                synchronized (total_processed_links) {

//                  check if connection resulted in error or exceeded maximum
                    if (res.statusCode() >= 400 ) {
                        continue;
                    }
                    if( total_processed_links.get()+1>MAX_PAGES)
                    {
                        break;
                    }

                    total_processed_links.set(total_processed_links.get() + 1);
                }
                System.out.println("URL: " + url + "Status code: " + res.statusCode() + " link number: " + total_processed_links.get() + " From Thread: "+this.getId());

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
                Keywords.replaceAll("\n"," ");
                for (org.jsoup.nodes.Element tag : tags) {
//                    extracting links to add to the queue to be processed
                    if (tag.attr("href").startsWith("http")) {
                        url_queue.add(tag.attr("href"));
                    }
                }
//              add the result of processing the link to the text file
                synchronized (f) {
                    f.write(url + "," + Title + "," + Keywords + "\n");
                    f.flush();



                }
                synchronized (url_queue) {
                    condition = !url_queue.isEmpty();
                }
                synchronized (total_processed_links) {
                    condition = condition && (total_processed_links.get() < MAX_PAGES);
                }

            } catch (IOException e) {
                System.out.println(e);
            }
        }


    }

}

