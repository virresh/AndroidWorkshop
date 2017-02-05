/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private int wordLength;
    private Random random = new Random();
    private ArrayList<String> dictionaryWords = new ArrayList<String>();
    private HashMap<String,ArrayList<String>> lettersToWords = new HashMap<String,ArrayList<String>>();
    private HashMap<Integer,ArrayList<String>> sizeToWords = new HashMap<Integer, ArrayList<String>>();
    private HashSet<String> wordSet = new HashSet<String>();
    private ArrayList<String> wordList = new ArrayList<String>();

    public AnagramDictionary(Reader reader) throws IOException {
        wordLength = DEFAULT_WORD_LENGTH;
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            String p = sortLetters(word);
            int len = word.length();
            if(lettersToWords.containsKey(p)){
                ArrayList<String> alist = lettersToWords.get(p);
                alist.add(word);
            }
            else{
                ArrayList<String> newlist = new ArrayList<String>();
                newlist.add(word);
                lettersToWords.put(p,newlist);
            }

            if(sizeToWords.containsKey(len)){
                ArrayList<String> al = sizeToWords.get(len);
                al.add(word);
            }
            else{
                ArrayList<String> nl = new ArrayList<>();
                nl.add(word);
                sizeToWords.put(len,nl);
            }

            wordSet.add(word);
            wordList.add(word);
        }
    }

    String sortLetters(String s){
        char[] x = s.toCharArray();
        Arrays.sort(x);
        String p = new String(x);
        return p;
    }

    public boolean isGoodWord(String word, String base) {
        if(wordSet.contains(word) && !word.contains(base)){
            return true;
        }
        return false;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String y = sortLetters(targetWord);
        ArrayList<String> l = lettersToWords.get(y);
        if(l!=null) {
            for (String x : l) {
                if (!x.equals(targetWord)) {
                    result.add(x);
                }
            }
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for (char x = 'a'; x<='z'; x+=1) {
            String s = x + word;
            List<String> p = getAnagrams(s);
            if(p!=null) {
                for (String o : p) {
                    if (isGoodWord(o, word)) {
                        result.add(o);
                    }
//                    Log.d("Word", o);
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        int x = 0;
        String w = "";
        do {
            ArrayList<String> wordsOfWordSize = sizeToWords.get(wordLength);
            int i = (random.nextInt(wordsOfWordSize.size())) % wordsOfWordSize.size();
            w = wordsOfWordSize.get(i);
            List<String> h = getAnagramsWithOneMoreLetter(w);
            x = h.size();
        }while(x<MIN_NUM_ANAGRAMS);
        if(wordLength < MAX_WORD_LENGTH){
            wordLength ++;
        }
        return w;
    }
}
