package com.example.anand.falconproduction.models.create;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anand on 16/12/14.
 * <p/>
 * This model represnts create request form.
 */
public class RequestForm {

  private List<DisplayGroupAdvanced> displayGroupsAdvanced;

  public RequestForm(JsonObject jsonObject) {
    displayGroupsAdvanced = new ArrayList<>();
    JsonArray jsonArray = jsonObject.getAsJsonArray("actionDisplayGroups");
    if (jsonArray != null) {
      for (JsonElement jsonElement : jsonArray) {
        displayGroupsAdvanced.add(new DisplayGroupAdvanced(jsonElement));
      }
    }
  }

  @Override
  public String toString() {
    return "RequestForm{" +
        "displayGroupsAdvanced=" + displayGroupsAdvanced +
        '}';
  }

  public List<DisplayGroupAdvanced> getDisplayGroupsAdvanced() {
    return displayGroupsAdvanced;
  }

  public void setDisplayGroupsAdvanced(List<DisplayGroupAdvanced> displayGroupsAdvanced) {
    this.displayGroupsAdvanced = displayGroupsAdvanced;
  }

}
