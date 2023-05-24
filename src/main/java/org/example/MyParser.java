package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

public class MyParser {
    public MyScanner myScanner = new MyScanner();
    private HashSet<Integer> wordsSet = new HashSet<>();

    private List<String> words = new ArrayList<>();

    private List<HashSet<Integer>> sets = new ArrayList<>();

    private int maxDistance = Integer.MIN_VALUE;

    public int getDistance(String a, String b) {
        int distance = Math.abs(a.length()-b.length());
        for (int i = 0; i < Math.min(a.length(),b.length()); i++) {
            if (a.charAt(i) != b.charAt(i)) {
                distance++;
            }
        }
        return distance;
    }

    private void checkNext(int i){
        HashSet<Integer> tempSet = new HashSet<>();
        for (int wordID: wordsSet) {
            if (getDistance(words.get(wordID),words.get(i)) < maxDistance){
                tempSet.add(wordID);
                wordsSet.remove(wordID);
            }
        }
        if (i+1 < words.size()) {
            wordsSet.add(i);
            if (!tempSet.isEmpty()){
                checkNext(i+1);
                wordsSet.remove(i);
                for (int wordID: tempSet) {
                    wordsSet.add(wordID);
                }
                tempSet.clear();
            }
            checkNext(i+1);
        }
        else {
            wordsSet.add(i);
            if (!tempSet.isEmpty()) {
                doAdd();
                for (int wordID : tempSet) {
                    wordsSet.add(wordID);
                }
                tempSet.clear();
                wordsSet.remove(i);
                doAdd();
            }
            else doAdd();
        }
        wordsSet.remove(i);
    }

    private void doAdd() {
        boolean addCheck = true;
        for (HashSet<Integer> set: sets) {
            if (set.containsAll(wordsSet)){
                addCheck = false;
            }
        }
        if (addCheck == true) sets.add((HashSet<Integer>) wordsSet.clone());
    }

    public String[] splitWords(String text) {

        if (text == null) return null;

        Pattern delimPattern = Pattern.compile("[\\s~!@#$%^&*()_+\\-=\\[\\]\\\\/\"№;:?|{}.,]+");
        String[] res = delimPattern.split(text);

        if (res.length == 0) res = null;
        return res;
    }

    public String parseException(IOException e){
        return "Oops";
    }

    public String process(Path path) throws IOException {
        try {
            return processThrows(path);
        }
        catch (IOException e){
            return parseException(e);
        }
    }

    public String processThrows(Path path) throws IOException {
            String text = myScanner.read(path);
            String[] split = splitWords(text);
            for (int i = 0; i < split.length; i++) {
                String word = split[i];
                if (word.length() > 30)
                    word = word.substring(0, 30);
                String finalWord = word;
                if (words.stream().filter(x -> x.equals(finalWord)).count() == 0)
                    words.add(word);
            }

            for (int i = 0; i < words.size(); i++) {
                for (int j = i + 1; j < words.size(); j++) {
                    String firstWord = words.get(i);
                    String secondWord = words.get(j);
                    int distance = getDistance(firstWord, secondWord);
                    if (distance > maxDistance)
                        maxDistance = distance;
                }
            }
            String ans = "";
            wordsSet.add(0);
            checkNext(1);
            for (HashSet<Integer> set : sets) {
                if (set.size() > 1) {
                    for (int wordID : set) {
                        ans += words.get(wordID) + " ";
                    }
                    ans += "\n";
                }
            }
            return ans;
    }
}
