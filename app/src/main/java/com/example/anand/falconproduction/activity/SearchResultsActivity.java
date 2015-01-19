package com.example.anand.falconproduction.activity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anand.falconproduction.R;
import com.example.anand.falconproduction.utility.ApiUrlBuilder;
import com.example.anand.falconproduction.utility.ApplicationConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Map;

/**
 * Created by anand on 15/12/14.
 *
 * Class for handling search results.
 */
public class SearchResultsActivity extends Activity {

  private final static String TAG = SearchResultsActivity.class.getName();

  ListView mListView;
  ProgressBar mProgressBar;
  Future<JsonObject> loading;
  ArrayAdapter<JsonObject> searchResultsAdapter;
  int totalResultsCount = 0;
  String query;
  long baId;
  String token;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.d(TAG, "onCreate called");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.search_results);
    mListView = (ListView) findViewById(R.id.search_results_list);
    mProgressBar = (ProgressBar) findViewById(R.id.search_progress_bar);
    Log.e("result", "called result");
    SharedPreferences prefs = getSharedPreferences(
        ApplicationConstants.appSharedPreference, Context.MODE_PRIVATE);
    token = prefs.getString(ApplicationConstants.appAuthToken, "token");
    handleIntent(getIntent());
  }

  private void handleIntent(Intent intent) {
    Log.d(TAG, "handleIntent called");
    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
      query = intent.getStringExtra(SearchManager.QUERY);
      baId = intent.getLongExtra("baId", 0);
      searchResultsAdapter = new ArrayAdapter<JsonObject>(this, 0) {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
          if (convertView == null) {
            convertView = getLayoutInflater().inflate(R.layout.feed_list_row, parent, false);
          }
          if (position >= getCount() - 2) {
            load();
          }
          JsonObject mainObject =  getItem(position);
          TextView mainId = (TextView) convertView.findViewById(R.id.main_action_id);
          mainId.setText(mainObject.get("requestDisplayId").getAsString() + " : " + mainObject.get("actionDisplayId").getAsString());
          TextView mainActionTitle = (TextView) convertView.findViewById(R.id.main_action_title);
          mainActionTitle.setText(mainObject.get("mainTitle").getAsString());
          JsonObject searchHighlight = mainObject.getAsJsonObject("searchResultsFields");

          if (searchHighlight != null) {
            StringBuilder htmlText = new StringBuilder();
            for (Map.Entry<String, JsonElement> elementEntry : searchHighlight.entrySet()) {
              htmlText.append("<p><b>");
              htmlText.append(elementEntry.getKey());
              htmlText.append("</b> : ");
              htmlText.append(elementEntry.getValue().getAsString());
              htmlText.append("</p>");
            }
            TextView mainRequestTime = (TextView) convertView.findViewById(R.id.main_request_time);
            mainRequestTime.setText(Html.fromHtml(htmlText.toString()));
          }
          return convertView;
        }
      };
      mListView.setAdapter(searchResultsAdapter);
      mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          JsonObject mainObject = searchResultsAdapter.getItem(position);
          Intent requestViewIntent = new Intent(SearchResultsActivity.this, ViewRequestActivity.class);
          requestViewIntent.putExtra("baId", baId);
          requestViewIntent.putExtra("actionId", mainObject.get("requestId").getAsLong());
          startActivity(requestViewIntent);
        }
      });
      load();
    }
  }

  private void load() {
    Log.d(TAG, "load method called");
    if (loading != null && !loading.isDone() && !loading.isCancelled()) {
      Log.d(TAG, "loading cancelled");
      return;
    }
    String url = ApplicationConstants.baseAppUrl
        + "actions/search"
        + "?clientId=" + ApiUrlBuilder.getClientId() + "&baId=" + baId
        + "&search=" + query
        + "&count=10";
    if (searchResultsAdapter.getCount() > 0) {
      if (searchResultsAdapter.getCount() >= totalResultsCount) {
        return;
      }
      url += "&skip=" + searchResultsAdapter.getCount();
    }
    Ion.getDefault(this).configure().setLogging("search", Log.DEBUG);
    loading = Ion.with(this)
        .load(url)
        .setHeader("auth-token", token)
        .asJsonObject()
        .setCallback(new FutureCallback<JsonObject>() {
          @Override
          public void onCompleted(Exception e, JsonObject result) {
            Log.d("search", "search completed");
            if (e != null) {
              Log.d(TAG, "some error has occured");
              Toast.makeText(SearchResultsActivity.this, "Some error has occured", Toast.LENGTH_SHORT).show();
              mProgressBar.setVisibility(View.GONE);
              return;
            }
            JsonArray mainResults = result.getAsJsonArray("baActions");
            if (mainResults != null) {
              totalResultsCount = result.get("baActionsCount").getAsInt();
              for (JsonElement jsonElement : mainResults) {
                searchResultsAdapter.add(jsonElement.getAsJsonObject());
              }
            } else {
              Toast.makeText(SearchResultsActivity.this, "No results found", Toast.LENGTH_LONG).show();
            }
            mProgressBar.setVisibility(View.GONE);
          }
        });
  }
}
