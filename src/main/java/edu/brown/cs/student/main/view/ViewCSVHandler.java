package edu.brown.cs.student.main.view;

import edu.brown.cs.student.main.server.CSVState;
import java.util.ArrayList;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * This is a handler for the "/csvview" endpoint. It displays the data sets.
 */
public class ViewCSVHandler implements Route {
  private final CSVState state;

  public ViewCSVHandler(CSVState state) {
    this.state = state;
  }

  /**
   * Calls on the loaded data and displays it.
   * @param request
   * @param response
   * @return a viewsuccessresponse or viewfailresponse
   * @throws Exception
   */
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
