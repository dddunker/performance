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

public class Clients extends MongoProcessor {

    private List<Integer> clnIds = new ArrayList<>();


    @Override
    public void fillFromFile() throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(BASE_DIR + "all_clients_id"));
            System.out.println("Reading from file....");

            String s;
            while ((s = reader.readLine()) != null) {
                clnIds.add(Integer.valueOf(s));
            }
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("Невозможно прочитать файл :" + BASE_DIR + "all_clients_id", ex);
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
        DBCollection clientsCollection = db.getCollection("clients");
        DBCollection clnNamesCollection = db.getCollection("cln_names");
        DBCollection employeesCountCollection = db.getCollection("employees");
        DBCollection recipientsCountCollection = db.getCollection("recipients");
        DBCollection staffsCountCollection = db.getCollection("staffs");
        DBCollection profilesCollection = db.getCollection("profiles");

        DBCollection uaPaymentCollection = db.getCollection("t_ua_payment");
        DBCollection paySheetCollection = db.getCollection("t_pay_sheet");
        DBCollection opersCollection = db.getCollection("t_opers");
        DBCollection clnByAccCollection = db.getCollection("t_cln_id_by_acc_id");

        List<Integer> clnNamesIdList = clnNamesCollection.distinct("client_id");



//        List<String> uaPaymentClnIdList = uaPaymentCollection.distinct("clnId");
//        List<String> paySheetClnIdList = paySheetCollection.distinct("clnId");
//        List<String> opersAccIdList = opersCollection.distinct("accId");

        /*
            client_id
        */
//        for (int clnId : clnIds) {
//            BasicDBObject client = new BasicDBObject();
//            client.put("client_id", clnId);
//            clientsCollection.insert(client);
//        }

        List<Integer> clnList = clientsCollection.distinct("client_id");
        for (int clnId : clnList) {
            BasicDBObject q = new BasicDBObject("client_id", clnId);
            BasicDBObject u;

//            // client_name
//            List<String> names = clnNamesCollection.distinct("client_name",
//                    new BasicDBObject("client_id", clnId));
//            String name = null;
//            if (names.size() > 0) {
//                name = names.get(0);
//            }
//            u = new BasicDBObject("$set", new BasicDBObject("client_name", name));
//            clientsCollection.update(q, u, true, false);
//
//            // employees count
//            List<Integer> e_counts = employeesCountCollection.distinct("employees_count",
//                    new BasicDBObject("client_id", clnId));
//            int e_count = 0;
//              if (e_counts.size() > 0) {
//                e_count = e_counts.get(0);
//            }
//            u = new BasicDBObject("$set", new BasicDBObject("employees_count", e_count));
//            clientsCollection.update(q, u, true, false);

//            // recipients count
//            List<Integer> recipients = recipientsCountCollection.distinct("recipients_count",
//                    new BasicDBObject("client_id", clnId));
//            int recipient = 0;
//            if (recipients.size() > 0) {
//                recipient = recipients.get(0);
//            }
//            u = new BasicDBObject("$set", new BasicDBObject("recipients_count", recipient));
//            clientsCollection.update(q, u, true, false);

//            // staffs count
//            List<Integer> staffs = (List<Integer>) staffsCountCollection.distinct("staffs_count",
//                    new BasicDBObject("client_id", clnId));
//            int staff = 0;
//            if (staffs.size() > 0) {
//                staff = staffs.get(0);
//            }
//            u = new BasicDBObject("$set", new BasicDBObject("staffs_count", staff));
//            clientsCollection.update(q, u, true, false);

//            // profile
//            List<String> profiles = (List<String>) profilesCollection.distinct("group",
//                    new BasicDBObject("client_id", clnId));
//            String profile = null;
//            if (profiles.size() > 0) {
//                profile = profiles.get(0);
//            }
//            u = new BasicDBObject("$set", new BasicDBObject("profile", profile));
//            clientsCollection.update(q, u, true, false);

            // category
            List clnProfile = clientsCollection.distinct("profile", new BasicDBObject("client_id", clnId));
            for (String k : ProfileCategory.categories.keySet())
                for (String v : ProfileCategory.categories.get(k))
                    if (v.equals(clnProfile.get(0))) {
                        u = new BasicDBObject("$set", new BasicDBObject("category", k));
                        clientsCollection.update(q, u, true, false);
                        break;
                    } else if (clnProfile.get(0) == null) {
                        u = new BasicDBObject("$set", new BasicDBObject("category", null));
                        clientsCollection.update(q, u, true, false);
                    }
        }

//        for (String clnId : uaPaymentClnIdList) {
//            BasicDBObject q = new BasicDBObject("client_id", clnId);
//            BasicDBObject u = new BasicDBObject("$set", new BasicDBObject("ua_payment",
//                    uaPaymentCollection.distinct("docs", new BasicDBObject("clnId", clnId))));
//            clientsCollection.update(q, u, true, false);
//        }
//
//        for (String clnId : paySheetClnIdList) {
//            BasicDBObject q = new BasicDBObject("client_id", clnId);
//            BasicDBObject u = new BasicDBObject("$set", new BasicDBObject("pay_sheet",
//                    paySheetCollection.distinct("docs", new BasicDBObject("clnId", clnId))));
//            clientsCollection.update(q, u, true, false);
//        }
//
//        for (String accId : opersAccIdList) {
//            BasicDBObject q = new BasicDBObject("client_id", new BasicDBObject("$in",
//                    clnByAccCollection.distinct("clnId", new BasicDBObject("accId", accId))));
//            BasicDBObject acc = new BasicDBObject("accounts", new BasicDBObject("id", accId).append("opers",
//                    opersCollection.distinct("opers", new BasicDBObject("accId", accId))));
//            BasicDBObject u = new BasicDBObject("$push", acc);
//            clientsCollection.update(q, u, true, false);
//        }


//        DBCollection testCollection = db.getCollection("test");
//        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = null;
//        try {
//            date = formatter.parse("2012-02-14");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        testCollection.insert(new BasicDBObject("id", 123).append("type", "qwerty").append("date", date));

        System.out.println("done!");
    }
}
