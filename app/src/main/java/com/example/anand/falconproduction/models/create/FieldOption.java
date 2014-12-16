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
}
