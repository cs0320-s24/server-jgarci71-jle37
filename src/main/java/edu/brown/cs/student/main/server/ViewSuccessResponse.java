package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.util.List;

public record ViewSuccessResponse(String response_type, ViewResponseData responseMap) {

  public record ViewResponseData(String filepath, List<List<String>> data) {}

  public ViewSuccessResponse(ViewResponseData data) {
    this("success", data);
  }

  String serialize() {
    try {
      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<ViewSuccessResponse> adapter = moshi.adapter(ViewSuccessResponse.class);
      return adapter.toJson(this);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  ;
}
