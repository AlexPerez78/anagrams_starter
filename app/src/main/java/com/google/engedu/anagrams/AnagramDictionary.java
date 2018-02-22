package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    int wordLength = DEFAULT_WORD_LENGTH - 1;
    private Random random = new Random();
    public HashSet<String> wordSet = new HashSet<>();
    public ArrayList<String> wordList = new ArrayList<>();
    public HashMap<String, ArrayList<String>> lettersToWord = new HashMap<>(); // Added <String> to array list
    public HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<>();

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();

            wordSet.add(word);

            ArrayList<String> sameWordLengths;
            if(!sizeToWords.containsKey(word.length())){
                sameWordLengths = new ArrayList<>();
                sameWordLengths.add(word);

                sizeToWords.put(word.length(),sameWordLengths);
            }
            else{
                sizeToWords.get(word.length()).add(word);
            }

            ArrayList<String> temp;
            if(!lettersToWord.containsKey(alphabeticalSorter(word))) {
                temp = new ArrayList<>();
                temp.add(word);

                lettersToWord.put(alphabeticalSorter(word), temp); //Adds the Sorted word with the new word in temp array
            }
            else{
                lettersToWord.get(alphabeticalSorter(word)).add(word);
            }
        }
        //System.out.println("Words in SizeToWords Hash: " + sizeToWords);
        wordList.addAll(wordSet);
    }

    //Gets a word and sorts the given word in alphabetical order.
    public String alphabeticalSorter(String word){
        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        String newWord = new String(chars);
        return newWord;
    }

    //Check if the Word is a good word to return or a bad word that we don't want
    public boolean isGoodWord(String userWord, String base) {
        boolean checker = false;
        //Checks if the userWord does not contain te substring of the Base word or if the userWord is not the base word itself
        if(userWord.toLowerCase().contains(base.toLowerCase()) || userWord.equalsIgnoreCase(base)){
            checker = false;
        }
        //If userWord does not contain the substring of the base word and the userWord is in the wordList then its a good word.
        else if(!userWord.toLowerCase().contains(base.toLowerCase()) && wordList.contains(userWord)){
            checker = true;
        }

        return checker;
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<>();

        for (int alphabet = 'a'; alphabet <= 'z'; alphabet++) {
            String tempWord = word + (char) alphabet;
            String newHashKey = alphabeticalSorter(tempWord);

            if(lettersToWord.containsKey(newHashKey)){
                ArrayList<String> tempList = lettersToWord.get(newHashKey);

                for(String element: tempList) {
                    if (isGoodWord(element, word)) {
                        result.add(element);
                    }
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord1() {
        String wordForGame = "";
        int startingPosition = (int) (Math.random() * wordList.size());
        for(int start = startingPosition; start <= wordList.size(); start++){
            String word = wordList.get(start);
           if(lettersToWord.get(alphabeticalSorter(word)).size() >= MIN_NUM_ANAGRAMS){
                wordForGame = word;
                //System.out.println("The Word Will Be: " + wordForGame);
                break;
            }
        }
        return wordForGame;
    }

    public String pickGoodStarterWord() {
        String wordForGame = "";
        int MaxWordLength = MAX_WORD_LENGTH;
        ArrayList<String> tempList = sizeToWords.get(wordLength); // Contains all the Letters with the given word Length

        //Base Case checking is the wordLength has exceeded the max word length.
        if(wordLength > MaxWordLength){
            wordLength = DEFAULT_WORD_LENGTH;
        }
        else{
            wordLength++; //Increment WordLength so that the users can have the game go from easy to hard ( 3 word_Length to 7 word_Length)
            System.out.println("WordLength at the moment: " + wordLength);
        }

        int startingPosition = (int) (Math.random() * sizeToWords.get(wordLength).size());
        for(int start = startingPosition; start <= sizeToWords.get(wordLength).size(); start++){
            String word = wordList.get(start);
            if(sizeToWords.get(wordLength).size() >= MIN_NUM_ANAGRAMS){
                wordForGame = word;
                break;
                //System.out.println("The Word Will Be: " + wordForGame);
            }
        }
        return wordForGame;
    }
}