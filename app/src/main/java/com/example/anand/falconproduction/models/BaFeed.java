package com.example.anand.falconproduction.models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by anand on 28/11/14.
 *
 * This class represent bafeed the one present in MainActivity
 */
public class BaFeed {

  private long baId;
  private long actionId;
  private long actionDisplayId;
  private long requestId;
  private long requestDisplayId;
  private String actionTitle;
  private String user;
  private String time;

  public BaFeed(long baId, JsonElement jsonElement) {
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    this.baId = baId;
    this.actionId = jsonObject.get("actionId").getAsLong();
    this.actionDisplayId = jsonObject.get("actionDisplayId").getAsLong();
    this.requestId = jsonObject.get("requestId").getAsLong();
    this.requestDisplayId = jsonObject.get("requestDisplayId").getAsLong();
    this.actionTitle = jsonObject.get("mainTitle").getAsString();
    this.user = jsonObject.get("actionUser").getAsString();
    this.time = jsonObject.get("actionLastUpdateTime").getAsString();
  }

  public long getBaId() {
    return baId;
  }

  public void setBaId(long baId) {
    this.baId = baId;
  }

  public long getActionId() {
    return actionId;
  }

  public void setActionId(long actionId) {
    this.actionId = actionId;
  }

  public long getActionDisplayId() {
    return actionDisplayId;
  }

  public void setActionDisplayId(long actionDisplayId) {
    this.actionDisplayId = actionDisplayId;
  }

  public long getRequestId() {
    return requestId;
  }

  public void setRequestId(long requestId) {
    this.requestId = requestId;
  }

  public long getRequestDisplayId() {
    return requestDisplayId;
  }

  public void setRequestDisplayId(long requestDisplayId) {
    this.requestDisplayId = requestDisplayId;
  }

  public String getActionTitle() {
    return actionTitle;
  }

  public void setActionTitle(String actionTitle) {
    this.actionTitle = actionTitle;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }
}
