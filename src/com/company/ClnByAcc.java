package com.company;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ClnByAcc {

    public Map<String, List<String>> buffReader() throws IOException {

        Map<String, List<String>> clnIdByAccId = new HashMap<String, List<String>>();
        BufferedReader reader = null;
        String s = null;
        try {
            reader = new BufferedReader(new FileReader("/home/binkovskiy/Рабочий стол/LOAD_TESTING/to_aval/to_Aval_5/sorted/clnID_by_accID/total.f"));
            while ((s = reader.readLine()) != null) {
                String[] str = s.split("\t");
                String clnId = str[0];
                String accId = str[1];
                List<String> lines = clnIdByAccId.get(clnId);
                if (lines == null) {
                    lines = new ArrayList<String>();
                    clnIdByAccId.put(clnId, lines);
                }
                lines.add(accId);
            }
            reader.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }

        return clnIdByAccId;
//        for (String clnId : clnIdByAccId.keySet()) {
//            for (String line : clnIdByAccId.get(clnId)) {
//                buffer.append(clnId).append("\t").append(line).append("\n");
//            }
//        }
//        System.out.println(buffer.toString());
    }

    public void insertToMongo() {
        try {
            MongoClient mongoClient = new MongoClient( "localhost" , 27017);
            DB db = mongoClient.getDB("test");

            DBCollection collection = db.getCollection("cln_id_by_acc_id");
            System.out.println("Collection selected successfully");

            ClnByAcc r = new ClnByAcc();
            try {
                for (String clnId : r.buffReader().keySet()) {
                    BasicDBObject document = new BasicDBObject();
                    document.put("clnId", clnId);
                    for (String accId : r.buffReader().get(clnId)) {
                        List<String> accounts = (List<String>) document.get("accId");
                        if (accounts == null) {
                            accounts = new ArrayList<String>();
                            document.put("accId", accounts);
                        }
                        accounts.add(accId);
                    }
                    collection.insert(document);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("done!");
        } catch(Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

}
