package SpotifyAPI;

public class PKCEAccessTokenRequestBuilder {
  private String grant_type;
  private String authCode;
  private String redirectUri;
  private String codeVerifier;
  private String clientId;
  private String contentType;

  public PKCEAccessTokenRequestBuilder(String authCode, String redirectUri, String codeVerifier,
      String clientId) {
    this.grant_type = "authorization_code";
    this.authCode = authCode;
    this.redirectUri = redirectUri;
    this.codeVerifier = codeVerifier;
    this.clientId = clientId;
    this.contentType = "application/x-www-form-urlencoded";
  }

  public String getGrantType() {
    return this.grant_type;
  }

  public String getAuthCode() {
    return this.authCode;
  }

  public String getRedirectUri() {
    return this.redirectUri;
  }

  public String getCodeVerifier() {
    return this.codeVerifier;
  }

  public String getClientId() {
    return this.clientId;
  }

  public String getContentType() {
    return this.contentType;
  }

  public String buildRequestBody() {
    return "grant_type=" + encode(grant_type) + "&code=" + encode(authCode) + "&redirect_uri="
        + encode(redirectUri) + "&client_id=" + encode(clientId) + "&code_verifier="
        + encode(codeVerifier);
  }

  private String encode(String value) {
    try {
      return java.net.URLEncoder.encode(value, "UTF-8");
    } catch (java.io.UnsupportedEncodingException ex) {
      throw new RuntimeException("Encoding not supported", ex);
    }
  }
}
