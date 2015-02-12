package com.example.anand.falconproduction.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.util.Log;
import android.view.Menu;
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
import com.example.anand.falconproduction.utility.ApiUrlBuilder;
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

  private static final String tag = MainActivity.class.getName();
  Future<JsonObject> loading;
  ArrayAdapter<BaFeed> feedsAdapter;
  long totalFeedsCount = 0;
  BaGroups feedBa;
  ProgressBar mProgressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.d(tag, "onCreate called");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main, this);
  }

  /**
   * This method has been overriden so that we can put ba id into intent.
   *
   * @param intent - passing intent
   */
  @Override
  public void startActivity(Intent intent) {
    Log.d(tag, "startActivity called");
    intent.putExtra("baId", feedBa.getBaId());
    intent.putExtra("group", groupId);
    super.startActivity(intent);
  }

  @Override
  public void fillDetails(int groupsSize) {
    Log.d(tag, "fillDetails called - with group size " + groupsSize);
    if (mProgressBar == null) {
      mProgressBar = (ProgressBar) findViewById(R.id.feed_progress_bar);
    }
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
            // for the purpose of infinte scroll.
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
          Intent requestViewIntent = new Intent(MainActivity.this, AdvancedViewRequestActivity.class);
          requestViewIntent.putExtra("baId", baFeed.getBaId());
          requestViewIntent.putExtra("actionId", baFeed.getActionId());
          startActivity(requestViewIntent);
        }
      });
      load(); // load the feeds
    } else {
      mProgressBar.setVisibility(View.GONE);
      Toast.makeText(this, "Cannot fetch bafeed as ba menu has not been fetched", Toast.LENGTH_LONG).show();
    }
  }

  /**
   * This is the main method which loads feeds. If another request is already going
   * on then do not process.
   */
  private void load() {
    Log.d(tag, "load called");
    if (loading != null && !loading.isDone() && !loading.isCancelled()) {
      return;
    }
    String url = ApplicationConstants.baseAppUrl
        + "actions/banewsfeed"
        + "?clientId=" + ApiUrlBuilder.getClientId() + "&baId=" + feedBa.getBaId()
        + "&count=10";
    Log.d(tag, "Total feeds in adapter = " + feedsAdapter.getCount());
    Log.d(tag, "Max feed limit for ba = " + totalFeedsCount);
    if (feedsAdapter.getCount() > 0) {
      if (feedsAdapter.getCount() >= totalFeedsCount) {
        Log.d(tag, "Skipping loading as feed count is >= totlafeeds");
        return;
      }
      url += "&skip=" + feedsAdapter.getCount();
    }
    Log.d(tag, "final url - " + url);
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
            if (mainFeeds != null) {
              long feedsLimit = result.get("baActionsCount").getAsLong();
              totalFeedsCount = feedsLimit;
              Log.d(tag, "total feed limit - " + feedsLimit);
              for (JsonElement singleFeedJson : mainFeeds) {
                feedsAdapter.add(new BaFeed(feedBa.getBaId(), singleFeedJson));
              }
            }
            Toast.makeText(MainActivity.this, "feeds loading finished", Toast.LENGTH_LONG).show();
            mProgressBar.setVisibility(View.GONE);
          }
        });
  }

  @Override
  public long getBaId() {
    return feedBa.getBaId();
  }

}
