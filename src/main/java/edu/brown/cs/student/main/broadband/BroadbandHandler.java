package edu.brown.cs.student.main.broadband;

import edu.brown.cs.student.main.ACSDatasource.ACSDatasource;
import edu.brown.cs.student.main.broadband.BroadbandSuccessResponse.BroadbandResponseData;
import java.util.Date;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * This is the handler for the "/broadband" endpoint. It takes in a datasource to perform queries
 * with given the expected parameters.
 */
public class BroadbandHandler implements Route {

  private final ACSDatasource api;

  public BroadbandHandler(ACSDatasource api) {
    this.api = api;
  }

  /**
   * Calls on the ACSDataSource to perform a query
   *
   * @param request
   * @param response
   * @return either a BroadBandFailResponse or a BroadBandSuccessResponse
   * @throws Exception
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {
    String stateName = request.queryParams("state");
    String countyName = request.queryParams("county");
    Date timeRequested = new Date();
    // if county does not exist throw error
    // if county is not given then do all counties
    if (stateName == null || stateName.isEmpty()) {
      return new BroadbandFailResponse(
              "error_bad_request",
              new BroadbandResponseData(
                  "missing stateName", "unparsed", "stateName is null", timeRequested.toString()))
          .serialize();
    }
    if (countyName == null || countyName.isEmpty()) {
      return new BroadbandFailResponse(
              "error_bad_request",
              new BroadbandResponseData(
                  stateName, "missing", "countyName is null", timeRequested.toString()))
          .serialize();
    }
    try {
      String[][] theResponse = this.api.query(stateName, countyName);
      return new BroadbandSuccessResponse(
              new BroadbandResponseData(
                  theResponse[1][0].split(",")[1],
                  theResponse[1][0].split(",")[0],
                  theResponse[1][1],
                  timeRequested.toString()))
          .serialize();
    } catch (IllegalArgumentException e) {
      return new BroadbandFailResponse(
              "error_bad_request",
              new BroadbandResponseData(
                  stateName, countyName, e.getMessage(), timeRequested.toString()))
          .serialize();
    }
  }
}
