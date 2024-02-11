package src.main.java.edu.brown.cs.student.server;

import src.main.java.edu.brown.cs.student.search.CSVSearch;

import java.io.FileNotFoundException;
import java.util.List;

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

  private void run() throws FileNotFoundException {
    // dear student: you can remove this. you can remove anything. you're in cs32. you're free!
    System.out.println(
        "Your horoscope for this project:\n"
            + "Entrust in th.//e Strategy pattern, and it shall give thee the sovereignty to "
            + "decide and the dexterity to change direction in the realm of thy code.");

    CSVSearch<List<String>> mySearch = new CSVSearch<>();
    //    FileReader fileReader =
    //        new FileReader(
    //            "C:/Users/jenni/OneDrive/Desktop/CS320/csv-ailee37/data/local/frfhaisuf.csv");
    //    for (List<String> result :
    //        mySearch.searcher(
    //            new FileReader(
    //
    // "C:/Users/jenni/OneDrive/Desktop/CS320/csv-ailee37/data/malformed/malformed_signs.csv"),
    //            "Fruit")) {
    //      System.out.println(result);
//        }
  }
}
