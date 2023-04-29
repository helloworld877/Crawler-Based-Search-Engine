import com.mongodb.MongoException;
import com.mongodb.client.*;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;

import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertManyResult;
import org.bson.Document;

import javax.print.Doc;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DB_Interface {

//    DB connection String
    public static String Connection_String= System.getenv("CONNECTION_STRING");



    public static void main(String[] args) {


//        try to connect to database
        try (MongoClient mongoClient = MongoClients.create(Connection_String)) {
            System.out.println("connected");
            MongoDatabase database = mongoClient.getDatabase("Crawler");
            MongoCollection<Document> collection = database.getCollection("Data");

            File f= new File("processed_links.txt");
            Scanner sc= new Scanner(f,"UTF-8");


            List<Document> documentList=new ArrayList<>();
            while (sc.hasNextLine())
            {
                String l = sc.nextLine();
                String [] line = l.split(",");
                documentList.add(new Document()
                    .append("URL",line[0])
                    .append("TITLE",line[1])
                    .append("KEYWORDS",line[2]));
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
