package CommonPoints;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class HTTPRequester {

    private String method;
    private String contentType;
    private String url;
    private String body;
    private String authToken;

    public HTTPRequester(String method, String contentType, String url) {
        this.setMethod(method);
        this.url = url;
        this.contentType = contentType;
    }

    public void setBody(String body) {
        if (this.method.equals("POST")) {
            this.body = body;
        } else {
            throw new IllegalStateException("Body can only be set for POST requests");
        }
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String makeRequest() throws IOException {
        HttpURLConnection connection = createConnection();

        if (this.method.equals("POST") && this.body != null) {
            sendPostData(connection);
        }

        return getResponse(connection);
    }

    private HttpURLConnection createConnection() throws IOException {
        URI uri;
        try {
            uri = new URI(this.url);
        } catch (URISyntaxException e) {
            throw new IOException("Invalid URL syntax", e);
        }
        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod(this.method);
        connection.setRequestProperty("Content-Type", this.contentType);
        if (this.authToken != null && !this.authToken.isEmpty()) {
            connection.setRequestProperty("Authorization", "Bearer " + this.authToken);
        }

        return connection;
    }

    private void sendPostData(HttpURLConnection connection) throws IOException {
        connection.setDoOutput(true);
        try (OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os))) {
            writer.write(this.body);
        }
    }

    private String getResponse(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in =
                    new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                return response.toString();
            }
        } else {
            throw new IOException("Request failed with HTTP code: " + responseCode);
        }
    }

    private void setMethod(String method) {
        if (method.equals("GET") || method.equals("POST")) {
            this.method = method;
        } else {
            throw new IllegalArgumentException("Invalid method: " + method);
        }
    }
}
