package edu.brown.cs.student.main.search;

import edu.brown.cs.student.main.strategy.CreatorFromRow;
import edu.brown.cs.student.main.strategy.FactoryFailureException;
import java.util.List;

public class SearchObject implements CreatorFromRow<List<String>> {
  public SearchObject() {}

  @Override
  public List<String> create(List<String> row) throws FactoryFailureException {
    return row;
  }
}
