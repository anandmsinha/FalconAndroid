package com.example.anand.falconproduction.models.view;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by anand on 1/12/14.
 * <p/>
 * This class represents a field.
 */
public class Field {

  private long componentType;
  private String fieldType;
  private String fieldDisplayName;
  private JsonElement fieldValue;

  public Field(JsonElement jsonElement) {
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    if (jsonObject.has("fieldUiComponentType")) {
      this.componentType = jsonObject.get("fieldUiComponentType").getAsLong();
    } else {
      this.componentType = 0;
    }
    this.fieldDisplayName = jsonObject.get("fieldDisplayName").getAsString();
    this.fieldType = jsonObject.get("fieldType").getAsString();
    this.fieldValue = jsonObject.get("fieldValue");
  }

  public JsonElement getFieldValue() {
    return fieldValue;
  }

  public void setFieldValue(JsonElement fieldValue) {
    this.fieldValue = fieldValue;
  }

  public long getComponentType() {
    return componentType;
  }

  public void setComponentType(long componentType) {
    this.componentType = componentType;
  }

  public String getFieldType() {
    return fieldType;
  }

  public void setFieldType(String fieldType) {
    this.fieldType = fieldType;
  }

  public String getFieldDisplayName() {
    return fieldDisplayName;
  }

  public void setFieldDisplayName(String fieldDisplayName) {
    this.fieldDisplayName = fieldDisplayName;
  }
}
