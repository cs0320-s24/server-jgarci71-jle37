package edu.brown.cs.student.main.broadband;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

/**
 * Represents a successful response for a Broadband endpoint call. Returns a success response type
 * with the expected data.
 */
public record BroadbandSuccessResponse(String response_type, BroadbandResponseData responseMap) {

  public record BroadbandResponseData(
      String state, String county, String percentage, String timeRequested) {}

  public BroadbandSuccessResponse(BroadbandResponseData data) {
    this("success", data);
  }

  /**
   * Converts th BroadBandSuccessResponse into a JSON representation.
   *
   * @return
   */
  String serialize() {
    try {
      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<BroadbandSuccessResponse> adapter = moshi.adapter(BroadbandSuccessResponse.class);
      return adapter.toJson(this);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  ;
}
