package edu.brown.cs.student.main.load;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.parse.CSVParser;
import edu.brown.cs.student.main.search.SearchObject;
import edu.brown.cs.student.main.server.CSVState;
import java.io.FileReader;
import java.util.List;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * This is the handler for the "/csvload" endpoint. It takes in a filepath to parse/load.
 */
public class LoadCSVHandler implements Route {

  private final CSVState state;

  public LoadCSVHandler(CSVState state) {
    this.state = state;
  }

  public record LoadResponseData(String filepath) {}

  /**
   * Represents a successful response for a csvload endpoint call. Returns a success response type
   * with the expected data.
   */
  public record LoadSuccessResponse(String response_type, LoadResponseData responseMap) {
    public LoadSuccessResponse(LoadResponseData data) {
      this("success", data);
    }

    String serialize() {
      try {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<LoadSuccessResponse> adapter = moshi.adapter(LoadSuccessResponse.class);
        return adapter.toJson(this);
      } catch (Exception e) {
        e.printStackTrace();
        throw e;
      }
    }
  }

  /**
   * Represents a failure response for a csvload endpoint call.
   */
  public record LoadFailResponse(String response_type, LoadResponseData responseMap) {
    public LoadFailResponse(LoadResponseData data) {
      this("error", data);
    }

    String serialize() {
      try {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<LoadFailResponse> adapter = moshi.adapter(LoadFailResponse.class);
        return adapter.toJson(this);
      } catch (Exception e) {
        e.printStackTrace();
        throw e;
      }
    }
  }

  /**
   * Requests a filepath query to load from user adn calls on parse
   *
   * @param request
   * @param response
   * @return either loadfailresponse or loadsuccessresponse
   * @throws Exception
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {
    // here we will call our parse,

    String filePath = request.queryParams("file");
    //        LoadResponseData data = new LoadResponseData(filePath);
    //        return new LoadSuccessResponse(data).serialize();

    if (filePath == null) {
      LoadResponseData data = new LoadResponseData("null");
      return new LoadFailResponse("error_bad_request", data).serialize();
    }

    String path = "data/" + filePath;
    if (path.contains("..")) {
      LoadResponseData data = new LoadResponseData(path);
      return new LoadFailResponse("error_bad_request", data).serialize();
    }

    try {
      FileReader reader = new FileReader(path);
      CSVParser<List<String>> parser = new CSVParser<>(reader, new SearchObject());
      this.state.fileSuccess();
      this.state.setResults(parser.getResults());
      this.state.setFilePath(path);
      return new LoadSuccessResponse(new LoadResponseData(path)).serialize();
    } catch (Exception e) {
      return new LoadFailResponse("error_datasource", new LoadResponseData(path)).serialize();
    }
  }
}
