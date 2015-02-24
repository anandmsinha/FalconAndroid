package com.example.anand.falconproduction.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.example.anand.falconproduction.R;
import com.example.anand.falconproduction.adapters.ViewRequestFragmentAdapter;
import com.example.anand.falconproduction.interfaces.GetActionModel;
import com.example.anand.falconproduction.interfaces.ProcessAfterDrawer;
import com.example.anand.falconproduction.models.ActionModel;
import com.example.anand.falconproduction.utility.CommonRequestsUtility;
import com.example.anand.falconproduction.utility.UiBuilder;

import java.util.ArrayList;

/**
 * Created by anand on 6/2/15.
 *
 * This is advanced view request activity it uses viewrequestfragmentadapter
 * to show all actions in form of slider tabs.
 */
public class AdvancedViewRequestActivity extends BaseDrawerActivity implements ProcessAfterDrawer {

  private static final String TAG = AdvancedViewRequestActivity.class.getName();

  long baId;
  long actionId;

  ViewPager viewPager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.d(TAG, "On create called.");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.advanced_view_request_activity, this);
  }

  /**
   * This method has been overriden so that we can put ba id into intent.
   *
   * @param intent - passing intent
   */
  @Override
  public void startActivity(Intent intent) {
    Log.d(TAG, "startActivity called");
    intent.putExtra("baId", baId);
    intent.putExtra("group", groupId);
    super.startActivity(intent);
  }

  @Override
  public long getBaId() {
    return baId;
  }

  @Override
  public void fillDetails(int groupsCount) {
    Log.d(TAG, "fillDetails called");
    baId = intentBundle.getLong("baId");
    actionId = intentBundle.getLong("actionId");
    final ProgressDialog showLoading = UiBuilder.createProgressDialog(
        this, getResources().getString(R.string.loading), getResources().getString(R.string.fetching_action));
    showLoading.show();
    CommonRequestsUtility.fetchActionModel(this, authToken, baId, actionId, new GetActionModel() {
      @Override
      public void processActionModel(ActionModel actionModel) {
        showLoading.dismiss();
        viewPager = (ViewPager) findViewById(R.id.view_activity_pager);
        ViewRequestFragmentAdapter viewRequestFragmentAdapter =
            new ViewRequestFragmentAdapter(getSupportFragmentManager(), authToken, actionModel);
        viewPager.setAdapter(viewRequestFragmentAdapter);
        // Calculate and set active tab
        int index = viewRequestFragmentAdapter.getActionIds().indexOf(actionModel.getActionId());
        viewPager.setCurrentItem(index);
      }

      @Override
      public void onActionModelFetchError(boolean networkError) {
        showLoading.setTitle(getResources().getString(R.string.fetch_error));
        if (networkError) {
          showLoading.setMessage(getResources().getString(R.string.network_error));
        } else {
          showLoading.setMessage(getResources().getString(R.string.permission_view));
        }
      }
    });
  }
}
