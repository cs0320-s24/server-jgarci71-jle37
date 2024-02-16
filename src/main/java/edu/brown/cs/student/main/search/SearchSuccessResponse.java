package edu.brown.cs.student.main.search;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.util.List;

public record SearchSuccessResponse(String response_type, SearchResponseData responseMap) {

  public record SearchResponseData(
      String filepath, String token, String column, List<List<String>> data) {}

  public SearchSuccessResponse(SearchResponseData data) {
    this("success", data);
  }

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
