package edu.brown.cs.student.main.ACSDatasource;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class ACSAPI implements ACSDatasource {

  private final HashMap<String, String> stateData;

  public ACSAPI() {
    this.stateData = this.mapify(deserialize(sendRequest()));
  }

  private HashMap<String, String> mapify(String[][] stateMap) {
    HashMap<String, String> ret = new HashMap<>();
    for (String[] stateCountyPair : stateMap) {
      ret.put(stateCountyPair[0].toLowerCase(), stateCountyPair[1].toLowerCase());
    }
    ret.put("*", "*");
    return ret;
  }

  private String[][] deserialize(String rawStateCode) {
    try {
      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<String[][]> adapter = moshi.adapter(String[][].class);
      String[][] data = adapter.fromJson(rawStateCode);
      return data;
    } catch (IOException e) {
      throw new RuntimeException(e);
      // fix this later
    }
  }

  private String sendRequest() {
    // Build a request to this BoredAPI. Try out this link in your browser, what do you see?
    // on participant number?
    try {
      HttpRequest censusApiRequest =
          HttpRequest.newBuilder()
              .uri(new URI("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*"))
              .GET()
              .build();
      // Send that API request then store the response in this variable. Note the generic type.
      HttpResponse<String> censusApiResponse =
          HttpClient.newBuilder()
              .build()
              .send(censusApiRequest, HttpResponse.BodyHandlers.ofString());
      return censusApiResponse.body();
    } catch (URISyntaxException | IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String[][] query(String state, String county) throws IllegalArgumentException {
    // need to error check this call
    if (state.isEmpty()) {
      throw new IllegalArgumentException("State is missing");
    }
    String stateID = this.stateData.getOrDefault(state.toLowerCase(), null);

    if (stateID == null) {
      throw new IllegalArgumentException("StateID is missing");
    }
    if (county.isEmpty()) {
      throw new IllegalArgumentException("County is missing");
    }
    String countyID = this.getCountyCode(stateID, county.toLowerCase());

    //
    // https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:*&in=state:06
    try {
      HttpRequest censusApiRequest =
          HttpRequest.newBuilder()
              .uri(
                  new URI(
                      "https://api.census.gov/data/2021/acs/acs1/subject/variables"
                          + "?get=NAME,S2802_C03_022E&"
                          + "for=county:"
                          + countyID
                          + "&in=state:"
                          + stateID))
              .GET()
              .build();

      // Send that API request then store the response in this variable. Note the generic type.
      HttpResponse<String> censusApiResponse =
          HttpClient.newBuilder()
              .build()
              .send(censusApiRequest, HttpResponse.BodyHandlers.ofString());

      return this.deserialize(censusApiResponse.body());
    } catch (URISyntaxException | IOException | InterruptedException e) {
      e.printStackTrace();
      throw new IllegalArgumentException();
    }
  }

  private String getCountyCode(String stateID, String county) {
    if (county.equals("*")) {
      return county;
    }
    try {
      HttpRequest censusApiRequest =
          HttpRequest.newBuilder()
              .uri(
                  new URI(
                      "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:"
                          + stateID))
              .GET()
              .build();

      // Send that API request then store the response in this variable. Note the generic type.
      HttpResponse<String> censusApiResponse =
          HttpClient.newBuilder()
              .build()
              .send(censusApiRequest, HttpResponse.BodyHandlers.ofString());

      String[][] stateData = this.deserialize(censusApiResponse.body());

      for (String[] countyStateThruple : stateData) {
        if (countyStateThruple[0].toLowerCase().contains(county)) {
          return countyStateThruple[2];
        }
      }
    } catch (URISyntaxException | IOException | InterruptedException e) {
      e.printStackTrace();
    }
    throw new IllegalArgumentException("County Not Found in State");
  }
}
