package edu.brown.cs.student.main.mocks;

import edu.brown.cs.student.main.ACSDatasource.ACSDatasource;
import java.util.concurrent.ExecutionException;

public class MockACSAPI implements ACSDatasource {

  private String[][] constantData;

  public MockACSAPI(String[][] data) {
    this.constantData = data;
  }

  @Override
  public String[][] query(String state, String county) throws ExecutionException {
    return this.constantData;
  }
}
