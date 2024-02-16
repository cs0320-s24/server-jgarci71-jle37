package edu.brown.cs.student.main.search;

import edu.brown.cs.student.main.search.SearchSuccessResponse.SearchResponseData;
import edu.brown.cs.student.main.server.CSVState;
import java.util.ArrayList;
import java.util.List;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * This is the handler for the "/csvsearch" endpoint. It takes in a filepath, search token,
 * and column header to perform a search with given the expected parameters.
 */
public class SearchCSVHandler implements Route {
  private final CSVState state;

  public SearchCSVHandler(CSVState state) {
    this.state = state;
  }

  /**
   * Takes in queries and calls on CSVsearch to retrieve a list of rows
   *
   * @param request
   * @param response
   * @return a searchsuccessresponse or searchfailresponse
   * @throws Exception
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {
    // here we will call our parse,
    List<List<String>> results = new ArrayList<>();

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
