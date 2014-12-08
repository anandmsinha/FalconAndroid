package com.example.anand.falconproduction.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.anand.falconproduction.R;
import com.example.anand.falconproduction.adapters.FalconRequestListAdapter;
import com.example.anand.falconproduction.interfaces.GetBaFeed;
import com.example.anand.falconproduction.interfaces.ProcessAfterDrawer;
import com.example.anand.falconproduction.models.ActionModel;
import com.example.anand.falconproduction.models.BaFeed;
import com.example.anand.falconproduction.models.BaGroups;
import com.example.anand.falconproduction.utility.ApplicationConstants;
import com.example.anand.falconproduction.utility.CommonRequestsUtility;
import com.example.anand.falconproduction.utility.SearchQueryBuilder;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.List;

/**
 * Main activity class here we show bafeed to the user.
 */
public class MainActivity extends BaseDrawerActivity implements GetBaFeed, ProcessAfterDrawer {

  private static final String tag = "main_activity";
  FalconRequestListAdapter falconRequestListAdapter;
  Future<JsonObject> loading;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main, this);
  }

  @Override
  public void fillDetails() {
    if (mainAdapter.getCount() != 0) {
      BaGroups baGroups = mainAdapter.getItem(groupId);
      CommonRequestsUtility.getBaFeed(this, this, baGroups.getBaId(), 10);
    } else {
      Toast.makeText(this, "Cannot fetch bafeed as ba menu has not been fetched", Toast.LENGTH_LONG).show();
    }
  }

  @Override
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
  }

}
