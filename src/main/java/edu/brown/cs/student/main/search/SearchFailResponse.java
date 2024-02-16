package edu.brown.cs.student.main.search;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.search.SearchSuccessResponse.SearchResponseData;

/**
 * This is a fail response for search.
 * @param response_type
 * @param responseData
 */
public record SearchFailResponse(String response_type, SearchResponseData responseData) {

  public SearchFailResponse(SearchResponseData data) {
    this("error", data);
  }

  /**
   * This converts the record into a JSON representation
   * @return a JSON
   */
  String serialize() {
    try {
      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<SearchFailResponse> adapter = moshi.adapter(SearchFailResponse.class);
      return adapter.toJson(this);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
}
