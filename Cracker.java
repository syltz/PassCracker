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
        String[] wordlist = null;
        try {
            inputHash = args[0];
        } catch (ArrayIndexOutOfBoundsException noHash) {
            System.out.println("You have to provide a hash dude"); //Can't really do anything without a hash
            System.exit(0);
        }
        try {
            wordlistFile = new File(args[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("No wordlist? I'm not about to fucking brute force this shit. Don't be retarded."); //What the prinlnt says
        }
    }
    public static boolean compareHashToWord(String hash, String word) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] bytesOfWord = word.getBytes(StandardCharsets.UTF_8.name());
        md.update(bytesOfWord);
        byte[] digestBytes = md.digest();
        String genHash = DatatypeConverter.printHexBinary(digestBytes);
        return hash.equalsIgnoreCase(genHash);
    }
    public static String[] genWordlist(File wordlistFile) throws FileNotFoundException {
        Scanner readList = new Scanner(wordlistFile);
        String[] wordlist = new String[0];
        while (readList.hasNext()) {
            wordlist = Arrays.copyOf(wordlist, wordlist.length + 1);
            wordlist[wordlist.length-1] = readList.next();
        }
        return wordlist;
    }
    public static void wordlistAttack(String hash, String[] wordlist) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        for (int i = 0; i < wordlist.length; i++) {
            if (compareHashToWord(hash, wordlist[i])) {
                System.out.println("Match found: " + wordlist[i]);
                System.exit(0);
            }
        }
        for (int i = 0; i< wordlist.length; i++) {
        }
        System.out.println("No matches found");
    }
}
