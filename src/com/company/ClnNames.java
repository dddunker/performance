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

public class ClnNames extends MongoProcessor {

    Map<Integer, String> clients = new HashMap<>();

    @Override
    public void fillFromFile() throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(BASE_DIR + "/clients_names.txt"));
            System.out.println("Reading from file....");

            String s;
            while ((s = reader.readLine()) != null) {
                String[] str = s.split(";");
                int clnId = Integer.parseInt(str[0]);
                String clnName = str[1];
                clients.put(clnId, clnName);
            }
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("Невозможно прочитать файл :" + BASE_DIR + "/clients_names.txt", ex);
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
        DBCollection collection = db.getCollection("cln_names");

        int count = 0;
        for (int cln : clients.keySet()) {
            BasicDBObject doc = new BasicDBObject();
            doc.put("client_id", cln);
            doc.put("client_name", clients.get(cln));
            collection.insert(doc);
            System.out.println(count++);
        }
        System.out.println("done!");
    }
}
