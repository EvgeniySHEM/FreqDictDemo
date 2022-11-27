package ru.avalon.javapp.devj120.freqdictdemo;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java ru.avalon.javapp.devj120.freqdictdemo.Main <file1>...");
            return;
        }

        WordCounter cntr = new WordCounter();
        for(String arg : args) {
            try {
                cntr.processFile(arg);
            } catch (IOException e) {
                System.out.println("Error reading file " + arg + ":");
                System.out.println(e.getMessage());
                System.out.println("Finishing.");
                return;
            }
        }
        try {
            cntr.saveReports();
        } catch (FileNotFoundException e) {
            System.out.println("Error saving reports:");
            System.out.println(e.getMessage());
            System.out.println("Finishing.");
            return;
        }
        System.out.println("Done.");
    }
}
