package edu.brown.cs.student.main.view;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.util.List;

/**
 * This represents a success response for csvview.
 * @param response_type
 * @param responseMap
 */
public record ViewSuccessResponse(String response_type, ViewResponseData responseMap) {

  public record ViewResponseData(String filepath, List<List<String>> data) {}

  public ViewSuccessResponse(ViewResponseData data) {
    this("success", data);
  }

  /**
   * This converts the record into a JSON representation.
   * @return a JSON
   */
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
