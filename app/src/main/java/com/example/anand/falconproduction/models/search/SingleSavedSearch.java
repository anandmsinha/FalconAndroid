package com.example.anand.falconproduction.models.search;

/**
 * Created by anand on 11/2/15.
 *
 * Single saved search entry.
 */
public class SingleSavedSearch {

  private long id;

  private long baId;

  private String name;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getBaId() {
    return baId;
  }

  public void setBaId(long baId) {
    this.baId = baId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
