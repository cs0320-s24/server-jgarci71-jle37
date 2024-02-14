package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class ACSAPI {

  private final String[][] stateData;

  public ACSAPI() throws URISyntaxException, IOException, InterruptedException {
    this.stateData = deserialize(sendRequest());
    System.out.println(stateData);
  }

  public static String[][] deserialize(String rawStateCode) {
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

  private String sendRequest() throws URISyntaxException, IOException, InterruptedException {
    // Build a request to this BoredAPI. Try out this link in your browser, what do you see?
    // TODO 1: Looking at the documentation, how can we add to the URI to query based
    // on participant number?
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

    // What's the difference between these two lines? Why do we return the body? What is useful from
    // the raw response (hint: how can we use the status of response)?
    System.out.println(censusApiResponse);
    System.out.println(censusApiResponse.body());

    return censusApiResponse.body();
  }
}
