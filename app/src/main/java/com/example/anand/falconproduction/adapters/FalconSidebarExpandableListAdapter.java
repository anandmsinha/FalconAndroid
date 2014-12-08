package com.example.anand.falconproduction.adapters;

import android.R;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.anand.falconproduction.models.BaGroups;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by anand on 27/11/14.
 *
 * This adapter is mainly used for populating the sidebar. It also
 * keeps track of ba details like id and permission.
 */
public class FalconSidebarExpandableListAdapter extends BaseExpandableListAdapter {

  public static final String tag = "sidebar";

  LayoutInflater inflater;
  List<BaGroups> baGroups;

  public FalconSidebarExpandableListAdapter(LayoutInflater inflater, List<BaGroups> groups) {
    Log.d(tag, "Sidebar constructor called");
    this.inflater = inflater;
    if (groups != null) {
      Log.d(tag, "groups passed to sidebar is not null " + groups.size());
      Iterator<BaGroups> baGroupsIterator = groups.iterator();
      while (baGroupsIterator.hasNext()) {
        BaGroups bag = baGroupsIterator.next();
        if (!bag.isYield()) {
          baGroupsIterator.remove();
        }
      }
    } else {
      Log.d(tag, "groups passed to sidebar is null.");
      groups = new ArrayList<BaGroups>();
    }
    baGroups = groups;
  }

  @Override
  public int getGroupCount() {
    Log.d(tag, "get group count called - " + baGroups.size());
    return baGroups.size();
  }

  @Override
  public int getChildrenCount(int groupPosition) {
    Log.d(tag, "get children count called - " + groupPosition);
    return baGroups.get(groupPosition).getChildrenCount();
  }

  @Override
  public BaGroups getGroup(int pos) {
    return baGroups.get(pos);
  }

  @Override
  public BaGroups getChild(int group, int child) {
    return baGroups.get(group).getItems().get(child);
  }

  @Override
  public long getGroupId(int group) {
    return group;
  }

  @Override
  public long getChildId(int group, int child) {
    return group * 1024 + child;
  }

  @Override
  public boolean hasStableIds() {
    return true;
  }

  @Override
  public View getGroupView(int group, boolean isExpanded, View convertView, ViewGroup parent) {
    Log.d(tag, "Get group view called.");
    if (convertView == null) {
      Log.d(tag, "convert view is null");
      /*int tmpLayout = (getChildrenCount(group) == 0) ? R.layout.simple_selectable_list_item
        : R.layout.simple_expandable_list_item_1;*/
      convertView = inflater.inflate(R.layout.simple_expandable_list_item_1, parent, false);
    }
    Log.d(tag, "convert view is set now");
    TextView tmpTextView = (TextView) convertView.findViewById(R.id.text1);
    tmpTextView.setText("random string");
    return convertView;
  }

  @Override
  public View getChildView(int group, int child, boolean isLastChild, View convertView, ViewGroup parent) {
    // if the child is ba group we have to create a
    //BaGroups tmpBaGroup = baGroups.get(group).getItems().get(child);
    if (convertView == null) {
      convertView = inflater.inflate(
        android.R.layout.simple_list_item_1, parent, false);
    }
    TextView tmpTextView = (TextView) convertView
      .findViewById(R.id.text1);
    tmpTextView.setText("random string");
    return convertView;
    /*if (tmpBaGroup.isGroup()) {
      convertView = inflater.inflate(R.layout.expandable_list_content, parent, false);
      ExpandableListView expandableListView =
        (ExpandableListView) convertView.findViewById(R.id.list);
      expandableListView.setAdapter(new FalconSidebarExpandableListAdapter(inflater, tmpBaGroup.getItems()));
    } else {
      convertView = inflater.inflate(R.layout.simple_list_item_1, parent, false);
      TextView tmpTextView = (TextView) convertView.findViewById(R.id.text1);
      tmpTextView.setText(tmpBaGroup.toString());
    }*/
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return true;
  }
}
