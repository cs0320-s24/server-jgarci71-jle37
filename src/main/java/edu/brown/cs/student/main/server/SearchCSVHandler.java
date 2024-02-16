package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.search.CSVSearch;
import edu.brown.cs.student.main.server.SearchSuccessResponse.SearchResponseData;
import java.util.ArrayList;
import java.util.List;
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
    // here we will call our parse,
    List<List<String>> results = new ArrayList<>();

    //    String filePath = request.queryParams("file");
    String path = this.state.getFilePath();
    String searchToken = request.queryParams("token");
    String targetColumn = request.queryParams("column");

    if (searchToken == null) {
      return new SearchFailResponse(
              "error_bad_request", new SearchResponseData(path, "null", "null", results))
          .serialize();
    }
    if (targetColumn == null) {
      targetColumn = "";
    }

    if (this.state.getState() != CSVState.State.LOADED) {
      return new SearchFailResponse(
              "error_datasource", new SearchResponseData(path, searchToken, targetColumn, results))
          .serialize();
    }

    try {
      CSVSearch<List<String>> search = new CSVSearch<>(this.state.getLoadResults(), searchToken);
      if (!targetColumn.isEmpty()) {
        int index = Integer.parseInt(targetColumn);
        if (index >= 0) {
          results = (search.searcher(index));
          return new SearchSuccessResponse(
                  new SearchResponseData(path, searchToken, targetColumn, results))
              .serialize();
        }
      } else {
        results = (search.searcher());
        return new SearchSuccessResponse(new SearchResponseData(path, searchToken, "none", results))
            .serialize();
      }
    } catch (NumberFormatException e) {
      results = new CSVSearch<>(this.state.getLoadResults(), searchToken).searcher(targetColumn);
      return new SearchSuccessResponse(
              new SearchResponseData(path, searchToken, targetColumn, results))
          .serialize();
    }
    return null;
  }
}
