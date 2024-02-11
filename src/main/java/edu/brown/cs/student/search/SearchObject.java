package edu.brown.cs.student.search;

import java.util.List;
import edu.brown.cs.student.strategy.CreatorFromRow;
import edu.brown.cs.student.strategy.FactoryFailureException;

public class SearchObject implements CreatorFromRow<List<String>> {
  public SearchObject() {}

  @Override
  public List<String> create(List<String> row) throws FactoryFailureException {
    return row;
  }
}
