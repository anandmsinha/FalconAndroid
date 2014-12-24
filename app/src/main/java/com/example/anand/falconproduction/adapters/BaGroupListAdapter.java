package com.example.anand.falconproduction.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.anand.falconproduction.models.BaGroups;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by anand on 28/11/14.
 *
 * Main left menu bar
 */
public class BaGroupListAdapter extends BaseAdapter {

  List<BaGroups> groups = new ArrayList<BaGroups>();
  LayoutInflater inflater;
  /**
   * This variable contains only ba which are yieldable
   */
  List<BaGroups> finalList = new ArrayList<BaGroups>();

  public BaGroupListAdapter(LayoutInflater inflater, List<BaGroups> entries) {
    this.inflater = inflater;
    if (entries != null) {
      this.groups.addAll(entries);
    }
    filterGroups();
  }

  @Override
  public int getCount() {
    return finalList.size();
  }

  @Override
  public BaGroups getItem(int position) {
    return finalList.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
    }

    BaGroups tmpBaGroup = getItem(position);
    TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
    textView.setText(tmpBaGroup.getDisplayName());
    return convertView;
  }

  private void filterGroups() {
    Iterator<BaGroups> it = groups.iterator();
    finalList.clear();
    while (it.hasNext()) {
      BaGroups current = it.next();
      if (current.isYield()) {
        if (current.isGroup()) {
          finalList.addAll(current.getAllBaInGroup());
        } else {
          finalList.add(current);
        }
      } else {
        it.remove();
      }
    }
  }
}
