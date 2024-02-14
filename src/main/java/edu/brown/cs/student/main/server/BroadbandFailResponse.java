package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.server.BroadbandSuccessResponse.BroadbandResponseData;

public record BroadbandFailResponse(String response_type, BroadbandResponseData responseData) {

  public BroadbandFailResponse(BroadbandResponseData data) {
    this("error", data);
  }

  String serialize() {
    try {
      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<BroadbandFailResponse> adapter = moshi.adapter(BroadbandFailResponse.class);
      return adapter.toJson(this);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
}
