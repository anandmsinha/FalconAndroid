package com.example.anand.falconproduction.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.anand.falconproduction.R;
import com.example.anand.falconproduction.models.BaFeed;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anand on 1/12/14.
 */
public class FalconRequestListAdapter extends BaseAdapter {

  LayoutInflater layoutInflater;
  List<BaFeed> mainFeed;

  public FalconRequestListAdapter(LayoutInflater layoutInflater, List<BaFeed> feeds) {
    this.layoutInflater = layoutInflater;
    if (feeds != null) {
      this.mainFeed = feeds;
    } else {
      this.mainFeed = new ArrayList<BaFeed>();
    }
  }

  @Override
  public int getCount() {
    return mainFeed.size();
  }

  @Override
  public BaFeed getItem(int position) {
    return mainFeed.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = layoutInflater.inflate(R.layout.feed_list_row, parent, false);
    }
    BaFeed baFeed = getItem(position);
    TextView mainId = (TextView) convertView.findViewById(R.id.main_action_id);
    mainId.setText(baFeed.getRequestDisplayId() + " : " + baFeed.getActionDisplayId());
    TextView mainActionTitle = (TextView) convertView.findViewById(R.id.main_action_title);
    mainActionTitle.setText(baFeed.getActionTitle());
    TextView mainRequestUser = (TextView) convertView.findViewById(R.id.main_request_user);
    mainRequestUser.setText("Last updated by :- " + baFeed.getUser());
    TextView mainRequestTime = (TextView) convertView.findViewById(R.id.main_request_time);
    mainRequestTime.setText("Last updated at :- " + baFeed.getTime());
    return convertView;
  }
}
