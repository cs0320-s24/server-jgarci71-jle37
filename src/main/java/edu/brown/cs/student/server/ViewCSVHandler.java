package edu.brown.cs.student.server;

import spark.Request;
import spark.Response;
import spark.Route;

public class ViewCSVHandler implements Route {
    private CSVState state;

    public ViewCSVHandler(CSVState state) {
        this.state = state;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        if(this.state.getState() == CSVState.State.LOADED) {
            //return a success response map with all the data.
        } else {
            //return a failure response map
        }
        return "this is working as expected";
    }
}
