package com.company;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UaPayment extends MongoProcessor {

    Map<Integer, List<Document>> documents = new HashMap<>();

    private class Document {

        String date;
        int docsCount;

        public Document(String date, int docsCount) {
            this.date = date;
            this.docsCount = docsCount;
        }
    }

    @Override
    public void fillFromFile() throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(BASE_DIR + "/ua_payment/ua_payment"));
            System.out.println("Reading from file....");

            String s;
            while ((s = reader.readLine()) != null) {
                String[] str = s.split(",");
                int accId = Integer.parseInt(str[0]);

                Document doc = new Document(str[1], Integer.parseInt(str[2]));

                List<Document> operationList = documents.get(accId);
                if (operationList == null) {
                    operationList = new ArrayList<>();
                    documents.put(accId, operationList);
                }
                operationList.add(doc);
            }
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("Невозможно прочитать файл :" + BASE_DIR + "/ua_payment/ua_payment", ex);
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
        DBCollection collection = db.getCollection("ua_payment");

        int count = 0;
        for (int clnId : documents.keySet()) {
            BasicDBObject document = new BasicDBObject();
            document.put("clnId", clnId);
            List<BasicDBObject> docs = new ArrayList<>();
            document.put("docs", docs);

            for (Document doc : documents.get(clnId)) {
                BasicDBObject innerDoc = new BasicDBObject();
                innerDoc.put("date", doc.date);
                innerDoc.put("docsCount", doc.docsCount);
                docs.add(innerDoc);
            }
            BasicDBObject q = new BasicDBObject("clnId", clnId);
            BasicDBObject u = new BasicDBObject("$push", new BasicDBObject("docs", new BasicDBObject("$each", docs)));
            collection.update(q, u, true, false);

            count++;
            if (count % 1000 == 0)
                System.out.println("inserted! " + count);
        }
        System.out.println("done!");
    }
}