package LibraryUse;

import spotify.api.authorization.AuthorizationCodeFlowPKCE;
import spotify.api.authorization.AuthorizationPKCERequestToken;
import spotify.api.enums.AuthorizationScope;
import spotify.models.authorization.AuthorizationCodeFlowTokenResponse;
import java.util.Arrays;
import WebServer.LocalServer;
import CommonPoints.Authorization.AuthInfo;
import CommonPoints.Authorization.PKCEBase;
import CommonPoints.Authorization.PKCEUtil;
/**
 * Class showing examples of how to use Authorization PKCE flow using the wrapper library.
 */
public class LibraryPKCE extends PKCEBase{

  // Hardcoded for sake of example.
  String authCode;
  String codeVerifier;
  String codeChallenge;

  public LibraryPKCE(LocalServer server, AuthInfo authInfo) {
    super(server, authInfo);
  }

  /**
   * Example of how to use Authorization Code Flow using PKCE.
   */
  public void AuthCodeFlowPKCE() {
    this.codeVerifier = PKCEUtil.generateCodeVerifier();
    this.codeChallenge = PKCEUtil.generateCodeChallenge(codeVerifier);
    AuthorizationCodeFlowPKCE pkce = new AuthorizationCodeFlowPKCE.Builder()
        .setClientId(this.clientId)
        .setRedirectUri(this.redirectUri)
        .setResponseType("code")
        .setScopes(Arrays.asList(
          AuthorizationScope.APP_REMOTE_CONTROL,
          AuthorizationScope.PLAYLIST_MODIFY_PRIVATE, 
          AuthorizationScope.STREAMING,
          AuthorizationScope.USER_READ_EMAIL,
          AuthorizationScope.USER_READ_PRIVATE))
        .setCodeChallengeMethod("S256")
        .setCodeChallenge(codeChallenge).build();

    this.printUserURL(pkce);
    this.pauseUntilAuthorization();

    this.authCode = server.getAuthorizationCode();
    this.generateTokens();
  }

  private void generateTokens() {
    AuthorizationPKCERequestToken auth = new AuthorizationPKCERequestToken();
    AuthorizationCodeFlowTokenResponse response = auth.getAuthorizationCodeToken(this.clientId,
        this.authCode, this.redirectUri, this.codeVerifier);
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
}
