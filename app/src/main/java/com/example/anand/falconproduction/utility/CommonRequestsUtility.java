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

  private static final String tag = "commonrequest";
  private static List<BaGroups> baGroups;
  private static HashMap<Long, List<BaFeed>> baFeeds = new HashMap<Long, List<BaFeed>>();

  public static void getBaMap(final Context context, final GetBaMap next) {
    if (baGroups == null) {
      String url = ApplicationConstants.baseAppUrl
          + "bagroups/permissible"
          + "?clientId=1&userId=1";
      Ion.getDefault(context).configure().setLogging("Net", Log.DEBUG);
      Ion.with(context)
          .load(url)
          .setJsonObjectBody(new JsonObject())
          .asJsonObject()
          .setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
              if (e != null) {
                Toast.makeText(context, "Error loading ba groups", Toast.LENGTH_LONG)
                    .show();
                return;
              }

              List<BaGroups> groups = new ArrayList<BaGroups>();
              for (Map.Entry<String, JsonElement> entry : result.entrySet()) {
                groups.add(new BaGroups(entry.getValue()));
              }
              setBaGroups(groups);
              next.processBaMap(groups);
            }
          });
    }
    next.processBaMap(baGroups);
  }

  public static void getBaFeed(final Context context, final GetBaFeed next, final long baId) {
    if (!baFeeds.containsKey(baId)) {
      String url = ApplicationConstants.baseAppUrl
          + "actions/banewsfeed"
          + "?clientId=1&baId=" + baId;
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
              if (e != null) {
                Log.e(tag, "Error loading ba feed for baid " + baId + " error - " + e.getMessage());
                Toast.makeText(context, "Error loading ba feed", Toast.LENGTH_LONG)
                    .show();
                return;
              }

              List<BaFeed> output = new ArrayList<BaFeed>();
              JsonArray mainFeed = result.getAsJsonArray("baActions");
              if (mainFeed != null) {
                for (JsonElement jsonElement : mainFeed) {
                  output.add(new BaFeed(baId, jsonElement));
                }
                baFeeds.put(baId, output);
              }
              next.processBaFeed(baId, output);
            }
          });
    }
    next.processBaFeed(baId, baFeeds.get(baId));
  }

  private static void setBaGroups(List<BaGroups> baGroupes) {
    baGroups = baGroupes;
  }

}
