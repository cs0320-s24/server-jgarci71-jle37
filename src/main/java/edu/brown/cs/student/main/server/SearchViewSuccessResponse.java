package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.util.List;

public record SearchViewSuccessResponse(String response_type, SearchViewResponseData responseMap) {

  public record SearchViewResponseData(String filepath, List<List<String>> data) {}

  public SearchViewSuccessResponse(SearchViewResponseData data) {
    this("success", data);
  }

  String serialize() {
    try {
      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<SearchViewSuccessResponse> adapter =
          moshi.adapter(SearchViewSuccessResponse.class);
      return adapter.toJson(this);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  ;
}
