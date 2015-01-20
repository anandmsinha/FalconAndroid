package com.example.anand.falconproduction.models;

import com.example.anand.falconproduction.models.view.DisplayGroup;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anand on 1/12/14.
 * <p/>
 * This class represents an action.
 */
public class ActionModel {

  private BusinessArea businessArea;
  private long requestDisplayId;
  private boolean canUpdateAction;
  private long actionId;
  private String actionUpdateCaption;
  private String displayTitle;
  private List<Long> permissibleActionIds;
  private List<DisplayGroup> displayGroups;

  public ActionModel(JsonElement jsonElement) {
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    this.businessArea = new BusinessArea(jsonObject.getAsJsonObject("businessArea"));
    this.requestDisplayId = jsonObject.get("requestDisplayId").getAsLong();
    this.canUpdateAction = jsonObject.get("canUpdateAction").getAsBoolean();
    this.actionId = jsonObject.get("actionId").getAsLong();
    this.actionUpdateCaption = jsonObject.get("actionUpdateCaption").getAsString();
    this.displayTitle = jsonObject.get("mainTitle").getAsString();
    this.permissibleActionIds = new ArrayList<>();
    JsonArray permissionsArray = jsonObject.getAsJsonArray("permissibleActionIds");
    if (permissionsArray != null && permissionsArray.size() > 0) {
      for (JsonElement actionId : permissionsArray) {
        permissibleActionIds.add(actionId.getAsLong());
      }
    }
    JsonArray displayGroups = jsonObject.getAsJsonArray("actionDisplayGroups");
    this.displayGroups = new ArrayList<>();
    if (displayGroups != null && displayGroups.size() > 0) {
      for (JsonElement displayGroup : displayGroups) {
        this.displayGroups.add(new DisplayGroup(displayGroup));
      }
    }
  }

  public List<DisplayGroup> getDisplayGroups() {
    return displayGroups;
  }

  public void setDisplayGroups(List<DisplayGroup> displayGroups) {
    this.displayGroups = displayGroups;
  }

  public BusinessArea getBusinessArea() {
    return businessArea;
  }

  public void setBusinessArea(BusinessArea businessArea) {
    this.businessArea = businessArea;
  }

  public long getRequestDisplayId() {
    return requestDisplayId;
  }

  public void setRequestDisplayId(long requestDisplayId) {
    this.requestDisplayId = requestDisplayId;
  }

  public boolean isCanUpdateAction() {
    return canUpdateAction;
  }

  public void setCanUpdateAction(boolean canUpdateAction) {
    this.canUpdateAction = canUpdateAction;
  }

  public long getActionId() {
    return actionId;
  }

  public void setActionId(long actionId) {
    this.actionId = actionId;
  }

  public String getActionUpdateCaption() {
    return actionUpdateCaption;
  }

  public void setActionUpdateCaption(String actionUpdateCaption) {
    this.actionUpdateCaption = actionUpdateCaption;
  }

  public String getDisplayTitle() {
    return displayTitle;
  }

  public void setDisplayTitle(String displayTitle) {
    this.displayTitle = displayTitle;
  }

  public List<Long> getPermissibleActionIds() {
    return permissibleActionIds;
  }

  public void setPermissibleActionIds(List<Long> permissibleActionIds) {
    this.permissibleActionIds = permissibleActionIds;
  }

}
