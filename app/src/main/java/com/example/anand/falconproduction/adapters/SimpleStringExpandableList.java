package com.example.anand.falconproduction.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * Created by anand on 28/11/14.
 */
public class SimpleStringExpandableList extends BaseExpandableListAdapter {

  LayoutInflater layoutInflater;

  public SimpleStringExpandableList(LayoutInflater inflater) {
    this.layoutInflater = inflater;
  }

  @Override
  public int getGroupCount() {
    return 4;
  }

  @Override
  public int getChildrenCount(int groupPosition) {
    return 2;
  }

  @Override
  public Object getGroup(int groupPosition) {
    return null;
  }

  @Override
  public Object getChild(int groupPosition, int childPosition) {
    return null;
  }

  @Override
  public long getGroupId(int groupPosition) {
    return groupPosition;
  }

  @Override
  public long getChildId(int groupPosition, int childPosition) {
    return groupPosition * 1024 + childPosition;
  }

  @Override
  public boolean hasStableIds() {
    return true;
  }

  @Override
  public View getGroupView(int groupPosition, boolean isExpanded,
                           View convertView, ViewGroup parent) {
    if (convertView == null) {
      int tmpLayoutElement = (getChildrenCount(groupPosition) == 0) ? android.R.layout.simple_selectable_list_item
        : android.R.layout.simple_expandable_list_item_1;
      convertView = layoutInflater.inflate(tmpLayoutElement, parent,
        false);
    }
    TextView tmpTextView = (TextView) convertView
      .findViewById(android.R.id.text1);
    tmpTextView.setText("Group");
    return convertView;
  }

  @Override
  public View getChildView(int groupPosition, int childPosition,
                           boolean isLastChild, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = layoutInflater.inflate(
        android.R.layout.simple_list_item_1, parent, false);
    }
    TextView tmpTextView = (TextView) convertView
      .findViewById(android.R.id.text1);
    tmpTextView.setText("child");
    return convertView;
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return true;
  }

}
