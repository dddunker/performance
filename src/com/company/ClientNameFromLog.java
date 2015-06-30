package com.company;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientNameFromLog extends MongoProcessor {

    private List<String> namesList = new ArrayList<>();

    @Override
    public void fillFromFile() throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/home/binkovskiy/Рабочий стол/logs/client_name/TOTAL_NAMES"));
            System.out.println("Reading from file....");

            String s;
            while ((s = reader.readLine()) != null) {
                namesList.add(s);
            }
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("Невозможно прочитать файл :" +
                    "/home/binkovskiy/Рабочий стол/logs/client_name/TOTAL_NAMES", ex);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        System.out.println("End of reading!!!!");
    }

    @Override
    public void process() {
        DB db = getDB();
        DBCollection namesFromLogCollection = db.getCollection("names_from_log");

        System.out.println("start writing");

        for (String n : namesList){
            BasicDBObject doc = new BasicDBObject();
            doc.put("client_name", n);
            namesFromLogCollection.insert(doc);
        }
        System.out.println("done!");
    }
}
