package com.example.anand.falconproduction.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anand on 1/12/14.
 *
 * This model represents a business area.
 */
public class BusinessArea {

  private long baId;
  private String name;
  private List<String> permissions;
  private String displayName;

  public BusinessArea(JsonElement jsonElement) {
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    this.baId = jsonObject.get("id").getAsLong();
    this.name = jsonObject.get("ba_name").getAsString();
    this.displayName = jsonObject.get("name").getAsString();
    this.permissions = new ArrayList<String>();
    JsonArray jsonArray = jsonObject.getAsJsonArray("ba_permissions");
    if (jsonArray != null && jsonArray.size() > 0) {
      for (JsonElement jsonElement1 : jsonArray) {
        this.permissions.add(jsonElement1.getAsString());
      }
    }
  }

}
