package SpotifyAPI;

import org.json.JSONObject;

public class TokenExtractor {
  private String jsonString;

  public TokenExtractor(String response) {
    this.jsonString = response;
  }

  public String getAccessToken() {
    JSONObject jsonObject = new JSONObject(this.jsonString);
    return jsonObject.getString("access_token");
  }

  public String getRefreshToken() {
    JSONObject jsonObject = new JSONObject(this.jsonString);
    return jsonObject.getString("refresh_token");
  }
}
