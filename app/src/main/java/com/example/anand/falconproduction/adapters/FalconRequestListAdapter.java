package com.example.anand.falconproduction.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.anand.falconproduction.R;
import com.example.anand.falconproduction.interfaces.GetBaFeed;
import com.example.anand.falconproduction.models.BaFeed;
import com.example.anand.falconproduction.utility.CommonRequestsUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anand on 1/12/14.
 */
public class FalconRequestListAdapter extends BaseAdapter implements GetBaFeed {

  LayoutInflater layoutInflater;
  List<BaFeed> mainFeed;
  Context context;
  long baId;

  public FalconRequestListAdapter(Context activity, long baId, LayoutInflater layoutInflater, List<BaFeed> feeds) {
    this.layoutInflater = layoutInflater;
    this.context = activity;
    this.baId = baId;
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
    if (position == getCount() - 3) {
      loadMore();
    }
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

  private void loadMore() {
    CommonRequestsUtility.getBaFeed(this.context, this, this.baId, getCount() + 10);
  }

  @Override
  public void processBaFeed(long baId, List<BaFeed> feeds) {
    this.mainFeed.addAll(feeds);
    notifyDataSetChanged();
  }
}
