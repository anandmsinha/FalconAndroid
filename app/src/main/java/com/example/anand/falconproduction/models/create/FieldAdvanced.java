package com.example.anand.falconproduction.models.create;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.example.anand.falconproduction.activity.utility.MultipleFilesSelectionActivity;
import com.example.anand.falconproduction.utility.UiBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by anand on 16/12/14.
 * <p/>
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

  private View formComponent;
  private HashMap<String, Long> fieldOptionsMap;
  private ArrayList<File> files = new ArrayList<>();

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

  public ArrayList<File> getFiles() {
    return files;
  }

  public void setFiles(ArrayList<File> files) {
    this.files = files;
  }

  public HashMap<String, Long> getFieldOptionsMap() {
    return fieldOptionsMap;
  }

  public void setFieldOptionsMap(HashMap<String, Long> fieldOptionsMap) {
    this.fieldOptionsMap = fieldOptionsMap;
  }

  /**
   * This method return the form component of the field. Here formComponent
   * variable is instantiated and this variable is later used to keep track
   * of value as it is more efficient than using id's to tarck values.
   *
   * @param activity - context
   * @return - form component of field
   */
  public View getUiComponent(final Activity activity) {
    if (formComponent == null) {
      String[] tmpArray = fieldType.split("\\.");
      String actualType = "";
      if (tmpArray.length > 0) {
        actualType = tmpArray[tmpArray.length - 1];
      }
      if (fieldHasOptions) {
        fieldOptionsMap = new HashMap<>();
        for (FieldOption fieldOption : fieldOptions) {
          fieldOptionsMap.put(fieldOption.getDisplayName(), fieldOption.getId());
        }
        formComponent = UiBuilder.createSpinner(activity, fieldOptionsMap.keySet());
      } else if (actualType.equals("StringData") || actualType.equals("IntegerData")) {
        // Not using regex (matches method) above as it is a costly operation and most of the actualTypes will matches StringData in rare case they will try to match beyong that.
        formComponent = UiBuilder.createEditText(activity);
      } else if (actualType.equals("FileDataList")) {
        Button mainUploadButton = UiBuilder.createButton(activity, "Select file");
        mainUploadButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(activity, MultipleFilesSelectionActivity.class);
            activity.startActivityForResult(intent, (int) fieldId);
          }
        });
        formComponent = mainUploadButton;
      } else if (actualType.equals("BooleanData")) {
        formComponent = UiBuilder.createCheckbox(activity);
      } else if (actualType.equals("TextData")) {
        formComponent = UiBuilder.createTextInput(activity);
      } else if (actualType.equals("UserDataList")) {
        formComponent = UiBuilder.createAutoCompleteText(activity);
      } else if (actualType.equals("UserData")) {
        formComponent = UiBuilder.createAutoCompleteText(activity);
      } else {
        formComponent = UiBuilder.createEditText(activity);
      }
    }

    return formComponent;
  }

  @Override
  public String toString() {
    return "FieldAdvanced{" +
        "fieldType='" + fieldType + '\'' +
        ", fieldDisplayName='" + fieldDisplayName + '\'' +
        ", fieldId=" + fieldId +
        ", fieldRegexErrorMessage='" + fieldRegexErrorMessage + '\'' +
        ", fieldIsRequired=" + fieldIsRequired +
        ", fieldHasOptions=" + fieldHasOptions +
        ", fieldUiComponentType=" + fieldUiComponentType +
        ", fieldOptions=" + fieldOptions +
        ", fieldRequiredErrorMessage='" + fieldRequiredErrorMessage + '\'' +
        ", fieldOrderId=" + fieldOrderId +
        ", fieldName='" + fieldName + '\'' +
        ", fieldRegex='" + fieldRegex + '\'' +
        '}';
  }

  public String getFieldType() {
    return fieldType;
  }

  public void setFieldType(String fieldType) {
    this.fieldType = fieldType;
  }

  public String getFieldDisplayName() {
    if (fieldIsRequired) {
      return fieldDisplayName + "*";
    }
    return fieldDisplayName;
  }

  public void setFieldDisplayName(String fieldDisplayName) {
    this.fieldDisplayName = fieldDisplayName;
  }

  public long getFieldId() {
    return fieldId;
  }

  public void setFieldId(long fieldId) {
    this.fieldId = fieldId;
  }

  public String getFieldRegexErrorMessage() {
    return fieldRegexErrorMessage;
  }

  public void setFieldRegexErrorMessage(String fieldRegexErrorMessage) {
    this.fieldRegexErrorMessage = fieldRegexErrorMessage;
  }

  public boolean isFieldIsRequired() {
    return fieldIsRequired;
  }

  public void setFieldIsRequired(boolean fieldIsRequired) {
    this.fieldIsRequired = fieldIsRequired;
  }

  public boolean isFieldHasOptions() {
    return fieldHasOptions;
  }

  public void setFieldHasOptions(boolean fieldHasOptions) {
    this.fieldHasOptions = fieldHasOptions;
  }

  public long getFieldUiComponentType() {
    return fieldUiComponentType;
  }

  public void setFieldUiComponentType(long fieldUiComponentType) {
    this.fieldUiComponentType = fieldUiComponentType;
  }

  public List<FieldOption> getFieldOptions() {
    return fieldOptions;
  }

  public void setFieldOptions(List<FieldOption> fieldOptions) {
    this.fieldOptions = fieldOptions;
  }

  public String getFieldRequiredErrorMessage() {
    return fieldRequiredErrorMessage;
  }

  public void setFieldRequiredErrorMessage(String fieldRequiredErrorMessage) {
    this.fieldRequiredErrorMessage = fieldRequiredErrorMessage;
  }

  public long getFieldOrderId() {
    return fieldOrderId;
  }

  public void setFieldOrderId(long fieldOrderId) {
    this.fieldOrderId = fieldOrderId;
  }

  public String getFieldName() {
    return fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getFieldRegex() {
    return fieldRegex;
  }

  public void setFieldRegex(String fieldRegex) {
    this.fieldRegex = fieldRegex;
  }

}
