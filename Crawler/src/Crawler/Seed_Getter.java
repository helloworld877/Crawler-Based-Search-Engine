package Crawler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;
import java.util.Scanner;

public class Seed_Getter {



    public Seed_Getter() {

    }

    public ArrayList<String> Get_Seeds( String PATH,int mode) {
        File backup = new File(PATH);
        ArrayList<String> Seeds = new ArrayList<String>();
        if (backup.exists() && backup.length()!=0) {
            //program was interrupted before ,and we need to recover the backup file
            System.out.println("Backup found");
            Scanner sc = null;
            try {

                sc = new Scanner(backup);
                String line;

                while (sc.hasNextLine()) {
                    line = sc.nextLine();
                    Seeds.add(line);


                }
                return Seeds;
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }


        }
        if(mode==1)
        {
            System.out.println("no backup file so no previously visited sites");
            Seeds.add(String.valueOf(0));
            return Seeds;
        }
        //program is running for the first time,or we deleted the backup file
        System.out.println("no backup file getting original seeds");
        File Seed_file = new File("Seeds.txt");
        Scanner sc = null;
        try {

            sc = new Scanner(Seed_file);
            String line;
            Seeds.add(String.valueOf(0));
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                Seeds.add(line);


            }
            return Seeds;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void Set_Seeds(String backup_Path, Queue<String> url_queue,int processed_links) {
        try {
            FileWriter f = new FileWriter(backup_Path);
            ArrayList<String> backup_data = new ArrayList<>();
            backup_data.add(String.valueOf(processed_links));
            while (!url_queue.isEmpty()) {
                backup_data.add(url_queue.peek());
                url_queue.remove();
            }


            for (String link: backup_data) {
                f.write(link+"\n");

            }

            f.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


}
