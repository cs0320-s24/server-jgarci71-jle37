package edu.brown.cs.student.main.ACSDatasource;

import java.util.concurrent.ExecutionException;

/**
 * The ACSDatasource. to be compatible with the Broadband handler or the ACSAPIWithCache, it should implement
 * the query method.
 */
public interface ACSDatasource {

  public String[][] query(String state, String county) throws ExecutionException;
}
