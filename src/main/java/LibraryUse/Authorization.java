package LibraryUse;

import spotify.*;
import spotify.api.authorization.AuthorizationCodeFlowPKCE;
import spotify.api.authorization.AuthorizationPKCERequestToken;
import spotify.api.enums.AuthorizationScope;
import spotify.models.authorization.AuthorizationCodeFlowTokenResponse;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import WebServer.LocalServer;

/**
 * Class showing examples of how to use Authorization PKCE flow using the wrapper library.
 */
public class Authorization {

  // Hardcoded for sake of example.
  private String clientID = "cac3091070c343f0ac6e6c1f26bb43fe";
  private String redirectURI = "http://localhost:8000/redirect";
  LocalServer server;
  String accessToken;
  public Authorization(LocalServer server) {
    this.server = server;
  }

  /**
   * Example of how to use Authorization Code Flow using PKCE.
   */
  public void AuthCodeFlowPKCE() {
    String codeVerifier = PKCEUtil.generateCodeVerifier();
    String codeChallenge = this.handleGenerateCodeChallenge(codeVerifier);
    String state = PKCEUtil.generateRandomString(16);
    System.out.printf("codeVerifier %s ", codeVerifier);
    AuthorizationCodeFlowPKCE pkce = new AuthorizationCodeFlowPKCE.Builder()
    .setClientId(this.clientID)
        .setRedirectUri(this.redirectURI)
        .setResponseType("code")
        .setScopes(Arrays.asList(
          AuthorizationScope.APP_REMOTE_CONTROL,
          AuthorizationScope.PLAYLIST_MODIFY_PRIVATE))
        .setCodeChallengeMethod("S256")
        .setCodeChallenge(codeChallenge)
        .setState(state)
        .build();

    this.printUserURL(pkce);
    this.pauseUntilAuthorization();

    String authorizationCode = server.getAuthorizationCode();
    // In both cases, your app should compare the state parameter that it
    // received in the redirection URI with the state parameter it originally provided to Spotify
    // in the authorization URI. If there is a mismatch then your app should reject the request and
    // stop the authentication flow.
    // TODO get state code?

    this.generateAccessToken(authorizationCode, codeVerifier);
  }

  public String getAccessToken() {
    return this.accessToken;
  }

  private void generateAccessToken(String authCode, String codeVerifier) {
    AuthorizationPKCERequestToken auth = new AuthorizationPKCERequestToken();
    final String accessToken = auth.getAuthorizationCodeToken(
            this.clientID,
            authCode,
            this.redirectURI,
            codeVerifier)
            .getAccessToken();
  }

  private String handleGenerateCodeChallenge(String codeVerifier) {
    try {
      return PKCEUtil.generateCodeChallenge(codeVerifier);
    } catch (NoSuchAlgorithmException e) {
      // Need to handle error here
      e.printStackTrace();
      return "";
    }
  }

  private void printUserURL(AuthorizationCodeFlowPKCE pkce) {
    String url = pkce.constructUrl();
    System.out.println("-----------------------------------");
    System.out.println("Please paste the following URL in your browser and authorize the app:");
    System.out.println(url);
    System.out.println("-----------------------------------");
  }

  private void pauseUntilAuthorization() {
    do {
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    } while (this.server.getAuthorizationCode() == null);
  }
}
