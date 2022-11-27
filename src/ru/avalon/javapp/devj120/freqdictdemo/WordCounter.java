package ru.avalon.javapp.devj120.freqdictdemo;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Character.UnicodeBlock.of;
import static java.lang.Character.UnicodeBlock.CYRILLIC;

public class WordCounter {

    private static final String BY_ALPH_REP = "report-by-alph.txt";
    private static final String BY_ALPH_REV_PER = "report-by-alph-rev.txt";
    private static final String BY_FREQ_REP = "report-by-freq.txt";
    private final Map<String, Integer> words = new HashMap<>();

    public void processFile(String fileName) throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String s;
            while ((s = br.readLine()) != null) {
                s = s.toLowerCase();
                int p = 0;
                while (p < s.length()) {
                    // пропускаем все некириллические символы
                    while (p < s.length() && of(s.charAt(p)) != CYRILLIC) {
                        p++;
                    }

                    int st = p;
                    //пропускаем все кириллические символы
                    while(p < s.length() && (of(s.charAt(p)) == CYRILLIC || s.charAt(p) == '-')) {
                        p++;
                    }

                    if(st < p) {
                        String w = s.substring(st, p);

                        /*if(words.containsKey(w)) {
                            words.put(w, words.get(w) + 1);
                        }
                        else {
                            words.put(w, 1);
                        } или что тоже самое*/
                        words.put(w, words.getOrDefault(w,0) + 1);
                    }
                }
            }
        }
    }

    public void saveReports() throws FileNotFoundException {
        Map.Entry<String, Integer>[] data = words.entrySet().toArray(new Map.Entry[0]);
        int wordsTotal = 0;
        for (Map.Entry<String, Integer> e : data) {
            wordsTotal += e.getValue();
        }

        //1
        Arrays.sort(data, (e1, e2) -> e1.getKey().compareTo(e2.getKey()));
        saveReport(BY_ALPH_REP, data, wordsTotal);
        //2
        Arrays.sort(data, (e1, e2) -> compareStringsByBackDict(e1.getKey(), e2.getKey()));
        saveReport(BY_ALPH_REV_PER, data, wordsTotal);
        //3
        Arrays.sort(data, (e1, e2) -> {
            int r = -e1.getValue().compareTo(e2.getValue());
            if (r != 0)
                return r;
            return e1.getKey().compareTo(e2.getKey());
        });
        saveReport(BY_FREQ_REP, data, wordsTotal);
    }

    private void saveReport(String fileName, Map.Entry<String, Integer>[] data, int wordsTotal) throws FileNotFoundException {
        try(PrintWriter pw = new PrintWriter(fileName)) {
            for (Map.Entry<String, Integer> e : data) {
                pw.println(e.getKey() + ", " + e.getValue() + ", " + ((float)e.getValue() / wordsTotal));
            }
        }
    }

    private static int compareStringsByBackDict(String s1, String s2) {
        int p1 = s1.length() - 1,
                p2 = s2.length() - 1;
        while (p1 >= 0 && p2 >= 0) {
            int r = Character.compare(s1.charAt(p1), s2.charAt(p2));
            if(r != 0) {
                return r;
            }
            p1--;
            p2--;
        }

       /* if (p1 == 0 && p2 == 0)
            return 0;
        if(p1 == 0)
            return -1;
        else
            return 1;
            или что тоже самое */
        return p1 - p2;
    }
}
