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
    return null;
  }
}
