package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import java.io.FileNotFoundException;
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

  private Server(String[] args) {}

  public void run() {
    // start up a port
    int port = 3443;
    Spark.port(port);
    // set up the shared dataclass
    currentState = new CSVState();

    // cors headers
    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });
    // set up the 4 different endpoints and handlers
    System.out.println("something is going wrong down here");
    Spark.get("csvload", new LoadCSVHandler(currentState));
    Spark.get("csvsearch", new SearchCSVHandler(currentState));
    Spark.get("csvview", new ViewCSVHandler(currentState));
    System.out.println("or down here");
    Spark.init();
    System.out.println("or here");
    Spark.awaitInitialization();
    System.out.println("here");
    System.out.println("Server started at http://localhost:" + port);
  }
}
