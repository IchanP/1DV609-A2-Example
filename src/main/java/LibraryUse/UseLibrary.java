package LibraryUse;

import spotify.api.*;
import spotify.api.spotify.SpotifyApi;
import spotify.models.artists.ArtistFull;

public class UseLibrary {

  SpotifyApi api;


  public UseLibrary(String token) {
    this.api = new SpotifyApi(token);
  }

  public void getArtistFromId(String artistId) {
    ArtistFull artist = api.getArtist(artistId);
    // There's a method for every field in the response.
    System.out.println("-------------------------------------------------");
    System.out.println(artist.getName());
    System.out.printf("Spotify URL: %s", artist.getExternalUrls().getSpotify());
    System.out.printf("\nPopularity: %d", artist.getPopularity());
    System.out.println("\n-------------------------------------------------");
  }
}
