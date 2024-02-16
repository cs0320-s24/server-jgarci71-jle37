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

/**
 * This is the ACSAPI. It's query works by making actual network calls to the census API and
 * reformatting the desired results
 */
public class ACSAPI implements ACSDatasource {

  // loads in the state to state ID mappings on initialization
  private final HashMap<String, String> stateData;

  // on construction, populate the state IDs
  public ACSAPI() {
    this.stateData = this.mapify(this.deserialize(this.fetchStates()));
  }

  /**
   * This method will take in an Array of [State, ID] pairs and populates a hashmap.
   *
   * @param stateMap
   * @return the hashMap.
   */
  private HashMap<String, String> mapify(String[][] stateMap) {
    HashMap<String, String> ret = new HashMap<>();
    for (String[] stateCountyPair : stateMap) {
      ret.put(stateCountyPair[0].toLowerCase(), stateCountyPair[1].toLowerCase());
    }
    return ret;
  }

  /**
   * This method will take in a JSON response of a nested list and deserialize it into a 2D Array
   *
   * @param rawStateCode
   * @return
   */
  private String[][] deserialize(String rawStateCode) {
    try {
      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<String[][]> adapter = moshi.adapter(String[][].class);
      String[][] data = adapter.fromJson(rawStateCode);
      return data;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * This method makes a request to the State to State IDs from the Census API.
   *
   * @return
   */
  private String fetchStates() {
    try {
      HttpRequest censusApiRequest =
          HttpRequest.newBuilder()
              .uri(new URI("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*"))
              .GET()
              .build();
      HttpResponse<String> censusApiResponse =
          HttpClient.newBuilder()
              .build()
              .send(censusApiRequest, HttpResponse.BodyHandlers.ofString());
      return censusApiResponse.body();
    } catch (URISyntaxException | IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * This is the query method. It takes in a state and county from the BroadBandHandler and returns
   * the results of the API in the form of a 2D Array. The results appear as follow: [[Location,
   * BroadbandPercentage, StateCode, CountyCode], [/string/, /string/, /string/, /string/] ]
   *
   * @param state State name
   * @param county County Name
   * @return
   * @throws IllegalArgumentException
   */
  @Override
  public String[][] query(String state, String county) throws IllegalArgumentException {
    // Can't search an empty State
    if (state.isEmpty()) {
      throw new IllegalArgumentException("State is missing");
    }
    // retrieve the state ID
    String stateID = this.stateData.getOrDefault(state.toLowerCase(), null);

    // state does not exist
    if (stateID == null) {
      throw new IllegalArgumentException("StateID is missing");
    }
    // can not search without a county
    if (county.isEmpty()) {
      throw new IllegalArgumentException("County is missing");
    }
    // this throws an error if the county is not found
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

  /**
   * Given a stateID and a county Name, find the county code using a call to the CensusAPI.
   *
   * @param stateID id retrieved from the census
   * @param county county name
   * @return county code
   */
  private String getCountyCode(String stateID, String county) {
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
    // if county is not found throw an exception
    throw new IllegalArgumentException("County Not Found in State");
  }
}
