package edu.brown.cs.student.server;

import java.io.FileNotFoundException;

import spark.Spark;

import static spark.Spark.after;

/** The Main class of our project. This is where execution begins. */
public final class Server {
  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) throws FileNotFoundException {
    new Server(args).run();
  }

  private Server(String[] args) {}

  private void run() {
    // start up a port
    int port = 3443;
    Spark.port(port);
    // set up the shared dataclass
    CSVState currentState = new CSVState();

    // cors headers
    after(
            (request, response) -> {
              response.header("Acess-Control-Allow-Origin", "*");
              response.header("Access-Control-Allow-Methods", "*");
            }
    );
    // set up the 4 different endpoints and handlers
    //    Spark.get("csvload", )
    Spark.init();
    Spark.awaitInitialization();
    // printing out instructions
    System.out.println("Server started at http://localhose:" + port);
  }
}
