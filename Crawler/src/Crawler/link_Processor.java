package Crawler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.internal.Normalizer;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class link_Processor extends Thread {

    private BlockingQueue<String> url_queue;
    private FileWriter f;

    private Wrapper<Integer> total_processed_links;

    private int MAX_PAGES;

    private ArrayList<String> visited;
    private FileWriter popularity_table;

    public link_Processor(BlockingQueue url_queue, FileWriter f, Wrapper<Integer> total_processed_links, int MAX_PAGES, ArrayList<String> visited, FileWriter popularity_table) {
        this.url_queue = url_queue;
        this.f = f;
        this.total_processed_links = total_processed_links;
        this.MAX_PAGES = MAX_PAGES;
        this.visited = visited;
        this.popularity_table=popularity_table;
    }

    public void run() {
        Boolean condition;

        synchronized (total_processed_links) {
            condition = (total_processed_links.get() < MAX_PAGES);
        }


        while (condition) {

            try {
//                output file for the processed links
//                the format of the file is : link,title, space separated Keywords


//              get url to process
                String url;


                url = url_queue.poll(10, TimeUnit.SECONDS);
                if (url == null) {
//                        queue is empty

                    break;
                }
//                    url_queue.remove();


//                connect to url to get the HTML page
                Connection.Response res = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
                        .timeout(3000)
                        .ignoreHttpErrors(true)
                        .execute();
                //              link already visited
                if (visited.contains(Normalizer.normalize(url))) {

                    continue;
                }
//              added current link to visited

                visited.add(Normalizer.normalize(url));


//                  check if connection resulted in error or exceeded maximum
                if (res.statusCode() >= 400) {


                    continue;
                }


//              parse the HTML
                Document doc = res.parse();
//                getting the anchor tags in the document
                Elements tags = doc.select("a[href]");
                //          Title of the Document
                String Title = doc.title();
                //            all anchor tags in document
//                ArrayList<String> links = new ArrayList<String>();
                //`         All Keywords in the document
                String Keywords = doc.body().text();


                Pattern latinPattern = Pattern.compile("\\p{InArabic}");
                Matcher matcher = latinPattern.matcher(Keywords);
                if (matcher.find()) {

//                    string contains arabic
                    continue;
                }


                synchronized (total_processed_links) {

                    if (total_processed_links.get() + 1 > MAX_PAGES) {
                        break;
                    }

                    total_processed_links.set(total_processed_links.get() + 1);
                }


                Keywords=Keywords.replaceAll("\\n+", " ");
                Keywords= Keywords.replaceAll("\\s+", " ");
               synchronized (popularity_table)
               {

                    for (org.jsoup.nodes.Element tag : tags) {
    //                    extracting links to add to the queue to be processed
                        if (tag.attr("href").startsWith("http")) {
                            url_queue.add(tag.attr("href"));


                            popularity_table.write(url+"~~~"+Normalizer.normalize(tag.attr("href"))+"\n" );
                            popularity_table.flush();

                        }
                    }
               }
                System.out.println("URL: " + url + "Status code: " + res.statusCode() + " link number: " + total_processed_links.get() + " From Thread: " + this.getId());


//                stringOperations
                stringOperations op = new stringOperations();
//                Normalize Keywords
                String cleanedKeywords = op.Operate(1, Keywords);
//                Remove stop words Keywords
                cleanedKeywords = op.Operate(3, cleanedKeywords);
//                Stemming Keywords
                cleanedKeywords = op.Operate(2, cleanedKeywords);

//                Normalize Title
                Title = op.Operate(1, Title);

//                Normalize URL
                url = op.Operate(1, url);


//              add the result of processing the link to the text file
                synchronized (f) {
                    f.write(url + "~~~" + Title + "~~~" + Keywords + "~~~" + cleanedKeywords + "\n");
                    f.flush();


                }
                synchronized (total_processed_links) {
                    condition = condition && (total_processed_links.get() < MAX_PAGES);
                }

            } catch (IOException e) {
                System.out.println(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    

    }

}

