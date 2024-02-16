package edu.brown.cs.student.main.view;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.view.ViewSuccessResponse.ViewResponseData;

/**
 * This represents a fail response for view
 * @param response_type
 * @param responseData
 */
public record ViewFailResponse(String response_type, ViewResponseData responseData) {

  public ViewFailResponse(ViewResponseData data) {
    this("error", data);
  }

  /**
   * This converts the record into a JSON representation
   * @return a JSON
   */
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
