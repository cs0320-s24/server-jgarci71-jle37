package edu.brown.cs.student.server;

import spark.Request;
import spark.Response;
import spark.Route;

public class SearchCSVHandler implements Route {
    private final CSVState state;

    public SearchCSVHandler(CSVState state) {
        this.state = state;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return "this is working as expected";
    }
}
