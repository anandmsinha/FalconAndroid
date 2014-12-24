package com.example.anand.falconproduction.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.anand.falconproduction.ActivityDecider;
import com.example.anand.falconproduction.R;
import com.example.anand.falconproduction.adapters.BaGroupListAdapter;
import com.example.anand.falconproduction.interfaces.GetBaMap;
import com.example.anand.falconproduction.interfaces.ProcessAfterDrawer;
import com.example.anand.falconproduction.models.BaGroups;
import com.example.anand.falconproduction.utility.ApplicationConstants;
import com.example.anand.falconproduction.utility.CommonRequestsUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by anand on 28/11/14.
 * <p/>
 * To extend this class you should have following elements in your
 * layout file.
 * 1. DrawerLayout with id - R.id.main_drawer_layout
 * 2. ListView in drawer layout with id - main_nav_list
 */
public abstract class BaseDrawerActivity extends ActionBarActivity implements GetBaMap, AdapterView.OnItemClickListener {

  ActionBarDrawerToggle mDrawerToggle;
  DrawerLayout mainDrawerLayout;
  ListView mainNavListView;
  Bundle intentBundle;
  BaGroupListAdapter mainAdapter;
  int groupId = -1;
  ProcessAfterDrawer nextMethod;
  String authToken;
  HashMap<Long, BaGroups> baGroupsMap = new HashMap<>();

  /**
   * Here we instatnitate the drawer menu.
   *
   * @param resId - resource id
   */
  public void setContentView(int resId, ProcessAfterDrawer next) {
    super.setContentView(resId);
    nextMethod = next;
    //String[] menu = new String[]{"Home", "Android", "Windows", "Linux", "Raspberry Pi", "WordPress", "Videos", "Contact Us"};
    mainNavListView = (ListView) findViewById(R.id.main_nav_list);
    mainDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
    mainDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

    SharedPreferences preferences = getSharedPreferences(ApplicationConstants.appSharedPreference,
        MODE_PRIVATE);
    authToken = preferences.getString(ApplicationConstants.appAuthToken, "token");

    CommonRequestsUtility.getBaMap(this, this);
  }

  @Override
  public void processBaMap(List<BaGroups> list) {
    if (list == null) {
      list = new ArrayList<>();
    }
    /**
     * fetch only the Ba since right now we are creating linear menu.
     */
    List<BaGroups> finalList = new ArrayList<>();
    for (BaGroups baGroups : list) {
      if (baGroups.isYield()) {
        if (baGroups.isGroup()) {
          finalList.addAll(baGroups.getAllBaInGroup());
        } else {
          finalList.add(baGroups);
        }
      }
    }
    for (BaGroups baGroups : finalList) {
      baGroupsMap.put(baGroups.getBaId(), baGroups);
    }
    mainAdapter = new BaGroupListAdapter(getLayoutInflater(), finalList);
    mainNavListView.setAdapter(mainAdapter);
    mainNavListView.setSelector(android.R.color.holo_blue_dark);
    mainNavListView.setOnItemClickListener(this);

    ActionBar actionBar = getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setHomeButtonEnabled(true);
    mDrawerToggle = new ActionBarDrawerToggle(
        this,
        mainDrawerLayout,
        R.drawable.ic_drawer,
        R.string.navigation_drawer_open,
        R.string.navigation_drawer_close
    );

    mainDrawerLayout.post(new Runnable() {
      @Override
      public void run() {
        mDrawerToggle.syncState();
      }
    });
    mainDrawerLayout.setDrawerListener(mDrawerToggle);

    intentBundle = getIntent().getExtras();
    if (intentBundle != null) {
      groupId = intentBundle.getInt("group");
    } else {
      groupId = 0;
    }
    try {
      getSupportActionBar().setTitle(mainAdapter.getItem(groupId).toString());
      mainNavListView.setSelection(groupId);
    } catch (Exception e) {
      Log.e("base", "unable to set title");
    }
    nextMethod.fillDetails(mainAdapter.getCount());
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    SearchView searchView = (SearchView) menu.findItem(R.id.main_search).getActionView();
    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    //searchView.setIconifiedByDefault(false);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (mDrawerToggle.onOptionsItemSelected(item)) {
      return true;
    }

    switch (item.getItemId()) {
      case R.id.main_search:
        //startActivity(new Intent(this, SearchResultsActivity.class));
        return true;
      case R.id.main_add_button:
        BaGroups tmpBaGroup = baGroupsMap.get(getBaId());
        if (tmpBaGroup != null) {
          // check if user has create permission in Ba.
          if (tmpBaGroup.isBaCreatePermission()) {
            Intent intent = new Intent(this, CreateRequestActivity.class);
            intent.putExtra("baId", getBaId());
            intent.putExtra("group", groupId);
            startActivity(intent);
          } else {
            Toast.makeText(this, "You don't have create permission in this Ba.", Toast.LENGTH_LONG).show();
          }
        } else {
          Toast.makeText(this, "Some internal error has occured.", Toast.LENGTH_LONG).show();
        }
        return true;
      case R.id.action_logout:
        getSharedPreferences(ApplicationConstants.appSharedPreference, 0)
            .edit().remove(ApplicationConstants.appAuthToken).remove(ApplicationConstants.clientToken).commit();
        startActivity(new Intent(this, ActivityDecider.class));
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    mDrawerToggle.syncState();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    mDrawerToggle.onConfigurationChanged(newConfig);
  }

  @Override
  public void onBackPressed() {
    if (mainDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
      mainDrawerLayout.closeDrawers();
      return;
    }
    super.onBackPressed();
  }

  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    Intent intent = new Intent(this, MainActivity.class);
    intent.putExtra("group", position);
    startActivity(intent);
  }

  public abstract long getBaId();
}
