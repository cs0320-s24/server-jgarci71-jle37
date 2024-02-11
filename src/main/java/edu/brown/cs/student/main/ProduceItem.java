package edu.brown.cs.student.main;

import java.util.List;

public class ProduceItem implements CreatorFromRow<ProduceItem> {
  private String name;
  private String color;
  private String type;

  public ProduceItem(String n, String c, String t) {
    name = n;
    color = c;
    type = t;
  }

  @Override
  public ProduceItem create(List<String> row) throws FactoryFailureException {
    return new ProduceItem(row.get(0), row.get(1), row.get(2));
  }
}
