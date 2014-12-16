package com.example.anand.falconproduction.models.view;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by anand on 1/12/14.
 * <p/>
 * Class represents a display group of and action.
 */
public class DisplayGroup {

  private String title;
  private List<Field> fields;

  public DisplayGroup(JsonElement jsonElement) {
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    this.title = jsonObject.get("actionDisplayGroupTitle").getAsString();
    JsonArray jsonArray = jsonObject.getAsJsonArray("actionDisplayGroupFields");
    this.fields = new ArrayList<Field>();
    if (jsonArray != null) {
      for (JsonElement jsonElement1 : jsonArray) {
        fields.add(new Field(jsonElement1));
      }
    }
  }

  /**
   * Method creates html for fields they all are embedded in a single text view.
   *
   * @return - html value of fields
   */
  public String getFieldsHtml() {
    final String lineBreak = "<br />";
    StringBuilder tmp = new StringBuilder();
    tmp.append(lineBreak);
    for (Field field : fields) {
      tmp.append("<p><b>");
      tmp.append(field.getFieldDisplayName());
      tmp.append(" </b> : ");
      if (field.getFieldType().equals("fieldTypeDefault") || field.getComponentType() != 8) {
        // Means this field is not file type. Check for fieldtype is done first beacuse
        // component type does not exists in many fields.
        tmp.append(field.getFieldValue().getAsString());
        tmp.append(" </p>");
      } else {
        if (field.getFieldValue().isJsonArray()) {
          JsonArray jsonArray = field.getFieldValue().getAsJsonArray();
          if (jsonArray != null && jsonArray.size() > 0) {
            for (JsonElement jsonElement : jsonArray) {
              JsonObject jsonObject = jsonElement.getAsJsonObject();
              if (jsonObject != null) {
                tmp.append(String.format("<a href=\"%s\">%s</a>", jsonObject.get("fieldFileDownloadLink").getAsString(), jsonObject.get("filedFileDisplayName").getAsString()));
              }
            }
            tmp.append("</p>");
          } else {
            tmp.append(" - </p>");
          }
        } else {
          tmp.append(field.getFieldValue().getAsString());
          tmp.append(" </p>");
        }
      }
    }
    return tmp.toString();
  }

  public List<Field> getFields() {
    return fields;
  }

  public void setFields(List<Field> fields) {
    this.fields = fields;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

}
