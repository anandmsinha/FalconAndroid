package com.example.anand.falconproduction.utility;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.anand.falconproduction.interfaces.GetActionModel;
import com.example.anand.falconproduction.interfaces.GetBaMap;
import com.example.anand.falconproduction.models.ActionModel;
import com.example.anand.falconproduction.models.BaGroups;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
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
  private static long defaultBaId = -1;

  /**
   * This method takes a callback if bagroups are already fetched just return it else fetch it
   * and return the callback once done.
   *
   * @param context - Activity
   * @param next    - Activity which implement GetBaMap interface.
   */
  public static void getBaMap(final Context context, String authToken, final GetBaMap next) {
    Log.d(tag, "GetBaMap called");
    if (baGroups == null) {
      Ion.getDefault(context).configure().setLogging("Net", Log.DEBUG);
      Ion.with(context)
          .load(ApiUrlBuilder.getBaMenu())
          .setHeader("auth-token", authToken)
          .setJsonObjectBody(new JsonObject())
          .asJsonObject()
          .setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
              List<BaGroups> groups = new ArrayList<>();
              if (e != null || !result.has("menu")) {
                Toast.makeText(context, "Error loading ba groups", Toast.LENGTH_LONG)
                    .show();
              } else {
                if (result.has("defaultBa")) {
                  try {
                    long tmpBaId = result.get("defaultBa").getAsLong();
                    Log.d(tag, "Setting default baId as - " + tmpBaId);
                    setDefaultBaId(tmpBaId);
                  } catch (Exception ex) {
                    Log.e(tag, "Error casting default Ba", ex);
                  }
                }
                JsonObject mainMenus = result.getAsJsonObject("menu");
                for (Map.Entry<String, JsonElement> entry : mainMenus.entrySet()) {
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

  public static void fetchActionModel(final Context context, String authToken, long baId, long actionId, final GetActionModel getActionModel) {
    Log.d(tag, "fetchActionModel called");
    Ion.with(context)
        .load(SearchQueryBuilder.getAnAction(baId, actionId))
        .setHeader("auth-token", authToken)
        .asJsonObject()
        .setCallback(new FutureCallback<JsonObject>() {
          @Override
          public void onCompleted(Exception e, JsonObject result) {
            if (e != null || !result.has(ApplicationConstants.businessArea)) {
              Log.d(tag, "Error in fetching action. " + result.toString());
              if (e != null) {
                getActionModel.onActionModelFetchError(true);
              } else {
                getActionModel.onActionModelFetchError(false);
              }
            } else {
              Log.d(tag, "Response fetch successfull building ui.");
              ActionModel actionModel = new ActionModel(result);
              getActionModel.processActionModel(actionModel);
            }
          }
        });
  }

  public static void setBaGroups(List<BaGroups> baGroupes) {
    baGroups = baGroupes;
  }

  public static long getDefaultBaId() {
    return defaultBaId;
  }

  public static void setDefaultBaId(long baId) {
    defaultBaId = baId;
  }
}
