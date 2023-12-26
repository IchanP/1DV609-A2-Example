package SpotifyAPI;

import java.io.IOException;
import CommonPoints.AuthInfo;
import CommonPoints.PKCEUtil;
import WebServer.LocalServer;

public class DirectPKCE {
  private String codeVerifier;
  private String codeChallenge;
  private String clientId;
  private String redirectUri;
  private LocalServer server;
  private String authCode;
  private String accessToken;
  private String refreshToken;

  public DirectPKCE(LocalServer server, AuthInfo authInfo) {
    this.clientId = authInfo.getClientId();
    this.redirectUri = authInfo.getRedirectUri();
    this.server = server;
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
        codeChallenge, "http://localhost:8000/redirect",
        "app-remote-control playlist-modify-private streaming");


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

  private String handleRequest(HTTPRequester request) {
    try {
      return request.makeRequest();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }
 
  private void extractTokens(String response) {
    TokenExtractor extractor = new TokenExtractor(response);
    this.accessToken = extractor.getAccessToken();
    this.refreshToken = extractor.getRefreshToken();
  }

  // Below is for sake of example

  private void printUserUrl(AuthUrlBuilder urlBuilder) {
    System.out.println(
        "\n--------------- Please paste the following URL in your browser and authorize the app --------------------\n");
    System.out.println(urlBuilder.getUrl());
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
