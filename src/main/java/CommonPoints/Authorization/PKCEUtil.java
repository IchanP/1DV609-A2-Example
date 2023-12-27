package CommonPoints.Authorization;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.security.SecureRandom;

public class PKCEUtil {

  public static String generateCodeVerifier() {
    SecureRandom secureRandom = new SecureRandom();
    byte[] codeVerifier = new byte[32];
    secureRandom.nextBytes(codeVerifier);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(codeVerifier);
  }

  public static String generateCodeChallenge(final String codeVerifier) {
    byte[] digest = null;
    try {
      byte[] bytes = codeVerifier.getBytes("US-ASCII");
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
      messageDigest.update(bytes, 0, bytes.length);
      digest = messageDigest.digest();
    } catch (UnsupportedEncodingException | NoSuchAlgorithmException exception) {
      exception.printStackTrace();
    }
    return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
  }
}
