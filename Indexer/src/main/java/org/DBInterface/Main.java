package org.DBInterface;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertManyResult;
import org.Indexer.linkData;
import org.bson.Document;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static String Connection_String= System.getenv("CONNECTION_STRING");
    public static void main(String args[]){

        try (MongoClient mongoClient = MongoClients.create(Connection_String)) {
            System.out.println("connected");
            MongoDatabase database = mongoClient.getDatabase("Crawler");
            MongoCollection<Document> collection = database.getCollection("Data");

            File f= new File("processed_links.txt");
            Scanner sc= new Scanner(f,"UTF-8");


            List<Document> documentList=new ArrayList<>();

            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader("processed_links.txt"));
                String line;

                while((line=br.readLine())!=null)
                {

                    String[] Data=line.split("~~~",-1);

                    documentList.add(new Document()
                            .append("URL",Data[0])
                            .append("TITLE",Data[1])
                            .append("KEYWORDS",Data[2])
                            .append("STEMMED_KEYWORDS",Data[3]));
//                System.out.println(Data[0]);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }




            try {
                InsertManyResult result = collection.insertMany(documentList);
                System.out.println("Inserted document ids: " + result.getInsertedIds());
            } catch (MongoException me) {
                System.err.println("Unable to insert due to an error: " + me);
            }

        }
        catch(Exception e)
        {
            throw  new RuntimeException(e);
        }

    }
}
