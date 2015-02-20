package com.example.anand.falconproduction.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.anand.falconproduction.interfaces.ListItem;

import java.util.List;

/**
 * Created by anand on 19/2/15.
 *
 * This adapter extends normal list adapter to allow headers for listview.
 */
public class ListViewWithHeaderAdapter extends ArrayAdapter<ListItem> {

  private LayoutInflater mLayoutInflater;

  public enum RowType {
    SEARCH_ITEM, HEADER_ITEM
  }

  public ListViewWithHeaderAdapter(Context context, List<ListItem> items) {
    super(context, 0, items);
    this.mLayoutInflater = LayoutInflater.from(context);
  }

  @Override
  public int getViewTypeCount() {
    return RowType.values().length;
  }

  @Override
  public int getItemViewType(int position) {
    return getItem(position).getViewType().ordinal();
  }

  public View getView(int position, View convertView, ViewGroup parent) {
    return getItem(position).getView(mLayoutInflater, convertView, parent);
  }
}
