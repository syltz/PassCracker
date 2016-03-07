import java.util.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.io.*;
import javax.xml.bind.DatatypeConverter;

public class Cracker  {
    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
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
    public static boolean compareHashToWord(String hash, String word) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] bytesOfWord = word.getBytes(StandardCharsets.UTF_8.name());
        md.update(bytesOfWord);
        byte[] digestBytes = md.digest();
        String genHash = DatatypeConverter.printHexBinary(digestBytes);
        return hash.equalsIgnoreCase(genHash);
    }
}
