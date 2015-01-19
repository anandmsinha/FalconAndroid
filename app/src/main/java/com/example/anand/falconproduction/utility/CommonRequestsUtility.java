package com.example.anand.falconproduction.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.anand.falconproduction.interfaces.GetBaFeed;
import com.example.anand.falconproduction.interfaces.GetBaMap;
import com.example.anand.falconproduction.models.BaFeed;
import com.example.anand.falconproduction.models.BaGroups;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anand on 27/11/14.
 * <p/>
 * This class caches some common entries in memory.
 */
public class CommonRequestsUtility {

  private static final String tag = CommonRequestsUtility.class.getName();
  private static List<BaGroups> baGroups;
  private static HashMap<Long, List<BaFeed>> baFeeds = new HashMap<Long, List<BaFeed>>();
  private static HashMap<Long, Long> baFeedsLimit = new HashMap<>();
  private static Future<JsonObject> loading;

  /**
   * This method takes a callback if bagroups are already fetched just return it else fetch it
   * and return the callback once done.
   * @param context - Activity
   * @param next - Activity which implement GetBaMap interface.
   */
  public static void getBaMap(final Context context, final GetBaMap next) {
    Log.d(tag, "GetBaMap called");
    if (baGroups == null) {
      String url = ApplicationConstants.baseAppUrl
          + "bagroups/permissible"
          + "?clientId=" + ApiUrlBuilder.getClientId() + "&userId=1";
      Ion.getDefault(context).configure().setLogging("Net", Log.DEBUG);
      Ion.with(context)
          .load(url)
          .setJsonObjectBody(new JsonObject())
          .asJsonObject()
          .setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
              List<BaGroups> groups = new ArrayList<>();
              if (e != null) {
                Toast.makeText(context, "Error loading ba groups", Toast.LENGTH_LONG)
                    .show();
              } else {
                for (Map.Entry<String, JsonElement> entry : result.entrySet()) {
                  groups.add(new BaGroups(entry.getValue()));
                }
                setBaGroups(groups);
              }
              next.processBaMap(groups);
            }
          });
    } else {
      next.processBaMap(baGroups);
    }
  }

  /**
   * Load BaFeed this method caches feeds in memory for better loading.
   *
   * Todo - check limit of bafeed count
   *
   * @param context - activity
   * @param next - class to process the feeds in case of pagination only the new feeds are returned.
   * @param baId - feeds baId
   * @param count - count of feeds needed.
   */
  public static void getBaFeed(final Context context, final GetBaFeed next, final long baId, int count) {
    Log.d(tag, "Get ba feed called");
    // cancel the request if it is already going on.
    if (loading != null && !loading.isDone() && !loading.isCancelled())
      return;
    boolean loadFeed = true;
    int skip = 0;
    if (baFeeds.containsKey(baId)) {
      skip = baFeeds.get(baId).size();
      if (baFeeds.get(baId).size() >= count) {
        loadFeed = false;
      }
    } else {
      baFeeds.put(baId, new ArrayList<BaFeed>());
    }
    if (loadFeed) {
      String url = ApplicationConstants.baseAppUrl
          + "actions/banewsfeed"
          + "?clientId=" + ApiUrlBuilder.getClientId() + "&baId=" + baId
          + "&skip=" + skip
          + "&count=" + 10;
      Ion.getDefault(context).configure().setLogging("feed", Log.DEBUG);
      SharedPreferences prefs = context.getSharedPreferences(
          ApplicationConstants.appSharedPreference, Context.MODE_PRIVATE);
      String token = prefs.getString(ApplicationConstants.appAuthToken, "token");
      Log.d(tag, "Authentication token - " + token);
      Ion.with(context)
          .load(url)
          .setHeader("auth-token", token)
          .asJsonObject()
          .setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
              List<BaFeed> output = new ArrayList<>();
              if (e != null) {
                Log.e(tag, "Error loading ba feed for baid " + baId + " error - " + e.getMessage());
                Toast.makeText(context, "Error loading ba feed", Toast.LENGTH_LONG)
                    .show();
              } else {
                JsonArray mainFeed = result.getAsJsonArray("baActions");
                Long feedLimitCount = result.get("baActionsCount").getAsLong();
                if (mainFeed != null) {
                  for (JsonElement jsonElement : mainFeed) {
                    output.add(new BaFeed(baId, jsonElement));
                  }
                  baFeeds.get(baId).addAll(output);
                  baFeedsLimit.put(baId, feedLimitCount);
                }
              }
              next.processBaFeed(baId, output);
            }
          });
    } else {
      next.processBaFeed(baId, baFeeds.get(baId));
    }
  }

  private static void setBaGroups(List<BaGroups> baGroupes) {
    baGroups = baGroupes;
  }

}
