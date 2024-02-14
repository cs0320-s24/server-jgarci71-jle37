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
    String state = request.queryParams("state");
    String county = request.queryParams("county");

    if(county == null){
      //fail response, missing arguments: county
    }
    if (state == null){
      //fail response, missing arguments:
    }

    String[][] theResponse = this.api.query(state, county);
    return null;
  }
}
