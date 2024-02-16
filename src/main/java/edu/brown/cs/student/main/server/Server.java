package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;
import spark.Spark;

/** The Main class of our project. This is where execution begins. */
public class Server {
  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) throws FileNotFoundException {
    new Server(args).run();
  }

  static CSVState currentState;
  static ACSDatasource currentAPI;

  private Server(String[] args) {}

  public void run() {
    // start up a port
    int port = 3443;
    Spark.port(port);
    // set up the shared dataclass
    currentState = new CSVState();
    try {
      currentAPI = new ACSAPIwithCache(200, 20, TimeUnit.MINUTES);
    } catch (Exception e) {
      System.out.println("api error");
    }

    // cors headers
    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    Spark.get("csvload", new LoadCSVHandler(currentState));
    Spark.get("csvsearch", new SearchCSVHandler(currentState));
    Spark.get("csvview", new ViewCSVHandler(currentState));
    Spark.get("broadband", new BroadbandHandler(currentAPI));
    Spark.init();
    Spark.awaitInitialization();

    System.out.println("Server started at http://localhost:" + port);
  }
}
