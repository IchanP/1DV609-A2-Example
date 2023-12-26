package main;

import java.io.IOException;
import CommonPoints.RefreshPCKE;
import LibraryUse.Authorization;
import WebServer.LocalServer;
import java.util.Map;
/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        LocalServer server = new LocalServer();

        // Start the server in a new thread
        new Thread(() -> {
            try {
                server.startServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        Authorization auth = new Authorization(server);
        auth.AuthCodeFlowPKCE();
         RefreshPCKE refreshPKCE = new RefreshPCKE();
        Map<String, String> tokens = refreshPKCE.refreshToken(auth.getRefreshToken(), auth.getClientId());
        auth.setTokens(tokens);
    }
}
