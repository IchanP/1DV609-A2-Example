package SpotifyAPI;

public class AuthUrlBuilder {

  private String baseUrl = "https://accounts.spotify.com/authorize";
  private String url;
  /**
 * Constructs a new AuthUrlBuilder used for building an authentication URL with the specified parameters.
 * This constructor formats the URL based on the OAuth 2.0 authorization flow, incorporating elements 
 * like client ID, redirect URI, response type, code challenge method, code challenge, state, and scope.
 * JavaDoc generated with ChatGPT 4.
 *
 * @param clientId            The client identifier issued to the client during the registration process.
 * @param redirectUri         The URI to which the response will be sent after authorization. Must be URL encoded.
 * @param responseType        The type of the response desired. This should typically be 'code' for requesting an authorization code.
 * @param codeChallengeMethod The method used to derive the code challenge. Usually 'S256' or 'plain'.
 * @param codeChallenge       The code challenge, derived from the code verifier sent to the authorization server.
 * @param state               An opaque value used by the client to maintain state between the request and callback, helping to prevent CSRF attacks.
 * @param scope               The scope of the access request. Multiple scopes should be separated by spaces. It's important that the scopes are correctly formatted with spaces between them, as they define the permissions the application is requesting.
 */
  public AuthUrlBuilder(String clientId, String redirectUri, String responseType, String codeChallengeMethod, String codeChallenge, String state, String scope) {
     this.url = String.format("%s?client_id=%s&redirect_uri=%s&response_type=%s&code_challenge_method=%s&code_challenge=%s&state=%s&scope=%s",
        baseUrl, clientId, redirectUri, responseType, codeChallengeMethod, codeChallenge, state, scope);
        this.url = this.url.replace(" ", "%20");
  }

  public String getUrl () {
    return this.url;
  }
}
