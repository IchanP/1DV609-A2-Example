package WebServer;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

public class LocalServer {
  // TODO maybe fix port
  private static final int PORT = 8000;
  private static final String CONTEXT = "/redirect";
  private final CountDownLatch latch = new CountDownLatch(1);
  private String authorizationCode = null;

  public void startServer() throws IOException {
    HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
    server.createContext(CONTEXT, new MyHandler());
    server.setExecutor(null); // creates a default executor
    server.start();
    System.out.println("Server started at http://localhost:" + PORT + CONTEXT);
    try {
      latch.await();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private class MyHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange t) throws IOException {
      String query = t.getRequestURI().getQuery();
      String response;

      // Check if the query string contains the authorization code
      if (query != null && query.contains("code=")) {
        // Extract the authorization code from the query string
        authorizationCode = query.split("code=")[1].split("&")[0];

        response = "Authorization successful. You can close this window.";
        latch.countDown(); // Signal that the code has been received
      } else {
        // If the code is not present, just respond without setting the code
        response = "No authorization code received.";
      }

      System.out.println("Hello from web server");
      t.sendResponseHeaders(200, response.length());
      OutputStream os = t.getResponseBody();
      os.write(response.getBytes());
      os.close();
    }
  }


  public String getAuthorizationCode() {
    return authorizationCode;
  }
}
