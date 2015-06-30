package com.company;

import com.mongodb.DB;
import com.mongodb.MongoClient;

import java.io.IOException;
import java.net.UnknownHostException;

public abstract class MongoProcessor {

    protected static String BASE_DIR = "/home/binkovskiy/Рабочий стол/LOAD_TESTING/to_aval/to_Aval_5/sorted/";
    private static MongoClient mongoClient = null;

    protected static void connect() {
        try {
            mongoClient = new MongoClient("localhost" , 27017);
        } catch (UnknownHostException e) {
            throw new RuntimeException("Error to connect to Mongo", e);
        }
    }

    protected static DB getDB() {
        if (mongoClient == null) {
            throw new RuntimeException("Mongo is not initialized");
        }
        return mongoClient.getDB("test");
    }

    public abstract void fillFromFile() throws IOException;

    public abstract void process();

    public static void closeMongo() {
        if (mongoClient == null) {
            throw new RuntimeException("Mongo is not initialized");
        }
        mongoClient.close();
    }
}
