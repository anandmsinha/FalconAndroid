package com.example.anand.falconproduction.models.search;

import java.util.List;

/**
 * Created by anand on 11/2/15.
 * <p/>
 * Main saved search container
 */
public class SearchContainer {

  private long baId;

  private String displayName;

  private List<SingleSavedSearch> searches;

  public long getBaId() {
    return baId;
  }

  public void setBaId(long baId) {
    this.baId = baId;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public List<SingleSavedSearch> getSearches() {
    return searches;
  }

  public void setSearches(List<SingleSavedSearch> searches) {
    this.searches = searches;
  }
}
