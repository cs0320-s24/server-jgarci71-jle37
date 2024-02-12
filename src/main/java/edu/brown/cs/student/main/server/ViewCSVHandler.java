package edu.brown.cs.student.main.server;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;

public class ViewCSVHandler implements Route {
    private final CSVState state;

    public ViewCSVHandler(CSVState state) {
        this.state = state;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        if (this.state.getState() == CSVState.State.LOADED) {
            return new SearchViewSuccessResponse(
                    new SearchViewSuccessResponse.SearchViewResponseData(
                            this.state.getFilePath(), this.state.getLoadResults()))
                    .serialize();
        } else {
            return new SearchViewFailResponse(
                    new SearchViewSuccessResponse.SearchViewResponseData(
                            this.state.getFilePath(), new ArrayList<>()))
                    .serialize();
        }
    }
}

