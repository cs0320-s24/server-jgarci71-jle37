package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.server.BroadbandSuccessResponse.BroadbandResponseData;
import spark.Request;
import spark.Response;
import spark.Route;

public class BroadbandHandler implements Route {

  private final ACSAPI api;

  public BroadbandHandler(ACSAPI api) {
    this.api = api;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    String stateName = request.queryParams("state");
    String countyName = request.queryParams("county");
    // if county does not exist throw error
    // if county is not given then do all counties

    try {
      String[][] theResponse = this.api.query(stateName, countyName);
      //      for (String[] lis : theResponse) {
      //        System.out.println();
      //        for (String s : lis) {
      //          System.out.print(s + ", ");
      //        }
      //      }
      return new BroadbandSuccessResponse(
              new BroadbandResponseData(stateName, countyName, theResponse[1][1]))
          .serialize();
    } catch (IllegalArgumentException e) {
      return new BroadbandFailResponse(
              new BroadbandResponseData(stateName, countyName, e.getMessage()))
          .serialize();
    }
  }
}
