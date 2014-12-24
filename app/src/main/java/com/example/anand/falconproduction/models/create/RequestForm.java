package com.example.anand.falconproduction.models.create;

import android.view.View;
import android.widget.EditText;

import com.example.anand.falconproduction.utility.FormValidator;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by anand on 16/12/14.
 * <p/>
 * This model represnts create request form.
 */
public class RequestForm {

  private List<DisplayGroupAdvanced> displayGroupsAdvanced;
  private long baId;

  public RequestForm(JsonObject jsonObject, long baId) {
    this.baId = baId;
    displayGroupsAdvanced = new ArrayList<>();
    JsonArray jsonArray = jsonObject.getAsJsonArray("actionDisplayGroups");
    if (jsonArray != null) {
      for (JsonElement jsonElement : jsonArray) {
        displayGroupsAdvanced.add(new DisplayGroupAdvanced(jsonElement));
      }
    }
  }

  public long getBaId() {
    return baId;
  }

  public void setBaId(long baId) {
    this.baId = baId;
  }

  /**
   * This function checks if request form submitted is valid. It returns array
   * list containing error messages. size 0 means no error.
   *
   * @return - ArrayList of errors. (if any)
   */
  public FormValidator isValid() {
    FormValidator formValidator = new FormValidator();
    for (DisplayGroupAdvanced dg : displayGroupsAdvanced) {
      for (Map.Entry<Long, FieldAdvanced> entry : dg.getFieldsMap().entrySet()) {
        FieldAdvanced fieldAdvanced = entry.getValue();
        if (fieldAdvanced.isFieldIsRequired()) {
          if (!fieldAdvanced.isFieldHasOptions()) {
            String[] arr = fieldAdvanced.getFieldType().split("\\.");
            String fieldType = arr[arr.length - 1];
            if (fieldType.equals("FileDataList") || fieldType.equals("FileData")) {
              if (fieldAdvanced.getFiles().size() == 0) {
                formValidator.addMessage(String.format("Field - %s is required", fieldAdvanced.getFieldDisplayName()));
              }
            } else if (!fieldType.equals("BooleanData")) {
              View formComponent = fieldAdvanced.getFormComponent();
              if (formComponent != null && formComponent instanceof EditText) {
                EditText tmpEditText = (EditText) formComponent;
                String val = tmpEditText.getText().toString();
                if (val == null || val.equals("")) {
                  tmpEditText.setError("This field is required");
                  formValidator.setTextError(true);
                }
              }
            }
          }
        }
      }
    }
    return formValidator;
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
