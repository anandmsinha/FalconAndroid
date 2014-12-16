package com.example.anand.falconproduction.models.create;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anand on 16/12/14.
 *
 * This is a advanced field means it contains all possible information about
 * a field compared to plain Field class which contains minimal info.
 */
public class FieldAdvanced {

  private String fieldType;
  private String fieldDisplayName;
  private long fieldId;
  private String fieldRegexErrorMessage;
  private boolean fieldIsRequired;
  private boolean fieldHasOptions;
  private long fieldUiComponentType;
  private List<FieldOption> fieldOptions;
  private String fieldRequiredErrorMessage;
  private long fieldOrderId;
  private String fieldName;
  private String fieldRegex;

  public FieldAdvanced(JsonElement jsonElement) {
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    fieldType = jsonObject.get("fieldType").getAsString();
    fieldDisplayName = jsonObject.get("fieldDisplayName").getAsString();
    fieldId = jsonObject.get("fieldId").getAsLong();
    fieldRegexErrorMessage = jsonObject.get("fieldRegexErrorMessage").getAsString();
    fieldIsRequired = jsonObject.get("fieldIsRequired").getAsBoolean();
    fieldHasOptions = jsonObject.get("fieldHasOptions").getAsBoolean();
    fieldUiComponentType = jsonObject.get("fieldUiComponentType").getAsLong();
    fieldOptions = new ArrayList<>();
    if (fieldHasOptions) {
      JsonArray jsonArray = jsonObject.get("fieldOptions").getAsJsonArray();
      if (jsonArray != null) {
        for (JsonElement fieldOption : jsonArray) {
          fieldOptions.add(new FieldOption(fieldOption));
        }
      }
    }
    fieldRequiredErrorMessage = jsonObject.get("fieldRequiredErrorMessage").getAsString();
    fieldOrderId = jsonObject.get("fieldOrderId").getAsLong();
    fieldName = jsonObject.get("fieldOrderId").getAsString();
    fieldRegex = jsonObject.get("fieldRegex").getAsString();
  }

}
