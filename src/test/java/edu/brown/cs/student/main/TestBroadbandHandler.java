package edu.brown.cs.student.main;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.ACSDatasource.ACSDatasource;
import edu.brown.cs.student.main.broadband.BroadbandHandler;
import edu.brown.cs.student.main.broadband.BroadbandSuccessResponse;
import edu.brown.cs.student.main.mocks.MockACSAPI;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

/**
 * An INTEGRATION TEST differs from a UNIT TEST in that it's testing a combination of code units and
 * their combined behavior.
 *
 * <p>Test our API server: send real web requests to our server as it is running. Note that for
 * these, we prefer to avoid sending many real API requests to the NWS, and use "mocking" to avoid
 * it. (There are many other reasons to use mock data here. What are they?)
 *
 * <p>In short, there are two new techniques demonstrated here: writing tests that send fake API
 * requests; and testing with mock data / mock objects.
 */
public class TestBroadbandHandler {

  @BeforeClass
  public static void setupOnce() {
    // Pick an arbitrary free port
    Spark.port(0);
    // Eliminate logger spam in console for test suite
    Logger.getLogger("").setLevel(Level.WARNING); // empty name = root
  }

  // Helping Moshi serialize Json responses; see the gearup for more info.
  // NOTE WELL: THE TYPES GIVEN HERE WOULD VARY ANYTIME THE RESPONSE TYPE VARIES
  // We are testing an API that returns Map<String, Object>
  // It would be different if the response was, e.g., List<List<String>>.
  private JsonAdapter<Map<String, Object>> adapter;
  private JsonAdapter<BroadbandSuccessResponse.BroadbandResponseData> broadbandDataAdapter;

  /** Things to test for: well-formed api call ** missing */
  @BeforeEach
  public void setup() {
    // Re-initialize parser, state, etc. for every test method

    // Use *MOCKED* data when in this test environment.
    // Notice that the WeatherHandler code doesn't need to care whether it has
    // "real" data or "fake" data. Good separation of concerns enables better testing.

    // Mock data needs to take on the following format:
    // [["NAME","S2802_C03_022E","state","county"],
    // ["Kings County, California","83.5","06","031"]]

    String[][] data = {
      {"NAME", "S2802_C03_022E", "state", "county"},
      {"Los Angeles County, California", "79.7", "06", "12"}
    };
    ACSDatasource mockedSource = new MockACSAPI(data);
    Spark.get("/broadband", new BroadbandHandler(mockedSource));
    Spark.awaitInitialization(); // don't continue until the server is listening
  }

  @AfterEach
  public void tearDown() {
    // Gracefully stop Spark listening on both endpoints
    Spark.unmap("/broadband");
    Spark.awaitStop(); // don't proceed until the server is stopped
  }

  /**
   * Helper to start a connection to a specific API endpoint/params
   *
   * <p>The "throws" clause doesn't matter below -- JUnit will fail if an exception is thrown that
   * hasn't been declared as a parameter to @Test.
   *
   * @param apiCall the call string, including endpoint (Note: this would be better if it had more
   *     structure!)
   * @return the connection for the given URL, just after connecting
   * @throws IOException if the connection fails for some reason
   */
  private HttpURLConnection tryRequest(String apiCall) throws IOException {
    // Configure the connection (but don't actually send a request yet)
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    // The request body contains a Json object
    clientConnection.setRequestProperty("Content-Type", "application/json");
    // We're expecting a Json object in the response body
    clientConnection.setRequestProperty("Accept", "application/json");

    clientConnection.connect();
    return clientConnection;
  }

  @Test
  public void testBroadbandRequestSuccess() throws IOException {

    // ONE IMPORTANT THING DON'T USE SPACES FOR THE API CALLS USE %20
    HttpURLConnection clientConnection =
        tryRequest("broadband?state=California&county=Los%20Angeles");
    assertEquals(200, clientConnection.getResponseCode());
    Moshi moshi = new Moshi.Builder().build();
    BroadbandSuccessResponse response =
        moshi
            .adapter(BroadbandSuccessResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    assertEquals("success", response.response_type());

    // the response map should ALWAYS return this because our BroadBand Handler is using
    // the mock data passed in during setup
    assertEquals("Los Angeles County", response.responseMap().county());
    assertEquals("79.7", response.responseMap().percentage());
    assertEquals("California", response.responseMap().state().trim());

    clientConnection.disconnect();
  }

  @Test
  public void testBroadbandNoCounty() throws IOException {

    // ONE IMPORTANT THING DON'T USE SPACES FOR THE API CALLS USE %20
    HttpURLConnection clientConnection = tryRequest("broadband?state=California");
    assertEquals(200, clientConnection.getResponseCode());
    Moshi moshi = new Moshi.Builder().build();
    BroadbandSuccessResponse response =
        moshi
            .adapter(BroadbandSuccessResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    assertEquals("error_bad_request", response.response_type());

    clientConnection.disconnect();
  }

  @Test
  public void testBroadbandNoState() throws IOException {

    // ONE IMPORTANT THING DON'T USE SPACES FOR THE API CALLS USE %20
    HttpURLConnection clientConnection = tryRequest("broadband?county=Los%20Angeles");
    assertEquals(200, clientConnection.getResponseCode());
    Moshi moshi = new Moshi.Builder().build();
    BroadbandSuccessResponse response =
        moshi
            .adapter(BroadbandSuccessResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    assertEquals("error_bad_request", response.response_type());

    clientConnection.disconnect();
  }

  @Test
  public void testBroadbandEmptyCounty() throws IOException {

    // ONE IMPORTANT THING DON'T USE SPACES FOR THE API CALLS USE %20
    HttpURLConnection clientConnection = tryRequest("broadband?state=California&county=");
    assertEquals(200, clientConnection.getResponseCode());
    Moshi moshi = new Moshi.Builder().build();
    BroadbandSuccessResponse response =
        moshi
            .adapter(BroadbandSuccessResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    assertEquals("error_bad_request", response.response_type());

    clientConnection.disconnect();
  }

  @Test
  public void testBroadbandEmptyState() throws IOException {

    // ONE IMPORTANT THING DON'T USE SPACES FOR THE API CALLS USE %20
    HttpURLConnection clientConnection = tryRequest("broadband?state=");
    assertEquals(200, clientConnection.getResponseCode());
    Moshi moshi = new Moshi.Builder().build();
    BroadbandSuccessResponse response =
        moshi
            .adapter(BroadbandSuccessResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    assertEquals("error_bad_request", response.response_type());

    clientConnection.disconnect();
  }
}
