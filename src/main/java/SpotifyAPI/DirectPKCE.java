package SpotifyAPI;

import CommonPoints.AuthInfo;
import CommonPoints.HTTPRequester;
import CommonPoints.PKCEUtil;
import WebServer.LocalServer;
import CommonPoints.PKCEBase;

public class DirectPKCE extends PKCEBase {
  private String codeVerifier;
  private String codeChallenge;
  private String authCode;

  public DirectPKCE(LocalServer server, AuthInfo authInfo) {
    super(server, authInfo);
  }

  public String getAccessToken() {
    return this.accessToken;
  }

  public String getRefreshToken() {
    return this.refreshToken;
  }

  public void AuthCodeFlowPKCE() {
    this.codeVerifier = PKCEUtil.generateCodeVerifier();
    this.codeChallenge = PKCEUtil.generateCodeChallenge(codeVerifier);
    // Biggest difference is having to look at docs for the different scopes. There is no
    // intellisense to help.
    AuthUrlBuilder urlBuilder = new AuthUrlBuilder(this.clientId, this.redirectUri, "code", "S256",
        codeChallenge, "", "app-remote-control playlist-modify-private streaming");


    this.printUserUrl(urlBuilder);
    this.pauseUntilAuthorization();

    this.authCode = server.getAuthorizationCode();
    PKCEAccessTokenRequestBuilder requestBuilder = new PKCEAccessTokenRequestBuilder(this.authCode,
        this.redirectUri, this.codeVerifier, this.clientId);
    this.generateTokens(requestBuilder);
  }

  private void generateTokens(PKCEAccessTokenRequestBuilder params) {
    HTTPRequester request = new HTTPRequester("POST", params.getContentType(),
        "https://accounts.spotify.com/api/token");
    request.setBody(params.buildRequestBody());
    String response = this.handleRequest(request);
    this.extractTokens(response);
  }
}
