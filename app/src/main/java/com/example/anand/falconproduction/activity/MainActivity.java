package com.example.anand.falconproduction.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anand.falconproduction.R;
import com.example.anand.falconproduction.interfaces.ProcessAfterDrawer;
import com.example.anand.falconproduction.models.BaFeed;
import com.example.anand.falconproduction.models.BaGroups;
import com.example.anand.falconproduction.utility.ApplicationConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Main activity class here we show bafeed to the user.
 */
public class MainActivity extends BaseDrawerActivity implements ProcessAfterDrawer {

  private static final String tag = "main_activity";
  Future<JsonObject> loading;
  ArrayAdapter<BaFeed> feedsAdapter;
  long totalFeedsCount = 0;
  BaGroups feedBa;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main, this);
  }

  @Override
  public void fillDetails(int groupsSize) {
    if (groupsSize != 0) {
      feedBa = mainAdapter.getItem(groupId);
      // initialize the adapter
      feedsAdapter = new ArrayAdapter<BaFeed>(this, 0) {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
          if (convertView == null) {
            convertView = getLayoutInflater().inflate(R.layout.feed_list_row, parent, false);
          }
          if (position >= getCount() - 2) {
            load();
          }
          BaFeed currentFeed = getItem(position);
          TextView mainId = (TextView) convertView.findViewById(R.id.main_action_id);
          mainId.setText(currentFeed.getRequestDisplayId() + " : " + currentFeed.getActionDisplayId());
          TextView mainActionTitle = (TextView) convertView.findViewById(R.id.main_action_title);
          mainActionTitle.setText(currentFeed.getActionTitle());
          TextView mainRequestUser = (TextView) convertView.findViewById(R.id.main_request_user);
          mainRequestUser.setText("Last updated by :- " + currentFeed.getUser());
          TextView mainRequestTime = (TextView) convertView.findViewById(R.id.main_request_time);
          mainRequestTime.setText("Last updated at :- " + currentFeed.getTime());
          return convertView;
        }
      };
      ListView feedsListView = (ListView) findViewById(R.id.main_list_content);
      feedsListView.setAdapter(feedsAdapter);
      feedsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          BaFeed baFeed = feedsAdapter.getItem(position);
          Intent requestViewIntent = new Intent(MainActivity.this, ViewRequestActivity.class);
          requestViewIntent.putExtra("baId", baFeed.getBaId());
          requestViewIntent.putExtra("actionId", baFeed.getRequestId());
          startActivity(requestViewIntent);
        }
      });
      load(); // load the feeds
    } else {
      Toast.makeText(this, "Cannot fetch bafeed as ba menu has not been fetched", Toast.LENGTH_LONG).show();
    }
  }

  /**
   * This is the main method which loads feeds. If another request is already going
   * on then do not process.
   */
  private void load() {
    if (loading != null && !loading.isDone() && !loading.isCancelled()) {
      return;
    }
    String url = ApplicationConstants.baseAppUrl
        + "actions/banewsfeed"
        + "?clientId=1&baId=" + feedBa.getBaId()
        + "&count=10";
    if (feedsAdapter.getCount() > 0) {
      if (feedsAdapter.getCount() >= totalFeedsCount) {
        return;
      }
      url += "&skip=" + feedsAdapter.getCount();
    }
    Toast.makeText(this, "started loading feed.", Toast.LENGTH_SHORT).show();
    // Now load the feed
    loading = Ion.with(this)
        .load(url)
        .setHeader("auth-token", authToken)
        .asJsonObject()
        .setCallback(new FutureCallback<JsonObject>() {
          @Override
          public void onCompleted(Exception e, JsonObject result) {
            if (e != null) {
              Log.e(tag, "Error fetching ba feed");
              Toast.makeText(MainActivity.this, "Error fetching ba feed", Toast.LENGTH_LONG).show();
              return;
            }

            JsonArray mainFeeds = result.getAsJsonArray("baActions");
            long feedsLimit = result.get("baActionsCount").getAsLong();
            if (mainFeeds != null) {
              totalFeedsCount = feedsLimit;
              for (JsonElement singleFeedJson : mainFeeds) {
                feedsAdapter.add(new BaFeed(feedBa.getBaId(), singleFeedJson));
              }
            }
            Toast.makeText(MainActivity.this, "feeds loading finished", Toast.LENGTH_LONG);
          }
        });
  }

  /*@Override
  public void processBaFeed(long baId, List<BaFeed> feeds) {
    falconRequestListAdapter = new FalconRequestListAdapter(this, baId, getLayoutInflater(), feeds);
    ListView listView = (ListView) findViewById(R.id.main_list_content);
    listView.setAdapter(falconRequestListAdapter);
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BaFeed baFeed = falconRequestListAdapter.getItem(position);
        Intent intent = new Intent(getApplicationContext(), ViewRequestActivity.class);
        intent.putExtra("baId", baFeed.getBaId());
        intent.putExtra("actionId", baFeed.getRequestId());
        startActivity(intent);
      }
    });
  }*/

}
