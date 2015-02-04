package com.example.anand.falconproduction.models.create;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;

/**
 * Created by anand on 16/12/14.
 * <p/>
 * This is a advanced display group and is used in case of create request activity.
 * Reason for having two diffrent types of DisplayGroup classes is that in case of
 * view request we need fewer fields compared to create request so in order to save network
 * transfer bandwidth we have created two diffrent classes.
 */
public class DisplayGroupAdvanced {
  private String actionDisplayGroupTitle;
  private HashMap<Long, FieldAdvanced> fieldsMap;

  public DisplayGroupAdvanced(JsonElement jsonElement) {
    this(jsonElement, false);
  }

  public DisplayGroupAdvanced(JsonElement jsonElement, boolean isEdit) {
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    actionDisplayGroupTitle = jsonObject.get("actionDisplayGroupTitle").getAsString();
    JsonArray jsonArray = jsonObject.get("fields").getAsJsonArray();
    fieldsMap = new HashMap<>();
    if (jsonArray != null) {
      for (JsonElement field : jsonArray) {
        FieldAdvanced fieldAdvanced = new FieldAdvanced(field, isEdit);
        fieldsMap.put(fieldAdvanced.getFieldId(), fieldAdvanced);
      }
    }
  }

  public HashMap<Long, FieldAdvanced> getFieldsMap() {
    return fieldsMap;
  }

  public void setFieldsMap(HashMap<Long, FieldAdvanced> fieldsMap) {
    this.fieldsMap = fieldsMap;
  }

  public String getActionDisplayGroupTitle() {
    return actionDisplayGroupTitle;
  }

  public void setActionDisplayGroupTitle(String actionDisplayGroupTitle) {
    this.actionDisplayGroupTitle = actionDisplayGroupTitle;
  }

}
