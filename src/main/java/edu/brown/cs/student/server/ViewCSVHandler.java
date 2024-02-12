package edu.brown.cs.student.server;

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
        if(this.state.getState() == CSVState.State.LOADED) {
            System.out.println("yayyy it worked");
            //make success response here
            //response map should map data to all the rows
        } else {
            System.out.println("you need to load");
            //response map should map filepath to the path
        }
        return "this is working as expected";
    }
}
