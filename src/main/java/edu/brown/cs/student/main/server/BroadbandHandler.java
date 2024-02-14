package edu.brown.cs.student.main.server;

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

    if (countyName == null) {
      // fail response, missing arguments: county
    }
    if (stateName == null) {
      // fail response, missing arguments:
    }

    String[][] theResponse = this.api.query(stateName, countyName);
    if(theResponse.length == 0){
      return "no results found";
    }
    return theResponse[1][1];
  }
}
