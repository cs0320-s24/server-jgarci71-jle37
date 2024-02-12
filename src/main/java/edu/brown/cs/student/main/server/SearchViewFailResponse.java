package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.server.SearchViewSuccessResponse.SearchViewResponseData;

public record SearchViewFailResponse(String response_type, SearchViewResponseData responseData) {

  public SearchViewFailResponse(SearchViewResponseData data) {
    this("error", data);
  }

  String serialize() {
    try {
      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<SearchViewFailResponse> adapter = moshi.adapter(SearchViewFailResponse.class);
      return adapter.toJson(this);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
}
