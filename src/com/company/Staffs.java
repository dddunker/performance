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


public class Staffs extends MongoProcessor {

    Map<Integer, Integer> staffs = new HashMap<>();

    @Override
    public void fillFromFile() throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(BASE_DIR + "/staffs_count.txt"));
            System.out.println("Reading from file....");

            String s;
            while ((s = reader.readLine()) != null) {
                String[] str = s.split(",");
                int clnId = Integer.parseInt(str[0]);
                int staffCount = Integer.parseInt(str[1]);
                staffs.put(clnId, staffCount);
            }
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("Невозможно прочитать файл :" + BASE_DIR + "/staffs_count.txt", ex);
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
        DBCollection collection = db.getCollection("staffs");

        int count = 0;
        for (int cln : staffs.keySet()) {
            BasicDBObject doc = new BasicDBObject();
            doc.put("client_id", cln);
            doc.put("staffs_count", staffs.get(cln));
            collection.insert(doc);
            System.out.println(count++);
        }
        System.out.println("done!");
    }
}
