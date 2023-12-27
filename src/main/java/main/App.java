package main;

import java.io.IOException;
import CommonPoints.Authorization.AuthInfo;
import LibraryUse.LibraryPKCE;
import LibraryUse.UseLibrary;
import SpotifyAPI.DirectPKCE;
import SpotifyAPI.GetArtist;
import WebServer.LocalServer;

/**
 * Entry point.
 *
 */
public class App {
    private String taylorSwiftArtistId = "06HL4z0CvFAxyc27GXpf02";
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


        AuthInfo authInfo =
                new AuthInfo("cac3091070c343f0ac6e6c1f26bb43fe", "http://localhost:8000/redirect");
        App app = new App();
        // Comment out/uncomment if you want to swap between the two.

         app.directAuth(server, authInfo);

      //  app.libraryAuth(server, authInfo);
    }

    private void libraryAuth(LocalServer server, AuthInfo authInfo) {
        LibraryPKCE libraryAuth = new LibraryPKCE(server, authInfo);
        libraryAuth.AuthCodeFlowPKCE();
        libraryAuth.refreshTokens();
        
        UseLibrary library = new UseLibrary(libraryAuth.getAccessToken());
        library.getArtistFromId(taylorSwiftArtistId);
    }

    private void directAuth(LocalServer server, AuthInfo authInfo) {
        DirectPKCE directAuth = new DirectPKCE(server, authInfo);
        directAuth.AuthCodeFlowPKCE();
        directAuth.refreshTokens();

        GetArtist artist = new GetArtist(directAuth.getAccessToken());
        artist.getArtistFromId(taylorSwiftArtistId);
    }
}
