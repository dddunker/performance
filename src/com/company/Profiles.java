package com.company;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Profiles extends MongoProcessor {

    Map<Integer, String> profiles = new HashMap<>();

    @Override
    public void fillFromFile() throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(BASE_DIR + "/profiles"));
            System.out.println("Reading from file....");

            String s;
            while ((s = reader.readLine()) != null) {
                String[] str = s.split("\t");
                int clnId = Integer.parseInt(str[0]);
                String profile = str[1];
                profiles.put(clnId, profile);
            }
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("Невозможно прочитать файл :" + BASE_DIR + "/profiles", ex);
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
        DBCollection profilesCollection = db.getCollection("profiles");
        DBCollection clientsCollection = db.getCollection("clients");

        int count = 0;
        for (int cln : profiles.keySet()) {
            BasicDBObject findDoc = new BasicDBObject("client_id", String.valueOf(cln));
            if (clientsCollection.findOne(findDoc) != null) {
                BasicDBObject doc = new BasicDBObject();
                doc.put("client_id", cln);
                doc.put("group", profiles.get(cln));
                profilesCollection.insert(doc);
                System.out.println(count++);
            }
        }
        System.out.println("done!");
    }
}
