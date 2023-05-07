package com.APT.API;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
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

        if (matcher.find()) {
            String connectionString = Connection_String;
            String exact_query = matcher.group(1);
            try (var mongoClient = MongoClients.create(connectionString)) {
                // Select database and collection
                MongoDatabase database = mongoClient.getDatabase("Crawler");
                MongoCollection<Document> collection = database.getCollection("Data");

                // Find documents that contain multiple values for the same key

                String keywordPattern = ".*"+exact_query +".*";
                Document filter = new Document("KEYWORDS", new Document("$regex", keywordPattern));
                Document projection = new Document("TITLE", 1).append("URL", 1).append("KEYWORDS",1).append("_id", 0);
                FindIterable<Document> iterable = collection.find(filter).projection(projection);
                List<Document> documents = new ArrayList<>();
                iterable.into(documents);


                return (ArrayList<Document>) documents;

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

        HashMap<String, Integer> results_map = new HashMap<>();
        HashSet<String> URL_Set = new HashSet<>();

        for (List<Document> l : Search_results) {
            for (Document item : l) {
                int score = item.get("occurrences", Integer.class) + item.get("to", Integer.class);
                if (results_map.containsKey(item.get("urlString", String.class))) {
                    int updated_score = results_map.get(item.get("urlString", String.class)) + score;
                    results_map.put(item.get("urlString", String.class), updated_score);
                } else {
                    results_map.put(item.get("urlString", String.class), score);

                }
                URL_Set.add(item.get("urlString", String.class));

            }

        }
        List<Map.Entry<String, Integer>> list = new ArrayList<>(results_map.entrySet());
        list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        List<String> values = new ArrayList<>(URL_Set);

        HashMap<String, String> Titles_Map = new HashMap<>();
        HashMap<String, String> Keywords_Map = new HashMap<>();
        try (var mongoClient = MongoClients.create(connectionString)) {
            // Select database and collection
            MongoDatabase database = mongoClient.getDatabase("Crawler");
            MongoCollection<Document> collection = database.getCollection("Data");

            // Find documents that contain multiple values for the same key
            String key = "URL";

            var filter = Filters.in(key, values);
            Document projection = new Document("TITLE", 1).append("URL", 1).append("KEYWORDS",1).append("_id", 0);
            FindIterable<Document> iterable = collection.find(filter).projection(projection);
            List<Document> documents = new ArrayList<>();
            iterable.into(documents);


            for (Document document : documents) {
                String Title = document.get("TITLE", String.class);
                String URL = document.get("URL", String.class);
                Titles_Map.put(URL, Title);


            }

        }

        ArrayList<Document> final_result = new ArrayList<>();

        for (String link : URL_Set) {

            Document link_doc = new Document("URL", link)
                    .append("TITLE", Titles_Map.get(link))
                    .append("SCORE", results_map.get(link));
            final_result.add(link_doc);
        }

        Comparator<Document> comparator = Comparator.comparingInt(document -> document.getInteger("SCORE"));
        Collections.sort(final_result, comparator.reversed());

        List<Document> firstTen = final_result.subList(0, 10);


        return final_result;

//        HashMap<String, String> map = new HashMap<>();
//        map.put("key", "123");
//        map.put("foo", "bar");
//        map.put("aa", "bb");
//        map.put("id","1");
//        map.put("body", q);
//        result.add(map);
//        map = new HashMap<>();
//        map.put("key", "345");
//        map.put("foo", "bar");
//        map.put("aa", "bb");
//        map.put("id","2");
//        map.put("body", q);
//        result.add(map);


    }

}
