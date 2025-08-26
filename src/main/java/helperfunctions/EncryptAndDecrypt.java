package helperfunctions;

import java.util.Base64;

public class EncryptAndDecrypt {
    static String encodedString="";
    public static void main(String[] args) {
        try {
            // Original string
            String originalString = "PASS_PASSWORD_HERE_TO_ENCRYPT";

            // Encode the string
            encodedString = Base64.getEncoder().encodeToString(originalString.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String decryptMethod(String pwd) {
        String decodedString = null;
        try {
            // Base64 encoded string
            String encodedString = pwd;

            // Decode the string
            byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
            decodedString = new String(decodedBytes);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return decodedString;
    }
}
