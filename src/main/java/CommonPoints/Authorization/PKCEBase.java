package CommonPoints.Authorization;

import WebServer.LocalServer;
import java.io.IOException;
import CommonPoints.HTTPRequester;
import CommonPoints.TokenExtractor;
import SpotifyAPI.AuthUrlBuilder;

public abstract class PKCEBase {
    protected String accessToken;
    protected String refreshToken;
    protected LocalServer server;
    protected String clientId;
    protected String redirectUri;

    public PKCEBase(LocalServer server, AuthInfo authInfo) {
        this.server = server;
        this.clientId = authInfo.getClientId();
        this.redirectUri = authInfo.getRedirectUri();
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public void refreshTokens() {
        RefreshRequestBuilder requestBuilder = new RefreshRequestBuilder(this.refreshToken, this.clientId);
        HTTPRequester request = new HTTPRequester("POST", requestBuilder.getContentType(), requestBuilder.getUrl());
        request.setBody(requestBuilder.buildRequestBody());
        String response = this.handleRequest(request);
        this.extractTokens(response);
    }

    protected String handleRequest(HTTPRequester request) {
        try {
            return request.makeRequest();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void extractTokens(String response) {
        System.out.printf("BEFORE: Access token %s\n Refresh token %s\n", this.accessToken, this.refreshToken);
        TokenExtractor extractor = new TokenExtractor(response);

        // Requires user to know field name. No intellisense to help.
        this.accessToken = extractor.getStringByFieldName("access_token");
        this.refreshToken = extractor.getStringByFieldName("refresh_token");

        System.out.printf("AFTER: Access token %s\n Refresh token %s\n", this.accessToken, this.refreshToken);
    }


  // -------------------- Below is for sake of the example -----------------------------------------

  protected void printUserUrl(AuthUrlBuilder urlBuilder) {
    System.out.println(
        "\n--------------- Please paste the following URL in your browser and authorize the app --------------------\n");
    System.out.println(urlBuilder.getUrl());
    System.out.println("\n-----------------------------------");
  }

  protected void pauseUntilAuthorization() {
    do {
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    } while (this.server.getAuthorizationCode() == null);
  }
}
