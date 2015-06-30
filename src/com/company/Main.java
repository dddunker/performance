package com.company;

import java.io.IOException;

public class Main{
    public static void main(String args[]) throws IOException {
        try {
            try {
                MongoProcessor.connect();

//                Opers o = new Opers();
//                o.fillFromFile();
//                o.process();

//                PaySheet p = new PaySheet();
//                p.fillFromFile();
//                p.process();

//                UaPayment u = new UaPayment();
//                u.fillFromFile();
//                u.process();

//                FormatDate formatDate = new FormatDate();
//                formatDate.dateFormatter();

//                FormatDate fd = new FormatDate();
//                fd.dateFormatter();

//                ClnNames clnNames = new ClnNames();
//                clnNames.fillFromFile();
//                clnNames.process();

//                Employees e = new Employees();
//                e.fillFromFile();
//                e.process();

//                Recipients r = new Recipients();
//                r.fillFromFile();
//                r.process();

//                Staffs s = new Staffs();
//                s.fillFromFile();
//                s.process();

//                Profiles profiles = new Profiles();
//                profiles.fillFromFile();
//                profiles.process();

//                ProfileCategory pc = new ProfileCategory();
//                pc.fillFromFile();

//                ClientNameFromLog cnfl = new ClientNameFromLog();
//                cnfl.fillFromFile();
//                cnfl.process();

                NamesComparator nc = new NamesComparator();
                nc.process();

//                Clients c = new Clients();
//                c.fillFromFile();
//                c.process();
            } finally {
                MongoProcessor.closeMongo();
            }
        } catch (Exception e) {
            System.err.println("Ошибка " + e.getMessage());
        }
    }

}

