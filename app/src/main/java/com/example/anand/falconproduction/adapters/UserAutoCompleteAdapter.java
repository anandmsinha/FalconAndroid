package com.example.anand.falconproduction.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.anand.falconproduction.utility.ApiUrlBuilder;
import com.example.anand.falconproduction.utility.ApplicationConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anand on 16/12/14.
 *
 * Adapter for autocomplete of usernames.
 */
public class UserAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

  private static final String TAG = "UserAutoCompleteAdapter";
  private List<String> users = new ArrayList<>();
  private Context mContext;
  String authToken;

  public UserAutoCompleteAdapter(Context context, int textViewResourceId) {
    super(context, textViewResourceId);
    mContext = context;
    SharedPreferences preferences = context.getSharedPreferences(ApplicationConstants.appSharedPreference,
        Context.MODE_PRIVATE);
    authToken = preferences.getString(ApplicationConstants.appAuthToken, "token");
  }

  @Override
  public int getCount() {
    return users.size();
  }

  @Override
  public String getItem(int index) {
    return users.get(index);
  }

  @Override
  public Filter getFilter() {
    return new Filter() {
      @Override
      protected FilterResults performFiltering(CharSequence constarint) {
        Log.d(TAG, "Perform filtering called for " + constarint);
        FilterResults filterResults = new FilterResults();
        if (constarint != null) {
          users = getAutoCompleteData(constarint.toString());
          filterResults.values = users;
          filterResults.count = users.size();
        }
        return filterResults;
      }

      @Override
      protected void publishResults(CharSequence constraint, FilterResults results) {
        if (results != null && results.count > 0) {
          notifyDataSetChanged();
        } else {
          notifyDataSetInvalidated();
        }
      }
    };
  }

  private ArrayList<String> getAutoCompleteData(String data) {
    Log.d(TAG, "Get autocomplete called for " + data);
    ArrayList<String> output = new ArrayList<>();
    try {
      // As this method is already called inside worker thread of filterable so we can directly get response instead of future response.
      JsonObject jsonObject = Ion.with(mContext)
          .load(ApiUrlBuilder.getUserAutoComplete(data))
          .setHeader("auth-token", authToken)
          .asJsonObject()
          .get();
      JsonArray jsonArray = jsonObject.getAsJsonArray("users");
      if (jsonArray != null) {
        Log.d(TAG, "autocomplete response successfull");
        for (JsonElement jsonElement : jsonArray) {
          output.add(jsonElement.getAsString());
        }
      }
    } catch (Exception e) {
      Log.e(TAG, "Error fetching autocomplete entries");
    }
    return output;
  }
}
