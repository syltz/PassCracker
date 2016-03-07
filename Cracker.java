import java.util.*;
import java.security.*;
import java.io.*;

public class Cracker  {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        String inputHash = null;
        try {
            inputHash = args[0];
        } catch (ArrayIndexOutOfBoundsException noHash) {
            System.out.println("You have to provide a hash dude"); //Can't really do anything without a hash
            System.exit(0);
        }
        try {
            File wordlist = new File(args[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            File wordlist = null; //No one actually cares, this just means no wordlist
        }
        System.out.println(compareHashToWord(inputHash, "asd"));
        
    }
    public static boolean compareHashToWord(String hash, String word) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] bytesOfWord = word.getBytes();
        md.update(bytesOfWord);
        System.out.println("md digest: " + md.digest);
        return Arrays.equals(hash.getBytes(), md.digest());
    }
}
