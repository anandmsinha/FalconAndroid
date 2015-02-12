package com.example.anand.falconproduction.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.anand.falconproduction.R;
import com.example.anand.falconproduction.interfaces.ProcessAfterDrawer;
import com.example.anand.falconproduction.models.search.SavedSearchWrapper;
import com.example.anand.falconproduction.utility.ApiUrlBuilder;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by anand on 10/2/15.
 * <p/>
 * List view to show saved searches of the user.
 */
public class SavedSearchActivity extends BaseDrawerActivity implements ProcessAfterDrawer {

  public static final String Tag = SavedSearchActivity.class.getName();

  ProgressBar mProgressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.d(Tag, "onCreate called");
    super.onCreate(savedInstanceState);
    super.setContentView(R.layout.saved_search_activity, this);
  }

  @Override
  public long getBaId() {
    return 0;
  }

  @Override
  public void fillDetails(int groupsCount) {
    Log.d(Tag, "fillDetails called - with group size " + groupsCount);
    if (mProgressBar == null) {
      mProgressBar = (ProgressBar) findViewById(R.id.feed_progress_bar);
    }
    Ion.with(this)
        .load(ApiUrlBuilder.getSavedSearches())
        .setHeader("auth-token", authToken)
        .as(new TypeToken<SavedSearchWrapper>(){})
        .setCallback(new FutureCallback<SavedSearchWrapper>() {
          @Override
          public void onCompleted(Exception e, SavedSearchWrapper result) {
            if (e != null) {
              Log.e(Tag, "Error loading saved searches.", e);
              Toast.makeText(SavedSearchActivity.this, "Error in loading saved searches.", Toast.LENGTH_LONG).show();
              return;
            }

          }
        });
  }
}
