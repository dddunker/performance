package com.company;

import com.mongodb.DB;
import com.mongodb.DBCollection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class NamesComparator extends MongoProcessor {

    boolean found = false;
    StringBuffer buffer = new StringBuffer();

    @Override
    public void fillFromFile() throws IOException {

    }

    @Override
    public void process() {
        DB db = getDB();
        DBCollection clientsCollection = db.getCollection("clients");
        DBCollection namesFromLogCollection = db.getCollection("names_from_log");

        List<String> clnList = (List<String>) clientsCollection.distinct("client_name");
        List<String> namesFromLogList = (List<String>) namesFromLogCollection.distinct("client_name");

        for (String clnNameFromLog : namesFromLogList) {
//            System.out.println(clnNameFromLog + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            for (String clnNameFromDB : clnList) {
                if (clnNameFromDB == null) {
                    clnNameFromDB = "null";
                }
//                System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" + clnNameFromDB.get(0));
                if (clnNameFromLog.equals(clnNameFromDB)) {
                    found = true;
                    break;
                }
            }
            if (found) {
//                System.out.println(clnNameFromLog + "        ---->    worked with DB");
//                buffer.append(clnNameFromLog).append("\t\t\t").append("---->    worked with DB").append("\n");
                found = false;
            } else {
                System.out.println(clnNameFromLog + "        ---->    didn't work with DB");
                buffer.append(clnNameFromLog).append("\n");
            }
        }

        File file = new File("/home/binkovskiy/Рабочий стол/oooooooooooooo.txt");
        try {
            System.out.println("Start writing");
            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter(file), 2048);
            bw.write(buffer.toString());

            bw.flush();
            bw.close();

            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}