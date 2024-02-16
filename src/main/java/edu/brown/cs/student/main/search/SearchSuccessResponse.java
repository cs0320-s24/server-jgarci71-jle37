package edu.brown.cs.student.main.search;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.util.List;

/**
 * Represents a successful response for a csvsearch endpoint call. Returns a success response type
 * with the expected data.
 */
public record SearchSuccessResponse(String response_type, SearchResponseData responseMap) {

  public record SearchResponseData(
      String filepath, String token, String column, List<List<String>> data) {}

  public SearchSuccessResponse(SearchResponseData data) {
    this("success", data);
  }

  /**
   * This converts the record into a JSON representation
   * @return a JSON
   */
  String serialize() {
    try {
      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<SearchSuccessResponse> adapter = moshi.adapter(SearchSuccessResponse.class);
      return adapter.toJson(this);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  ;
}
