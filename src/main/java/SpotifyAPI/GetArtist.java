package SpotifyAPI;

import java.io.IOException;
import CommonPoints.HTTPRequester;
import CommonPoints.TokenExtractor;

public class GetArtist {
  private String endpoint = "https://api.spotify.com/v1/artists/";
  private String authToken;
  private TokenExtractor tokenExtractor;

  public GetArtist(String authToken) {
    this.authToken = authToken;
  }

  public void getArtistFromId(String artistId) {
    String url = this.endpoint + artistId;
    CommonPoints.HTTPRequester request =
        new CommonPoints.HTTPRequester("GET", "application/json", url);
    request.setAuthToken(authToken);
    String response = this.handleRequest(request);

    // Extract fields from the response.
    tokenExtractor = new TokenExtractor(response);
    System.out.println("-------------------------------------------------");
    System.out.println(tokenExtractor.getStringByFieldName("name"));
    System.out.printf("Spotify URL: %s", tokenExtractor.getNestedField("external_urls", "spotify"));
    System.out.printf("\nPopularity: %s", tokenExtractor.getIntByFieldName("popularity"));
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
