package edu.brown.cs.student.main;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.ACSDatasource.ACSAPI;
import edu.brown.cs.student.main.ACSDatasource.ACSDatasource;
import edu.brown.cs.student.main.broadband.BroadbandHandler;
import edu.brown.cs.student.main.broadband.BroadbandSuccessResponse;
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
 * Tests the BroadbandHandler with calls to the ACSAPI
 */
public class TestBroadbandHandlerAPI {

  /**
   * Sets the spark port before anything in the test suite is run
   */
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

  /**
   * Initializes the ACSAPI data
   */
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
    ACSDatasource apiSource = new ACSAPI();
    Spark.get("/broadband", new BroadbandHandler(apiSource));
    Spark.awaitInitialization(); // don't continue until the server is listening
  }

  /**
   * Clears maps after tests are done running
   */
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

  /**
   * Tests a successful broadband call
   * @throws IOException
   */
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
    assertEquals("89.9", response.responseMap().percentage());
    assertEquals("California", response.responseMap().state().trim());

    clientConnection.disconnect();
  }

  /**
   * Tests a failure for when county is null
   * @throws IOException
   */
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

  /**
   * Tests a failure for when state is null
   * @throws IOException
   */
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

  /**
   * Tests a failure for when no county is specified
   * @throws IOException
   */
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

  /**
   * Tests a failure for when no state is specified
   * @throws IOException
   */
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

  /**
   * Tests a failure for when a state does not exist
   * @throws IOException
   */
  @Test
  public void testBroadbandStateDoesNotExist() throws IOException {

    // ONE IMPORTANT THING DON'T USE SPACES FOR THE API CALLS USE %20
    HttpURLConnection clientConnection = tryRequest("broadband?state=China&county=Los%20Angeles");
    assertEquals(200, clientConnection.getResponseCode());
    Moshi moshi = new Moshi.Builder().build();
    BroadbandSuccessResponse response =
        moshi
            .adapter(BroadbandSuccessResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    assertEquals("error_bad_request", response.response_type());

    clientConnection.disconnect();
  }

  /**
   * Tests a failure for when the given county is not found in the state
   * @throws IOException
   */
  @Test
  public void testBroadbandCountyNotInState() throws IOException {

    // ONE IMPORTANT THING DON'T USE SPACES FOR THE API CALLS USE %20
    HttpURLConnection clientConnection = tryRequest("broadband?state=California&county=Gwinnett");
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
