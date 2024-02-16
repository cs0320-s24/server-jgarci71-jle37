package edu.brown.cs.student.main.broadband;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.broadband.BroadbandSuccessResponse.BroadbandResponseData;

/** Represents a Fail response for the broadband handler. */
public record BroadbandFailResponse(String response_type, BroadbandResponseData responseData) {

  public BroadbandFailResponse(BroadbandResponseData data) {
    this("error", data);
  }

  /**
   * Converts the record into a JSON representation.
   *
   * @return
   */
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
