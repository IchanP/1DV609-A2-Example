package CommonPoints;

public class RefreshRequestBuilder {
  private String base_url = "https://accounts.spotify.com/api/token";
  private String grant_type = "refresh_token";
  private String refresh_token;
  private String client_id;
  private String content_type = "application/x-www-form-urlencoded";

  public RefreshRequestBuilder(String refresh_token, String client_id) {
    this.refresh_token = refresh_token;
    this.client_id = client_id;
    this.content_type = "application/x-www-form-urlencoded";
  }

  public String getUrl() {
    return this.base_url;
  }

  public String getContentType() {
    return this.content_type;
  }

  public String buildRequestBody() {
    return "grant_type=" + encode(grant_type) + 
           "&refresh_token=" + encode(refresh_token) +
           "&client_id=" + encode(client_id);
  }

  private String encode(String value) {
    try {
      return java.net.URLEncoder.encode(value, "UTF-8");
    } catch (java.io.UnsupportedEncodingException ex) {
      throw new RuntimeException("Encoding not supported", ex);
    }
  }
}
