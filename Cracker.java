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
        } catch (ArrayIndexOutOfBoundsException noHash) {
            System.out.println("You have to provide a hash dude"); //Can't really do anything without a hash
            System.exit(0);
        }
        try {
            wordlistFile = new File(args[1]);
            genWordlist(wordlistFile);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("No wordlist loaded"); //No one actually cares, this just means no wordlistFile
        }
        if (wordlistFile == null) {
            System.out.println("No wordlist? I'm not going to fucking brute force this. Don't be dumb.");
            System.exit(0);
        } else {
            wordlistAttack(inputHash);
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
    public static void genWordlist(File wordlistFile) throws FileNotFoundException {
        Scanner readList = new Scanner(wordlistFile);
        while (readList.hasNext()) {
            wordlist.add(readList.next());
        }
    }
    public static void wordlistAttack(String hash) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        for (int i = 0; i < wordlist.size(); i++) {
            if (compareHashToWord(hash, wordlist.get(i))) {
                System.out.println("Match found: " + wordlist.get(i));
                System.exit(0);
            }
        }
    }
}
