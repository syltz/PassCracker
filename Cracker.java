/* 
 * TO DO LIST
 * Create a method to check entire lines (readLine.nexLine()) 
 * Redo the input method, use scanner from system in rather than args
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
        try {
            inputHash = args[0];
        } catch (ArrayIndexOutOfBoundsException noHashException) {
            System.out.println("You have to provide a hash dude"); //Can't really do anything without a hash
            System.out.println(noHashException);
            System.exit(0);
        }
        try {
            wordlistFile = new File(args[1]);
        } catch (ArrayIndexOutOfBoundsException noWordlistException) {
            System.out.println("Either you're not misspelling the file name or you're expecting me to brute force this shit."); //What the println says
            System.out.println(noWordlistException);
            System.exit(0);
        }
        wordlistAttack(inputHash, wordlistFile);
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
                words[i] = readLine.next();
            } catch (EOFException e) {
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
                matchFound(fullString);
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
    private static void matchFound(String match) {
        System.out.println("Match found: " + match);
        System.exit(0);
    }
    private static void noMatch() {
        System.out.println("No matches");
    }
}
