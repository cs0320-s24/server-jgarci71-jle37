package edu.brown.cs.student.server;

import spark.Request;
import spark.Response;
import spark.Route;
import edu.brown.cs.student.server.SearchViewSuccessResponse.SearchViewResponseData;

import java.util.ArrayList;

public class ViewCSVHandler implements Route {
    private final CSVState state;

    public ViewCSVHandler(CSVState state) {
        this.state = state;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        if(this.state.getState() == CSVState.State.LOADED) {
            //make success response here
            return new SearchViewSuccessResponse(
                    new SearchViewResponseData(
                            this.state.getFilePath(),
                            this.state.getLoadResults()
                    )
            ).serialize();
        } else {
            return new SearchViewFailResponse(
                    new SearchViewResponseData(
                            this.state.getFilePath(),
                            new ArrayList<>()
                    )
            ).serialize();
        }
    }
}
