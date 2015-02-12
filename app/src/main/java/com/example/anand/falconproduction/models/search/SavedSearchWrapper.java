package com.example.anand.falconproduction.models.search;

import java.util.List;

/**
 * Created by anand on 11/2/15.
 * <p/>
 * Main wrapper for saved searches.
 */
public class SavedSearchWrapper {
  private List<SearchContainer> container;
  private long count;

  public List<SearchContainer> getContainer() {
    return container;
  }

  public void setContainer(List<SearchContainer> container) {
    this.container = container;
  }

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }
}
