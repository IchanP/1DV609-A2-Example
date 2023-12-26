package main;

import java.io.IOException;
import LibraryUse.Authorization;
import WebServer.LocalServer;
import spotify.models.authorization.AuthorizationCodeFlowTokenResponse;

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
    /*     System.out.printf(" From auth: %s " + auth.getAccessToken());
        AuthorizationCodeFlowTokenResponse tokenResponse = new AuthorizationCodeFlowTokenResponse();
        System.out.printf(" From tokenResponse: %s " + tokenResponse.getAccessToken());
        System.out.println("From tokenResponse: %s " + tokenResponse.getRefreshToken()); */
    }
}
