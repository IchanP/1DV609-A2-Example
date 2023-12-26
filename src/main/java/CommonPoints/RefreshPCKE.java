package CommonPoints;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONObject;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class RefreshPCKE {

  private final String tokenEndpoint = "https://accounts.spotify.com/api/token";


  public Map<String, String> refreshToken(String refreshToken, String clientId) {
    try {
      HttpURLConnection connection = createConnection(tokenEndpoint);
      String params = String.format("grant_type=refresh_token&refresh_token=%s&client_id=%s",
          refreshToken, clientId);
      sendRequest(connection, params);

      BufferedReader reader =
          new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String response = readResponse(reader);
      reader.close();

      return parseTokens(response);

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private HttpURLConnection createConnection(String endpoint) throws Exception {
    URI uri = new URI(endpoint);
    URL url = uri.toURL();
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("POST");
    connection.setDoOutput(true);
    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    return connection;
  }

  private void sendRequest(HttpURLConnection connection, String params) throws Exception {
    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
    writer.write(params);
    writer.flush();
    writer.close();
  }

  private String readResponse(BufferedReader reader) throws Exception {
    String line;
    StringBuilder response = new StringBuilder();
    while ((line = reader.readLine()) != null) {
      response.append(line);
    }
    return response.toString();
  }

  private Map<String, String> parseTokens(String response) {
    JSONObject jsonResponse = new JSONObject(response);
    Map<String, String> tokens = new HashMap<>();
    tokens.put("access_token", jsonResponse.getString("access_token"));
    tokens.put("refresh_token", jsonResponse.optString("refresh_token"));
    return tokens;
  }
}
