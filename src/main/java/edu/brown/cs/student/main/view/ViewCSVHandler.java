package edu.brown.cs.student.main.view;

import java.util.ArrayList;

import edu.brown.cs.student.main.server.CSVState;
import spark.Request;
import spark.Response;
import spark.Route;

public class ViewCSVHandler implements Route {
  private final CSVState state;

  public ViewCSVHandler(CSVState state) {
    this.state = state;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    if (this.state.getState() == CSVState.State.LOADED) {
      return new ViewSuccessResponse(
              new ViewSuccessResponse.ViewResponseData(
                  this.state.getFilePath(), this.state.getLoadResults()))
          .serialize();
    } else {
      return new ViewFailResponse(
              "error_datasource",
              new ViewSuccessResponse.ViewResponseData(this.state.getFilePath(), new ArrayList<>()))
          .serialize();
    }
  }
}
