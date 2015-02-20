package com.example.anand.falconproduction.interfaces;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anand.falconproduction.adapters.ListViewWithHeaderAdapter;

/**
 * Created by anand on 19/2/15.
 *
 * Item for ListVIewWithHeaderAdapter
 */
public interface ListItem {
  ListViewWithHeaderAdapter.RowType getViewType();
  View getView(LayoutInflater layoutInflater, View convertView, ViewGroup parent);
  void onClickHandler();
}
