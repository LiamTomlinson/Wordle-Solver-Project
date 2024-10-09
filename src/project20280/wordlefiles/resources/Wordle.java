package project20280.wordlefiles.resources;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import com.sun.net.httpserver.Filter;
import project20280.hashtable.ChainHashMap;
import project20280.interfaces.Entry;


public class Wordle {

    String fileName = "src/project20280/wordlefiles/resources/dictionary.txt";
    //String fileName = "wordle/resources/extended-dictionary.txt";
    List<String> dictionary = null;
    final int num_guesses = 5;
    final long seed = 42;
    //Random rand = new Random(seed);
    Random rand = new Random();

    static final String winMessage = "CONGRATULATIONS! YOU WON! :)";
    static final String lostMessage = "YOU LOST :( THE WORD CHOSEN BY THE GAME IS: ";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_GREY_BACKGROUND = "\u001B[100m";


    Wordle() {

        this.dictionary = readDictionary(fileName);

        System.out.println("dict length: " + this.dictionary.size());
        //System.out.println("dict: " + dictionary);
        this.dictionaryMap = new ChainHashMap<>();
        this.frequencyMap = new ChainHashMap<>();
        countCharacterFrequencies(dictionary);
    }

    public static void main(String[] args) throws IOException {

        String fileName = "C:/data-structures-LiamTomlinson/src/project20280/wordlefiles/resources/dictionary.txt";




        int winCount = 0;
        for (int i = 0; i < 1000; i++) {
            Wordle game = new Wordle();
            String target = game.getRandomTargetWord();
            //String target = "cliff";
            //System.out.println("target: " + target);


            game.populateDictionaryMap(fileName);
            //game.printDictionaryMap();
            if (game.play(target)){
                winCount++;
            }
        }

        System.out.println(winCount);
    }

    public boolean play(String target) {
        System.out.println(wordGuess());
        //printFrequencies(frequencyMap);
        for(int i = 0; i <= num_guesses; ++i) {
            //String guess = getGuess();
            String guess = wordGuess();
            if(guess == target) { // you won!
                win(target);
                return true;
            }

            char[] targetchar = target.toCharArray();
            char[] guesschar = guess.toCharArray();





            // the hint is a string where green="+", yellow="o", grey="_"

            String [] hint = {"_", "_", "_", "_", "_"};
            int[] guessTaken = {0,0,0,0,0};
            int [][] targetTaken;

            targetTaken = new int[5][2];

            for(int j = 0; j < 5; j++){
                for (int k = 0; k < 2; k++ ){
                    targetTaken[j][k] = 0;
                }
            }

            guesschar = guess.toCharArray();
            for (int k = 0; k < 5; k++){
                if (guesschar[k] == targetchar[k]){
                    hint[k] = "+";
                    targetTaken[k][0] = 1;
                    guessTaken[k] = 1;
                }
            }

            for (int k = 0; k < 5; k++) {
                for(int j = 0; j < 5; j++){

                    if ( guesschar[k] == targetchar[j]){
                        if(targetTaken[j][0] == 0 && guessTaken[k] == 0) {
                            hint[k] = "o";
                            targetTaken[j][0] = 1;
                            guessTaken[k] = 1;
                        }
                    }
                }
            }

            cleanDictionaryMap(guesschar, targetchar, hint);
            //printDictionaryMap();
            System.out.println("dict length: " + this.dictionary.size());
            System.out.println(wordGuess());




            // after setting the yellow and green positions, the remaining hint positions must be "not present" or "_"
            System.out.println("hint: " + Arrays.toString(hint));


            // check for a win
            int num_green = 0;
            for(int k = 0; k < 5; ++k) {
                if(hint[k] == "+") num_green += 1;
            }
            if(num_green == 5) {
                win(target);
                return true;
            }


        }

        lost(target);
        return false;
    }

    public void lost(String target) {
        System.out.println();
        System.out.println(lostMessage + target.toUpperCase() + ".");
        System.out.println();

    }
    public void win(String target) {
        System.out.println(ANSI_GREEN_BACKGROUND + target.toUpperCase() + ANSI_RESET);
        System.out.println();
        System.out.println(winMessage);
        System.out.println();
    }

    public String getGuess() {
        Scanner myScanner = new Scanner(System.in, StandardCharsets.UTF_8.displayName());  // Create a Scanner object
        System.out.println("Guess:");

        String userWord = myScanner.nextLine();  // Read user input
        userWord = userWord.toLowerCase(); // covert to lowercase

        // check the length of the word and if it exists
        while ((userWord.length() != 5) || !(dictionary.contains(userWord))) {
            if ((userWord.length() != 5)) {
                System.out.println("The word " + userWord + " does not have 5 letters.");
            } else {
                System.out.println("The word " + userWord + " is not in the word list.");
            }
            // Ask for a new word
            System.out.println("Please enter a new 5-letter word.");
            userWord = myScanner.nextLine();
        }

        return userWord;
    }

    public void countCharacterFrequencies(List<String> dictionary) {
        for (String word : dictionary) {
            for (char c : word.toCharArray()) {
                frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
            }
        }
    }


    public static void printFrequencies(ChainHashMap<Character, Integer> frequencies) {
        System.out.println("Character frequencies:");
        for (Entry<Character, Integer> entry : frequencies.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public String getRandomTargetWord() {
        // generate random values from 0 to dictionary size
        return dictionary.get(rand.nextInt(dictionary.size()));
    }
    private List<String> readDictionary(String fileName) {
        List<String> wordList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String strLine;

            // Read file line by line
            while ((strLine = reader.readLine()) != null) {
                wordList.add(strLine.toLowerCase());
            }
        } catch (IOException e) {
            System.err.println("Error reading dictionary file: " + e.getMessage());
        }

        return wordList;
    }

    ChainHashMap<String, Integer> dictionaryMap;
    ChainHashMap<Character, Integer> frequencyMap;

    public int findFrequency(String word) {
        int sum = 0;

        // Iterate over each character in the word
        for (char c : word.toCharArray()) {
            // Get the frequency of the current character from the frequencyMap
            Integer characterFrequency = frequencyMap.get(c);

            // If the character exists in the frequencyMap, add its frequency to the sum

                sum += characterFrequency;

        }

        return sum;
    }

    private void populateDictionaryMap(String fileName) throws IOException {
        for (String word : dictionary) {
            int count = findFrequency(word);
            dictionaryMap.put(word, count);
        }
    }

    private void cleanDictionaryMap(char[] guess, char[] target, String[] hint ) {
        Set<Character> guessSet = new HashSet<>();
        Set<Character> targetSet = new HashSet<>();


        char[] guess2 = guess.clone();

        int[] check = {0,0,0,0,0};

        for (int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                if (Objects.equals(guess[i], target[j])){
                   guess[i] = '.';
                }
            }
        }


        // Iterate over dictionaryMap entries
        Iterator<Entry<String, Integer>> iterator = dictionaryMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, Integer> entry = iterator.next();
            String word = entry.getKey();

            boolean containsExtraLetters = false;
            for(int i = 0; i < 5; i ++){
                for(int j = 0; j < 5; j++) {

                    if (guess[i] == word.charAt(j)) {
                        containsExtraLetters = true;
                        break;
                    }
                }
            }



            for(int i = 0; i < 5; i ++){

                    if (Objects.equals(hint[i], "+")){
                        if (guess2[i] != word.charAt(i)) {
                            containsExtraLetters = true;
                            break;
                        }
                    }

                if (Objects.equals(hint[i], "o")){
                    if (guess2[i] == word.charAt(i)) {
                        containsExtraLetters = true;
                        break;
                    }
                }

                int count = 0;
                for(int j = 0; j < 5; j++) {
                    if (Objects.equals(hint[i], "_") && Objects.equals(hint[j], "+")){
                        if (guess2[i] == word.charAt(i)) {
                            containsExtraLetters = true;
                            break;
                        }
                    }

                    if (Objects.equals(hint[i], "o")){

                        if (guess2[i] != word.charAt(j)) {
                            count++;

                        }
                    }
                    if (count == 5){
                        containsExtraLetters = true;
                    }
                }


            }

            String guess3 = new String(guess2);

            // Remove word if it contains extra letters or wrong positions
            if (containsExtraLetters ) {
                //System.out.println(word);
                dictionaryMap.remove(entry.getKey());
                dictionary.remove(word);
            }
            dictionaryMap.remove(guess3);
            dictionary.remove(guess3);
        }
    }

    private String wordGuess(){
        int max = 0;
        String word = "";

        for (Entry<String, Integer> entry : dictionaryMap.entrySet()) {
           if (entry.getValue() > max) {
               max = entry.getValue();
               word = entry.getKey();
           }
        }

        return word;
    }

    private void printDictionaryMap() {

        System.out.println("Dictionary Map:");
        for (Entry<String, Integer> entry : dictionaryMap.entrySet()) {
                System.out.println(entry.getKey());
                System.out.println(entry.getValue());
        }
    }


}