/* 
 * TO DO LIST
 * Add a check for -h and --help flags and write some info
 */
import java.util.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.io.*;
import javax.xml.bind.DatatypeConverter;

public class Cracker  {
    private static ArrayList<String> wordlist = new ArrayList<String>();
    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException, FileNotFoundException {
        
        String inputHash = null;
        File wordlistFile = null;
        Scanner userSelection = new Scanner(System.in);
        String userChoice;
        System.out.print("Input hash: ");
        inputHash = userSelection.next();
        System.out.println();
        System.out.print("Supply a wordlist file: ");
        wordlistFile = new File(userSelection.next());
        System.out.println();
        while (true) {
            System.out.println("What kind of attack?");
            System.out.println("1) Read one line as a string");
            System.out.println("2) Read each individial word as a string");
            System.out.println("3) Read several words separated by spaces as a string");
            System.out.print("(1/2/3/q): ");
            userChoice = userSelection.next();
            System.out.println();
            if (userChoice.equals("1"))
                lineAttack(inputHash, wordlistFile);
            else if (userChoice.equals("2"))
                wordlistAttack(inputHash, wordlistFile);
            else if (userChoice.equals("3")) {
                System.out.print("How many words? ");
                int numOfWords = 0;
                try {
                    numOfWords = userSelection.nextInt();
                } catch (IllegalArgumentException e) {
                    System.out.println();
                    System.out.println("Only numbers idiot");
                }
                multiWordAttack(inputHash, wordlistFile, numOfWords);
            }
            else if (userChoice.equals("q")) 
                System.exit(0);
        }
    }
    public static boolean compareHashToWord(String hash, String word) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //What kind of hash function is it? At some point I'll make it possible to change the
        //algorithm
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] bytesOfWord = word.getBytes(StandardCharsets.UTF_8.name());
        md.update(bytesOfWord);
        byte[] digestBytes = md.digest();
        String genHash = DatatypeConverter.printHexBinary(digestBytes);
        return hash.equalsIgnoreCase(genHash);
    }
    public static void wordlistAttack(String hash, File wordlist) throws NoSuchAlgorithmException, UnsupportedEncodingException, FileNotFoundException  {
        System.out.println("Starting to check each individual word");
        Scanner readList = new Scanner(wordlist);
        while (readList.hasNext()) {
            String nextWord = readList.next();
            if (compareHashToWord(hash, nextWord)) {
                matchFound(nextWord);
            }
        }
        noMatch();
    }
    public static void multiWordAttack(String hash, File wordlist, int numOfWords) throws NoSuchAlgorithmException, UnsupportedEncodingException, FileNotFoundException {
        System.out.println("Checking strings of " + numOfWords + " words.");
        Scanner readList = new Scanner(wordlist);
        String[] words = new String[numOfWords];
        StringBuilder fullString; 
        //Create the initial array of words
        for (int i = 0; i < numOfWords; i++) {
            //If the number of words we want to check together are more than there are words in the
            //wordlist we'll just exit the method
            try {
                words[i] = readList.next();
            } catch (NoSuchElementException e) {
                return;
            }
        }
        //If the number of words we want to check is the same as the wordlength of the text file
        if (!readList.hasNext()) {
            //Create the entire string
            fullString = new StringBuilder(words[0]);
            for (int i = 1; i < words.length; i++) {
                //Apparently StringBuilder is more efficient than String concatenation, the more you know
                fullString.append(" ").append(words[i]);
            }
            if (compareHashToWord(hash, fullString.toString())) { //If it's a match call the matchFound
                matchFound(fullString.toString());
            } else { //If it's not a match we exit
                noMatch();
            }
        }
        //Right, this is the shit that matters right here.
        while (readList.hasNext()) {
            fullString = new StringBuilder(words[0]);
            for (int i = 1; i < words.length; i++) {
                fullString.append(" ").append(words[i]);
            }
            if (compareHashToWord(hash, fullString.toString())) {
                matchFound(fullString.toString());
            }
            for (int i = 0; i < words.length - 1; i++) {
                words[i] = words[i+1];
            }
            words[words.length-1] = readList.next();
        }
    }
    public static void lineAttack(String hash, File wordlist) throws NoSuchAlgorithmException, UnsupportedEncodingException, FileNotFoundException {
        System.out.println("Reading entire lines");
        Scanner readList = new Scanner(wordlist);
        String fullString;
        while (readList.hasNext()) {
            fullString = readList.nextLine();
            if(compareHashToWord(hash, fullString)) {
                matchFound(fullString);
            }
        }
        noMatch();
    }
    private static void matchFound(String match) {
        System.out.println("Match found: " + match);
        System.exit(0);
    }
    private static void noMatch() {
        System.out.println("No matches");
    }
}
