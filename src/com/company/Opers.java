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

public class Opers extends MongoProcessor {

    Map<Integer, List<Operation>> opers = new HashMap<>();

    private class Operation {

        String date;
        int opersCount;
        String operType;

        public Operation(String date, int opersCount, String operType) {
            this.date = date;
            this.opersCount = opersCount;
            this.operType = operType;
        }
    }

    @Override
    public void fillFromFile() throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(BASE_DIR + "/opers/opers"));
            System.out.println("Reading from file....");

            String s;
            while ((s = reader.readLine()) != null) {
                String[] str = s.split(",");
                int accId = Integer.parseInt(str[0]);

                Operation oper = new Operation(str[1], Integer.parseInt(str[2]), str[3]);

                List<Operation> operationList = opers.get(accId);
                if (operationList == null) {
                    operationList = new ArrayList<>();
                    opers.put(accId, operationList);
                }
                operationList.add(oper);
            }
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("Невозможно прочитать файл :" + BASE_DIR + "/opers/opers", ex);
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
        DBCollection collection = db.getCollection("opers");

        int count = 0;
        for (int accId : opers.keySet()) {
            BasicDBObject document = new BasicDBObject();
            document.put("accId", accId);
            List<BasicDBObject> op = new ArrayList<>();
            document.put("opers", op);

            for (Operation operation : opers.get(accId)) {
                BasicDBObject innerDoc = new BasicDBObject();
                innerDoc.put("date", operation.date);
                innerDoc.put("opersCount", operation.opersCount);
                innerDoc.put("operType", operation.operType);
                op.add(innerDoc);
            }
            BasicDBObject q = new BasicDBObject("accId", accId);
            BasicDBObject u = new BasicDBObject("$push", new BasicDBObject("opers", new BasicDBObject("$each", op)));
            collection.update(q, u, true, false);

            count++;
            if (count % 1000 == 0)
                System.out.println("inserted! " + count);
        }
        closeMongo();
        System.out.println("done!");
    }

}
