package SpotifyAPI;

import java.io.IOException;
import CommonPoints.HTTPRequester;
import CommonPoints.TokenExtractor;

public class GetCurrentUser {
  private String endpoint = "https://api.spotify.com/v1/me";
  private String authToken;
  private TokenExtractor tokenExtractor;

  public GetCurrentUser(String authToken) {
    this.authToken = authToken;
  }

  public void getCurrentUser() {
    CommonPoints.HTTPRequester request =
        new CommonPoints.HTTPRequester("GET", "application/json", this.endpoint);
    request.setAuthToken(authToken);
    String response = this.handleRequest(request);

    // Extract fields from the response.
    tokenExtractor = new TokenExtractor(response);
    System.out.println("-------------------------------------------------");
    System.out.println(tokenExtractor.getStringByFieldName("display_name"));
    System.out.println(tokenExtractor.getStringByFieldName("email"));
    System.out.println("\n-------------------------------------------------");
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
}
