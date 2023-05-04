package org.DBInterface_Index;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertManyResult;
import org.Indexer.urlOccurrence;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class DBInterface_Index {

    public static String Connection_String = System.getenv("CONNECTION_STRING");

    public DBInterface_Index(HashMap<String,ArrayList<urlOccurrence>> documentList)
    {


        MongoClient mongoClient = MongoClients.create(Connection_String);


        System.out.println("connected");
        MongoDatabase database = mongoClient.getDatabase("Crawler");
        MongoCollection<Document> collection = database.getCollection("Index");

        int count=0;
        for (Map.Entry<String, ArrayList<urlOccurrence>> entry : documentList.entrySet()) {
            // Create a new Document for each entry

            List<Document> URL_Data_list = new ArrayList<>();
            for (urlOccurrence url : entry.getValue()) {
                Document urlDoc = new Document();
                urlDoc.append("urlString", url.getUrlString());
                urlDoc.append("occurrences", url.getOccurrences());
                urlDoc.append("from",url.getFrom());
                urlDoc.append("to", url.getTo());
//                urlDoc.append("KEYWORDS",url.getKeywords());
                URL_Data_list.add(urlDoc);
            }

            Document document = new Document();
            document.append("STEMMED_KEYWORD", entry.getKey());
            document.append("URLS", URL_Data_list);


            // Insert the Document into the collection
            collection.insertOne(document);

            count++;
            System.out.println("inserted document : "+ count);
        }

    }


}
