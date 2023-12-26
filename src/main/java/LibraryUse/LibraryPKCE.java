package LibraryUse;

import spotify.api.authorization.AuthorizationCodeFlowPKCE;
import spotify.api.authorization.AuthorizationPKCERequestToken;
import spotify.api.enums.AuthorizationScope;
import spotify.models.authorization.AuthorizationCodeFlowTokenResponse;
import java.util.Arrays;
import WebServer.LocalServer;
import java.util.Map;
import CommonPoints.AuthInfo;
import CommonPoints.PKCEUtil;
/**
 * Class showing examples of how to use Authorization PKCE flow using the wrapper library.
 */
public class LibraryPKCE {

  // Hardcoded for sake of example.
  private String clientID;
  private String redirectURI;
  LocalServer server;
  String accessToken;
  String refreshToken;
  String authCode;
  String codeVerifier;
  String codeChallenge;

  public LibraryPKCE(LocalServer server, AuthInfo authInfo) {
    this.server = server;
    this.clientID = authInfo.getClientId();
    this.redirectURI = authInfo.getRedirectUri();
  }

  public String getAccessToken() {
    return this.accessToken;
  }

  public String getRefreshToken() {
    return this.refreshToken;
  }

  public String getClientId() {
    return this.clientID;
  }

  public void setTokens(Map<String, String> tokens) {
    System.out.printf("BEFORE: Access token %s\n Refresh token %s\n", this.accessToken, this.refreshToken);
    this.accessToken = tokens.get("access_token");
    this.refreshToken = tokens.get("refresh_token");
    System.out.printf("AFTER: Access token %s\n Refresh token %s\n", this.accessToken, this.refreshToken);
  }

  /**
   * Example of how to use Authorization Code Flow using PKCE.
   */
  public void AuthCodeFlowPKCE() {
    this.codeVerifier = PKCEUtil.generateCodeVerifier();
    this.codeChallenge = PKCEUtil.generateCodeChallenge(codeVerifier);
    AuthorizationCodeFlowPKCE pkce = new AuthorizationCodeFlowPKCE.Builder()
        .setClientId(this.clientID).setRedirectUri(this.redirectURI).setResponseType("code")
        .setScopes(Arrays.asList(AuthorizationScope.APP_REMOTE_CONTROL,
            AuthorizationScope.PLAYLIST_MODIFY_PRIVATE, AuthorizationScope.STREAMING))
        .setCodeChallengeMethod("S256").setCodeChallenge(codeChallenge).build();

    this.printUserURL(pkce);
    this.pauseUntilAuthorization();

    this.authCode = server.getAuthorizationCode();
    this.generateTokens();
  }

  private void generateTokens() {
    AuthorizationPKCERequestToken auth = new AuthorizationPKCERequestToken();
    AuthorizationCodeFlowTokenResponse response = auth.getAuthorizationCodeToken(this.clientID,
        this.authCode, this.redirectURI, this.codeVerifier);
    this.accessToken = response.getAccessToken();
    this.refreshToken = response.getRefreshToken();
  }

  // Below is for sake of example

  private void printUserURL(AuthorizationCodeFlowPKCE pkce) {
    String url = pkce.constructUrl();
    url = url.replace(" ", "%20");
    System.out.println(
        "\n--------------- Please paste the following URL in your browser and authorize the app --------------------\n");
    System.out.println(url);
    System.out.println("\n-----------------------------------");
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
