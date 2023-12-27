package CommonPoints;

import org.json.JSONObject;

public class TokenExtractor {
  private String jsonString;

  public TokenExtractor(String response) {
    this.jsonString = response;
  }

  public String getStringByFieldName(String fieldName) {
    JSONObject json = new JSONObject(this.jsonString);
    return json.getString(fieldName);
  }

  public int getIntByFieldName(String fieldName) {
    JSONObject json = new JSONObject(this.jsonString);
    return json.getInt(fieldName);
  }

  public String getNestedField(String objectName, String fieldName) {
    JSONObject json = new JSONObject(this.jsonString);
    JSONObject nestedObject = json.getJSONObject(objectName);
    return nestedObject.getString(fieldName);
  }
}
