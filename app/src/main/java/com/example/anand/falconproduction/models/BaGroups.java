package com.example.anand.falconproduction.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anand on 27/11/14.
 * <p/>
 * This class represents a BaMenu or a Business area.
 */
public class BaGroups {

  private boolean isGroup = false;
  private String displayName;
  private boolean yield = false;
  private List<BaGroups> items;
  private long baId;
  private String baName;
  private boolean baReadPermission = false;
  private boolean baCreatePermission = false;

  /**
   * The same constructor is used for creating an entry of type ba or group.
   * If the type of json element is neither baGroup or baMenu then yield will
   * be set to false which means this element should not be rendered in ui.
   *
   * @param element - JsonElement representing bagroup or baMenu
   */
  public BaGroups(JsonElement element) {
    JsonObject jsonObject = element.getAsJsonObject();
    String type = jsonObject.get("type").getAsString();
    if (type.equals("baMenu")) {
      isGroup = true;
      processBaGroup(jsonObject);
    } else if (type.equals("businessArea")) {
      processBusinessArea(jsonObject);
    }
  }

  /**
   * This method will return all BaMenu which are ba's inside the current menu.
   * @return - list of bamenu's which are ba's
   */
  public List<BaGroups> getAllBaInGroup() {
    ArrayList<BaGroups> output = new ArrayList<>();
    if (isGroup && items != null && !items.isEmpty()) {
      for (BaGroups baGroups : items) {
        if (baGroups.isGroup()) {
          output.addAll(baGroups.getAllBaInGroup());
        } else {
          output.add(baGroups);
        }
      }
    }
    return output;
  }

  public boolean isGroup() {
    return isGroup;
  }

  public void setGroup(boolean isGroup) {
    this.isGroup = isGroup;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public boolean isYield() {
    return yield;
  }

  public void setYield(boolean yield) {
    this.yield = yield;
  }

  public List<BaGroups> getItems() {
    return items;
  }

  public void setItems(List<BaGroups> items) {
    this.items = items;
  }

  public long getBaId() {
    return baId;
  }

  public void setBaId(long baId) {
    this.baId = baId;
  }

  public String getBaName() {
    return baName;
  }

  public void setBaName(String baName) {
    this.baName = baName;
  }

  public boolean isBaReadPermission() {
    return baReadPermission;
  }

  public void setBaReadPermission(boolean baReadPermission) {
    this.baReadPermission = baReadPermission;
  }

  public boolean isBaCreatePermission() {
    return baCreatePermission;
  }

  public void setBaCreatePermission(boolean baCreatePermission) {
    this.baCreatePermission = baCreatePermission;
  }

  public int getChildrenCount() {
    if (isGroup) {
      return items.size();
    }
    return 0;
  }

  @Override
  public String toString() {
    return displayName;
  }

  private void processBaGroup(JsonObject baGroupJsonObject) {
    displayName = baGroupJsonObject.get("name").getAsString();
    items = new ArrayList<BaGroups>();
    JsonArray itemsArray = baGroupJsonObject.getAsJsonArray("items");
    if (itemsArray != null) {
      for (JsonElement baElement : itemsArray) {
        items.add(new BaGroups(baElement));
      }
    }
    yield = true;
  }

  private void processBusinessArea(JsonObject baJsonObject) {
    displayName = baJsonObject.get("name").getAsString();
    baId = Long.parseLong(baJsonObject.get("id").getAsString());
    baName = baJsonObject.get("ba_name").getAsString();
    JsonArray baPermissionJsonArray =
      baJsonObject.getAsJsonArray("ba_permissions");
    for (JsonElement permissionElement : baPermissionJsonArray) {
      if (permissionElement != null) {
        try {
          String permissionString = permissionElement.getAsString();
          if (permissionString.equals("read")) {
            baReadPermission = true;
          } else if (permissionString.equals("create")) {
            baCreatePermission = true;
          }
        } catch (Exception e) {

        }
      }
    }
    yield = true;
  }
}
