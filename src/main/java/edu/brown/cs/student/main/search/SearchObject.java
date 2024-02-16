package edu.brown.cs.student.main.search;

import edu.brown.cs.student.main.strategy.CreatorFromRow;
import edu.brown.cs.student.main.strategy.FactoryFailureException;
import java.util.List;

/**
 * This is an instance of the CreatorFromRow<T> strategy for CSVsearch/CSVparser
 */
public class SearchObject implements CreatorFromRow<List<String>> {
  public SearchObject() {}

  /**
   * Creates a SearchObject
   * @param row
   * @return a List<String>
   * @throws FactoryFailureException
   */
  @Override
  public List<String> create(List<String> row) throws FactoryFailureException {
    return row;
  }
}
