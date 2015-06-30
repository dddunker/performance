package com.company;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileCategory extends MongoProcessor{

    public String[] low = {"aaa", "0aa", "a0a", "aa0", "00a", "0a0", "a00"};

    private String[] mid1 = {"aab", "aba", "abb", "baa", "bab", "bba", "bbb", "0ab", "0ba", "0bb", "a0b", "b0a", "b0b",
            "ab0", "ba0", "bb0", "00b", "0b0", "b00", "aac", "bac", "a0c", "b0c"};

    private String[] mid2 = {"bca", "bcb", "caa", "cab", "cac", "cba", "cbb", "cbc", "cca", "ccb", "c0a", "c0b", "c0c",
            "c00", "abd", "bad", "bbd", "cad", "cbd", "dab", "dba", "dbd", "a0d", "b0d", "c0d", "d0b", "d0c", "d0d",
            "00d", "aae", "bae", "cae"};

    private String[] high = {"ddb", "e0b", "e0d", "e0e", "caf", "dbf", "c0f", "d0f", "e0f", "00f"};

    public static Map<String, List<String>> categories = new HashMap<>();

    @Override
    public void fillFromFile() throws IOException {
        List<String> list = new ArrayList<>();
        for (String l : low) {
            list.add(l);
            categories.put("low", list);
        }
        list = new ArrayList<>();
        for (String m1 : mid1) {
            list.add(m1);
            categories.put("mid1", list);
        }
        list = new ArrayList<>();
        for (String m2 : mid2) {
            list.add(m2);
            categories.put("mid2", list);
        }
        list = new ArrayList<>();
        for (String h : high) {
            list.add(h);
            categories.put("high", list);
        }

//        for (String k : categories.keySet())
//            for (String v : categories.get(k))
//                System.out.println(k + " " + v);
    }

    @Override
    public void process() {

    }
}






