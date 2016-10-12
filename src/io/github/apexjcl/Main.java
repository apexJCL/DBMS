package io.github.apexjcl;

import io.github.apexjcl.entities.Database;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        //Database db = new Database("C:/Users/zero_/", "testdb/");
        File f = new File("C:/Users/zero_/testdb/");
        for (String s: f.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.matches(".*\\.tbl");
            }
        }))
            System.out.println(s);
    }
}
