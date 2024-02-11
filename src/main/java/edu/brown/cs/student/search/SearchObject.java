package src.main.java.edu.brown.cs.student.search;

import src.main.java.edu.brown.cs.student.strategy.CreatorFromRow;
import src.main.java.edu.brown.cs.student.strategy.FactoryFailureException;

import java.util.List;

public class SearchObject implements CreatorFromRow<List<String>> {
  public SearchObject() {}

  @Override
  public List<String> create(List<String> row) throws FactoryFailureException {
    return row;
  }
}
