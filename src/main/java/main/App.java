package main;

import java.io.IOException;
import CommonPoints.AuthInfo;
import CommonPoints.RefreshPCKE;
import LibraryUse.LibraryPKCE;
import SpotifyAPI.DirectPKCE;
import WebServer.LocalServer;
import java.util.Map;
/**
 * Entry point.
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
        AuthInfo authInfo = new AuthInfo("cac3091070c343f0ac6e6c1f26bb43fe", "http://localhost:8000/redirect");
         DirectPKCE directAuth = new DirectPKCE(server, authInfo);
        directAuth.AuthCodeFlowPKCE();
        System.out.println(directAuth.getAccessToken());
        System.out.println(directAuth.getRefreshToken());
      /*    LibraryPKCE libraryAuth = new LibraryPKCE(server, authInfo);
        libraryAuth.AuthCodeFlowPKCE();
        RefreshPCKE refreshPKCE = new RefreshPCKE();
        Map<String, String> tokens = refreshPKCE.refreshToken(libraryAuth.getRefreshToken(), authInfo.getClientId());
        libraryAuth.setTokens(tokens); */
    }
}
