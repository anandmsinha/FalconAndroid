package com.example.anand.falconproduction.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.anand.falconproduction.R;
import com.example.anand.falconproduction.adapters.ListViewWithHeaderAdapter;
import com.example.anand.falconproduction.adapters.components.HeaderItem;
import com.example.anand.falconproduction.adapters.components.SavedSearchItem;
import com.example.anand.falconproduction.interfaces.ListItem;
import com.example.anand.falconproduction.interfaces.ProcessAfterDrawer;
import com.example.anand.falconproduction.models.search.SavedSearchWrapper;
import com.example.anand.falconproduction.models.search.SearchContainer;
import com.example.anand.falconproduction.models.search.SingleSavedSearch;
import com.example.anand.falconproduction.utility.ApiUrlBuilder;
import com.example.anand.falconproduction.utility.ApplicationConstants;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anand on 10/2/15.
 * <p/>
 * List view to show saved searches of the user.
 */
public class SavedSearchActivity extends BaseDrawerActivity implements ProcessAfterDrawer {

  public static final String Tag = SavedSearchActivity.class.getName();

  ProgressBar mProgressBar;
  SavedSearchWrapper savedSearchWrapper;
  long baId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.d(Tag, "onCreate called");
    super.onCreate(savedInstanceState);
    super.setContentView(R.layout.saved_search_activity, this);
  }

  @Override
  public long getBaId() {
    return baId;
  }

  @Override
  public void fillDetails(int groupsCount) {
    Log.d(Tag, "fillDetails called - with group size " + groupsCount);
    if (mProgressBar == null) {
      mProgressBar = (ProgressBar) findViewById(R.id.feed_progress_bar);
    }
    this.baId = intentBundle.getLong("baId");
    Ion.with(this)
        .load(ApiUrlBuilder.getSavedSearches())
        .setHeader(ApplicationConstants.authTokenHeader, authToken)
        .as(new TypeToken<SavedSearchWrapper>(){})
        .setCallback(new FutureCallback<SavedSearchWrapper>() {
          @Override
          public void onCompleted(Exception e, SavedSearchWrapper result) {
            if (e != null && result != null) {
              Log.e(Tag, "Error loading saved searches.", e);
              Toast.makeText(SavedSearchActivity.this, getResources().getString(R.string.error_saved_search), Toast.LENGTH_LONG).show();
            } else {
              Log.d(Tag, "Saved searches successfully fetched.");
              SavedSearchActivity.this.savedSearchWrapper = result;
              buildUi();
            }
          }
        });
  }

  /**
   * Build the ui and hide the progress bar.
   */
  private void buildUi() {
    if (savedSearchWrapper != null && savedSearchWrapper.getCount() != 0) {
      ListView mainListView = (ListView) findViewById(R.id.main_list_content);
      final List<ListItem> adapterList = new ArrayList<>();
      for (SearchContainer searchContainer : savedSearchWrapper.getContainer()) {
        adapterList.add(new HeaderItem(searchContainer.getDisplayName()));
        for (SingleSavedSearch singleSavedSearch : searchContainer.getSearches()) {
          adapterList.add(new SavedSearchItem(singleSavedSearch));
        }
      }
      ListViewWithHeaderAdapter listViewWithHeaderAdapter = new ListViewWithHeaderAdapter(this, adapterList);
      mainListView.setAdapter(listViewWithHeaderAdapter);
      mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          ListItem listItem = adapterList.get(position);
          if (listItem instanceof SavedSearchItem) {
            SavedSearchItem savedSearchItem = (SavedSearchItem) listItem;
            Intent intent = new Intent(SavedSearchActivity.this, SearchResultsActivity.class);
            intent.putExtra(SearchManager.QUERY, "s__" + savedSearchItem.getSingleSavedSearch().getId());
            intent.putExtra("baId", savedSearchItem.getSingleSavedSearch().getBaId());
            startActivity(intent);
          }
        }
      });
    }
    mProgressBar.setVisibility(View.GONE);
  }
}
