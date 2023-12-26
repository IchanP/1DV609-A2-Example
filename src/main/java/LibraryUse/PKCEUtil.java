package LibraryUse;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PKCEUtil {
  // Generates a code verifier
  public static String generateCodeVerifier() {
    SecureRandom sr = new SecureRandom();
    byte[] codeVerifier = new byte[64]; // 64 bytes for 128-bit entropy
    sr.nextBytes(codeVerifier);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(codeVerifier);
  }

  // Hashes the code verifier using SHA-256 and encodes it using base64 URL encoding
  public static String generateCodeChallenge(String codeVerifier) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    byte[] digest = md.digest(codeVerifier.getBytes());
    return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
  }

  public static String generateRandomString(int stringLength) {
    String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    SecureRandom random = new SecureRandom();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < stringLength; i++) {
      int randomIndex = random.nextInt(charSet.length());
      sb.append(charSet.charAt(randomIndex));
    }

    return sb.toString();
  }
}
