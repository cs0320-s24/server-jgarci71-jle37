package edu.brown.cs.student.main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * CSVParser class creates a parser and parses through given readers. It utilizes a regex to split
 * the data into sections and returns as a list of a given type.
 *
 * @param <T>
 */
public class CSVParser<T> {
  List<T> results = new ArrayList<>();

  /**
   * Parser method that parses through given text/file and splits it using a regex
   *
   * @param myReader
   * @param createdObject
   * @return
   */
  public List<T> parser(Reader myReader, CreatorFromRow<T> createdObject) {
    try {
      BufferedReader buffReader = new BufferedReader(myReader);
      String currRow = buffReader.readLine();
      final Pattern regexSplitCSVRow =
          Pattern.compile(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*(?![^\\\"]*\\\"))");
      while (currRow != null) {
        String[] sections = regexSplitCSVRow.split(currRow);
        results.add(createdObject.create(List.of(sections)));
        currRow = buffReader.readLine();
      }
      buffReader.close();
    } catch (FileNotFoundException e) {
      System.err.println("Error: File Not Found");
      System.exit(0);
    } catch (IOException e) {
      System.err.println("Error: IO Exception; cannot continue");
      System.exit(0);
    } catch (FactoryFailureException e) {
      System.err.println("FactoryFailureException: file is malformed");
      System.exit(0);
    }
    return results;
  }
}
