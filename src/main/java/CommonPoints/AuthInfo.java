package CommonPoints;

public class AuthInfo {
  private String clientId;
  private String redirectUri;

  public AuthInfo(String clientId, String redirectUri) {
    this.clientId = clientId;
    this.redirectUri = redirectUri;
  }

  public String getClientId() {
      return clientId;
  }

  public String getRedirectUri() {
      return redirectUri;
  }
}
