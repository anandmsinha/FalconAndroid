package com.example.anand.falconproduction.adapters.components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anand.falconproduction.R;
import com.example.anand.falconproduction.adapters.ListViewWithHeaderAdapter;
import com.example.anand.falconproduction.interfaces.ListItem;

/**
 * Created by anand on 19/2/15.
 *
 * Header type item in list
 */
public class HeaderItem implements ListItem {

  private String displayName;

  public HeaderItem(String displayName) {
    this.displayName = displayName;
  }

  @Override
  public ListViewWithHeaderAdapter.RowType getViewType() {
    return ListViewWithHeaderAdapter.RowType.HEADER_ITEM;
  }

  @Override
  public View getView(LayoutInflater layoutInflater, View convertView, ViewGroup parent) {
    View view;
    view = (convertView == null) ? layoutInflater.inflate(R.layout.list_header_item, parent, false) : convertView;
    ((TextView) view.findViewById(R.id.main_text)).setText(displayName);
    return view;
  }

  @Override
  public void onClickHandler() {

  }
}
