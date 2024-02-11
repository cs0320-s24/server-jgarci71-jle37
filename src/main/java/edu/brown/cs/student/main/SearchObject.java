package edu.brown.cs.student.main;

import java.util.List;

public class SearchObject implements CreatorFromRow<List<String>> {
  public SearchObject() {}

  @Override
  public List<String> create(List<String> row) throws FactoryFailureException {
    return row;
  }
}
