package edu.brown.cs.student.main.ACSDatasource;

import java.util.concurrent.ExecutionException;

public interface ACSDatasource {

  public String[][] query(String state, String county) throws ExecutionException;
}
