package com.APT.API;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.StringOperators;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.print.Doc;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class query {
    public static String Connection_String = System.getenv("CONNECTION_STRING");

    @GetMapping("/query")

    public ArrayList<Document> query(@RequestParam(name = "q") String q) {
//        to make JSON Object
        ArrayList<Document> result = new ArrayList<>();
//        query empty
        if (q.equals("")) {
            return result;
        }

        Pattern pattern = Pattern.compile("\"([^\"]*)\"");

        Matcher matcher = pattern.matcher(q);

//        Exact match
        if (matcher.find()) {
            String connectionString = Connection_String;
            String exact_query = matcher.group(1);
            try (var mongoClient = MongoClients.create(connectionString)) {
                // Select database and collection
                MongoDatabase database = mongoClient.getDatabase("Crawler");
                MongoCollection<Document> collection = database.getCollection("Data");

                // Find documents that contain multiple values for the same key

                String keywordPattern = ".*"+exact_query +".*";
                Document filter = new Document("$or", new ArrayList<Document>() {{
                    add(new Document("URL", new Document("$regex", keywordPattern)));
                    add(new Document("KEYWORDS", new Document("$regex", keywordPattern)));
                    add(new Document("TITLE", new Document("$regex", keywordPattern)));
                }});
                Document projection = new Document("TITLE", 1).append("URL", 1).append("KEYWORDS",1).append("_id", 0);
                FindIterable<Document> iterable = collection.find(filter).projection(projection);
                List<Document> documents = new ArrayList<>();
                iterable.into(documents);

                ArrayList<Document> final_result = new ArrayList<>();
                for (Document doc : documents) {
                    String [] temp_list=doc.get("KEYWORDS", String.class).split(" ");
                    ArrayList<String> url_Keywords =new ArrayList<>(Arrays.asList(temp_list) ) ;
                    String snippet;

                    String search = exact_query;
                    int index = url_Keywords.indexOf(search);

                    int start = Math.max(index - 15, 0);
                    int end = Math.min(index + 15, url_Keywords.size() - 1);
                    snippet = String.join(" ", Arrays.copyOfRange(url_Keywords.toArray(new String[0]), start, end + 1));

                    Document link_doc = new Document("URL", doc.get("URL", String.class))
                            .append("TITLE", doc.get("TITLE", String.class))
                            .append("SNIPPET", snippet);

                    final_result.add(link_doc);

                }



                return final_result;

            }

        }

        stringOperations op = new stringOperations();
//        process query
        String Processed_query = op.Operate(1, q);
        Processed_query = op.Operate(3, Processed_query);
        Processed_query = op.Operate(2, Processed_query);

//        query contains stop words only
        if (Processed_query.equals("")) {

            return result;
        }
        String[] Search_values = Processed_query.split(" ");
        ArrayList<List<Document>> Search_results = new ArrayList<>();

        // Connect to MongoDB
        String connectionString = Connection_String;
        try (var mongoClient = MongoClients.create(connectionString)) {
            // Select database and collection
            MongoDatabase database = mongoClient.getDatabase("Crawler");
            MongoCollection<Document> collection = database.getCollection("Index");

            // Find documents that contain multiple values for the same key
            String key = "STEMMED_KEYWORD";
            List<String> values = Arrays.asList(Search_values);
            var filter = Filters.in(key, values);

            try (MongoCursor<Document> cursor = collection.find(filter).iterator()) {
                while (cursor.hasNext()) {
                    Document document = cursor.next();
                    List<Document> urls = document.get("URLS", ArrayList.class);
                    if (urls != null && !urls.isEmpty()) {
                        Search_results.add(urls);
                    }
//                    System.out.println(document.toJson());
                }
            }
        }
        if (Search_results.isEmpty()) {
            return result;
        }

        HashMap<String, Integer> results_map_popularity = new HashMap<>();
        HashMap<String, Integer> results_map_occurences = new HashMap<>();
        HashSet<String> URL_Set = new HashSet<>();

        for (List<Document> l : Search_results) {
            for (Document item : l) {
                int score_occurences = item.get("occurrences", Integer.class) ;
                int score_popularity =  item.get("to", Integer.class);
                if (results_map_occurences.containsKey(item.get("urlString", String.class))) {
                    int updated_score_popularity = results_map_popularity.get(item.get("urlString", String.class)) + score_popularity;
                    int updated_score_occurances = results_map_occurences.get(item.get("urlString", String.class)) + score_occurences;
//                  added updated scores to hash map
                    results_map_occurences.put(item.get("urlString", String.class), updated_score_occurances);
                    results_map_popularity.put(item.get("urlString", String.class), updated_score_popularity);
                } else {
                    results_map_occurences.put(item.get("urlString", String.class), score_occurences);
                    results_map_popularity.put(item.get("urlString", String.class), score_popularity);

                }
                URL_Set.add(item.get("urlString", String.class));

            }

        }


//        all unique urls
        List<String> values = new ArrayList<>(URL_Set);

        HashMap<String, String> Titles_Map = new HashMap<>();
        HashMap<String, String> Stemmed_Keywords_Map = new HashMap<>();
        HashMap<String, String> Keywords_Map = new HashMap<>();
        try (var mongoClient = MongoClients.create(connectionString)) {
            // Select database and collection
            MongoDatabase database = mongoClient.getDatabase("Crawler");
            MongoCollection<Document> collection = database.getCollection("Data");

            // Find documents that contain multiple values for the same key
            String key = "URL";

            var filter = Filters.in(key, values);
            Document projection = new Document("TITLE", 1).append("URL", 1).append("KEYWORDS",1).append("STEMMED_KEYWORDS",1).append("_id", 0);
            FindIterable<Document> iterable = collection.find(filter).projection(projection);
            List<Document> documents = new ArrayList<>();
            iterable.into(documents);


            for (Document document : documents) {
                String Title = document.get("TITLE", String.class);
                String URL = document.get("URL", String.class);
                String STEMMED_KEYWORDS = document.get("STEMMED_KEYWORDS",String.class);
                String KEYWORDS = document.get("KEYWORDS",String.class);
                Titles_Map.put(URL, Title);
                Stemmed_Keywords_Map.put(URL,STEMMED_KEYWORDS);
                Keywords_Map.put(URL,KEYWORDS);


            }

        }

        ArrayList<Document> final_result = new ArrayList<>();

        for (String link : URL_Set) {
//            System.out.println("/////////////"+results_map_occurences.get(link)+"//////////////////////");
            double relevence_score=800*(results_map_occurences.get(link));

            for (String keyword : Processed_query.split(" "))
            {
                if(Stemmed_Keywords_Map.get(link).contains(keyword))
                {
                    relevence_score *=300;
                }
                if(Titles_Map.get(link).contains(keyword))
                {
                    relevence_score *=200;
                }


            }

            double popularity_score=200*(results_map_popularity.get(link));
            double total_score=relevence_score+popularity_score;


           String [] url_Keywords= Keywords_Map.get(link).split(" ");
           String snippet= " ";
            for (int i =0 ;i< url_Keywords.length;i++) {

                String keyword= url_Keywords[i];
                keyword = op.Operate(1, q);
                keyword = op.Operate(3, keyword);
                keyword = op.Operate(2, keyword);

                if (keyword.equals(" "))
                {
                    continue;
                }

                if(Processed_query.contains(keyword))
                {
                    int start = Math.max(i - 15, 0);
                    int end = Math.min(i + 15, url_Keywords.length - 1);
                     snippet = String.join(" ", Arrays.copyOfRange(url_Keywords, start, end + 1));
                     break;
                }


            }

            Document link_doc = new Document("URL", link)
                    .append("TITLE", Titles_Map.get(link))
                    .append("SNIPPET",snippet)
                    .append("SCORE", total_score);
            final_result.add(link_doc);
        }

        Comparator<Document> comparator = Comparator.comparingDouble(document -> document.getDouble("SCORE"));
        Collections.sort(final_result, comparator.reversed());

//        List<Document> firstTen = final_result.subList(0, 10);


        return final_result;


    }

}
