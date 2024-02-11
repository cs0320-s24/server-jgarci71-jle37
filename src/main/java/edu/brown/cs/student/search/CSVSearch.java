package src.main.java.edu.brown.cs.student.search;

import src.main.java.edu.brown.cs.student.strategy.CreatorFromRow;
import src.main.java.edu.brown.cs.student.strategy.FactoryFailureException;
import src.main.java.edu.brown.cs.student.parse.CSVParser;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CSVSearch<T> {
  private boolean useHeader;
  private String searchToken;
  CreatorFromRow<List<String>> myObject;

  /**
   * constructor/method for search (used when header is unspecified)
   *
   * @param reader
   * @param token
   * @return
   */
  public List<List<String>> searcher(Reader reader, String token) throws IOException, FactoryFailureException {
    useHeader = false;
    searchToken = token;
    myObject = new SearchObject();
    return getLists(getParsed(reader), -1);
  }

  /**
   * alternative constructor/method for search using String header
   *
   * @param reader
   * @param token
   * @param headerName
   * @return
   */
  public List<List<String>> searcher(Reader reader, String token, String headerName) throws IOException, FactoryFailureException {
    useHeader = true;
    searchToken = token;
    myObject = new SearchObject();
    List<List<String>> parsedList = getParsed(reader);
    for (int i = 0; i < parsedList.get(0).size(); i++) {
      if (parsedList.get(0).get(i).equalsIgnoreCase(headerName)) {
        return getLists(parsedList, i);
      }
    }
    return new ArrayList<>();
  }

  /**
   * alternative constructor/method for search using column index
   *
   * @param reader
   * @param token
   * @param headerIndex
   * @return
   */
  public List<List<String>> searcher(Reader reader, String token, int headerIndex) throws IOException, FactoryFailureException {
    useHeader = true;
    searchToken = token;
    myObject = new SearchObject();
    return getLists(getParsed(reader), headerIndex);
  }

  /**
   * helper method that calls parser in the CSVParser class
   *
   * @param reader
   * @return
   */
  public List<List<String>> getParsed(Reader reader) throws IOException, FactoryFailureException {
    CSVParser<List<String>> myParse = new CSVParser<>(reader, myObject);
    return myParse.getResults();
  }

  /**
   * Helper method that searches through results of parsing to find matches and returns final
   * results
   *
   * @param rows
   * @param columnIndex
   * @return
   */
  public List<List<String>> getLists(List<List<String>> rows, int columnIndex) {
    List<List<String>> results = new ArrayList<>();
    List<String> prevRow = rows.get(0);
    if (columnIndex == -1) {
      for (int i = 0; i < rows.size(); i++) {
        for (int j = 0; j < rows.get(0).size(); j++) {
          if (rows.get(i).get(j).toLowerCase().contains(searchToken.toLowerCase())
              && !results.contains(rows.get(i))) {
            results.add(rows.get(i));
          }
        }
      }
    } else {
      for (List<String> row : rows) {
        if (row.get(columnIndex).toLowerCase().contains(searchToken.toLowerCase())
            && !results.contains(row)) {
          results.add(row);
        }
      }
    }
    return results;
  }
}