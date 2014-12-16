package com.example.anand.falconproduction.models.create;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


/**
 * Created by anand on 16/12/14.
 * <p/>
 * This class represents field options
 */
public class FieldOption {

  private long id;
  private boolean entryRenderUpdateAction;
  private String name;
  private long entryOrder;
  private boolean entryActive;
  private String entryDescription;
  private String displayName;

  public FieldOption(JsonElement jsonElement) {
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    id = jsonObject.get("id").getAsLong();
    entryRenderUpdateAction = jsonObject.get("entryRenderUpdateAction").getAsBoolean();
    name = jsonObject.get("name").getAsString();
    entryOrder = jsonObject.get("entryOrder").getAsLong();
    entryActive = jsonObject.get("entryActive").getAsBoolean();
    entryDescription = jsonObject.get("entryDescription").getAsString();
    displayName = jsonObject.get("displayName").getAsString();
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "FieldOption{" +
        "id=" + id +
        ", entryRenderUpdateAction=" + entryRenderUpdateAction +
        ", name='" + name + '\'' +
        ", entryOrder=" + entryOrder +
        ", entryActive=" + entryActive +
        ", entryDescription='" + entryDescription + '\'' +
        ", displayName='" + displayName + '\'' +
        '}';
  }

  public boolean isEntryRenderUpdateAction() {
    return entryRenderUpdateAction;
  }

  public void setEntryRenderUpdateAction(boolean entryRenderUpdateAction) {
    this.entryRenderUpdateAction = entryRenderUpdateAction;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getEntryOrder() {
    return entryOrder;
  }

  public void setEntryOrder(long entryOrder) {
    this.entryOrder = entryOrder;
  }

  public boolean isEntryActive() {
    return entryActive;
  }

  public void setEntryActive(boolean entryActive) {
    this.entryActive = entryActive;
  }

  public String getEntryDescription() {
    return entryDescription;
  }

  public void setEntryDescription(String entryDescription) {
    this.entryDescription = entryDescription;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }
}
