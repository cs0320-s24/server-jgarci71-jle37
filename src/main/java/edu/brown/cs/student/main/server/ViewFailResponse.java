package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.server.ViewSuccessResponse.ViewResponseData;

public record ViewFailResponse(String response_type, ViewResponseData responseData) {

  public ViewFailResponse(ViewResponseData data) {
    this("error", data);
  }

  String serialize() {
    try {
      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<ViewFailResponse> adapter = moshi.adapter(ViewFailResponse.class);
      return adapter.toJson(this);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
}
