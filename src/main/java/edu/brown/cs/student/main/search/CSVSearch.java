package edu.brown.cs.student.main.search;

import edu.brown.cs.student.main.strategy.CreatorFromRow;
import edu.brown.cs.student.main.strategy.FactoryFailureException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVSearch<T> {
  private boolean useHeader;
  private final String searchToken;
  CreatorFromRow<List<String>> myObject;

  private List<List<String>> loadResults;

  public CSVSearch(List<List<String>> results, String token) {
    loadResults = results;
    searchToken = token;
    myObject = new SearchObject();
  }

  /**
   * constructor/method for search (used when header is unspecified)
   *
   * @return
   */
  public List<List<String>> searcher() throws IOException, FactoryFailureException {
    useHeader = false;
    return getLists(loadResults, -1);
  }

  /**
   * alternative constructor/method for search using String header
   *
   * @param headerName
   * @return
   */
  public List<List<String>> searcher(String headerName)
      throws IOException, FactoryFailureException {
    useHeader = true;
    myObject = new SearchObject();
    List<List<String>> parsedList = loadResults;
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
   * @param headerIndex
   * @return
   */
  public List<List<String>> searcher(int headerIndex) throws IOException, FactoryFailureException {
    useHeader = true;
    myObject = new SearchObject();
    return getLists(loadResults, headerIndex);
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