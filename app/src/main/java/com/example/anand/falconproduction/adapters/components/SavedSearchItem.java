package com.example.anand.falconproduction.adapters.components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anand.falconproduction.R;
import com.example.anand.falconproduction.adapters.ListViewWithHeaderAdapter;
import com.example.anand.falconproduction.interfaces.ListItem;
import com.example.anand.falconproduction.models.search.SingleSavedSearch;

/**
 * Created by anand on 19/2/15.
 * <p/>
 * Saved search item in list.
 */
public class SavedSearchItem implements ListItem {

  SingleSavedSearch singleSavedSearch;

  public SavedSearchItem(SingleSavedSearch singleSavedSearch) {
    this.singleSavedSearch = singleSavedSearch;
  }

  public SingleSavedSearch getSingleSavedSearch() {
    return singleSavedSearch;
  }

  @Override
  public ListViewWithHeaderAdapter.RowType getViewType() {
    return ListViewWithHeaderAdapter.RowType.SEARCH_ITEM;
  }

  @Override
  public View getView(LayoutInflater layoutInflater, View convertView, ViewGroup parent) {
    View view;
    view = (convertView == null) ? layoutInflater.inflate(R.layout.list_search_item, parent, false) : convertView;
    TextView mainTextView = (TextView) view.findViewById(R.id.main_text);
    mainTextView.setText(singleSavedSearch.getName());
    return view;
  }

  @Override
  public void onClickHandler() {

  }
}
