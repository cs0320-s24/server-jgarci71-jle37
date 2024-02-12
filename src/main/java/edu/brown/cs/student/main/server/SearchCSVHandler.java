package edu.brown.cs.student.main.server;
import edu.brown.cs.student.main.search.CSVSearch;
import edu.brown.cs.student.main.server.SearchViewSuccessResponse.SearchViewResponseData;
import java.io.IOException;
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

    String filePath = request.queryParams("file");
    String path = "data/" + filePath;
    String searchToken = request.queryParams("token");
    String targetColumn = request.queryParams("column");

    try {
      CSVSearch<List<String>> search = new CSVSearch<>(this.state.getLoadResults(), searchToken);
      if (!targetColumn.isEmpty()) {
        int index = Integer.parseInt(targetColumn);
        if (index >= 0) {
          results = (search.searcher(index));
        }
      } else {
        results = (search.searcher());
      }
      this.state.fileSuccess();
      this.state.setFilePath(path);
      return new SearchViewSuccessResponse(new SearchViewResponseData(path, results)).serialize();
    } catch (IOException e) {
      return new SearchViewFailResponse(new SearchViewResponseData(path, results)).serialize();
    } catch (NumberFormatException e) {
      this.state.setResults(
          new CSVSearch<>(this.state.getLoadResults(), searchToken).searcher(targetColumn));
    }
    return results;
  }
}
