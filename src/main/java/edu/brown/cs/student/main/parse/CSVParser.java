package edu.brown.cs.student.main.parse;

import edu.brown.cs.student.main.strategy.CreatorFromRow;
import edu.brown.cs.student.main.strategy.FactoryFailureException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * This is my generic CSV Parser.
 *
 * @param <T> The object to convert every row in the CSV into.
 */
public class CSVParser<T> {

  private List<T> results = new LinkedList<>();
  static final Pattern regexSplitCSVRow =
      Pattern.compile(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*(?![^\\\"]*\\\"))");

  /**
   * Creates a new CSV Parser.
   *
   * @param reader Generic reader. Wrapped in a BufferedReader for access to nextLine();
   * @param strategy of type CreatorFromRow, implements the create method that will convert the rows
   *     into T's
   * @throws IOException exception thrown while reading the file
   * @throws FactoryFailureException exception thrown by the strategy passed in
   */
  public CSVParser(Reader reader, CreatorFromRow<T> strategy)
      throws IOException, FactoryFailureException, IllegalArgumentException {
    this.parse(reader, strategy);
  }

  /**
   * Runs a loop that parses through the data using the reader.
   *
   * @param reader Generic Reader.
   * @param strategy Strategy used to convert rows into T's
   * @throws IllegalArgumentException: Thrown for null inputs
   * @throws IOException: Thrown when parsing the file encounters an error
   * @throws FactoryFailureException: Thrown by the strategy's create() method
   */
  private void parse(Reader reader, CreatorFromRow<T> strategy)
      throws IllegalArgumentException, IOException, FactoryFailureException {

    if (reader == null || strategy == null) {
      throw new IllegalArgumentException("Inputs can not be null");
    }

    BufferedReader myReader = new BufferedReader(reader);
    String row = myReader.readLine();

    while (row != null) {
      List<String> rowSplit = List.of(regexSplitCSVRow.split(row));
      this.results.add(strategy.create(rowSplit));
      row = myReader.readLine();
    }
  }

  public List<T> getResults() {
    return this.results;
  }
}
