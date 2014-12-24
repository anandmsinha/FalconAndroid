package com.example.anand.falconproduction.activity.utility;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.anand.falconproduction.models.create.DisplayGroupAdvanced;
import com.example.anand.falconproduction.models.create.FieldAdvanced;
import com.example.anand.falconproduction.models.create.RequestForm;
import com.example.anand.falconproduction.utility.ApiUrlBuilder;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by anand on 22/12/14.
 *
 * This class submits the request form.
 */
public class FormSubmit implements Callable<Boolean> {

  RequestForm requestForm;
  Activity mActivity;
  long baId;
  String authToken;

  public FormSubmit(RequestForm requestForm, Activity activity, String authToken, long baId) {
    this.requestForm = requestForm;
    this.mActivity = activity;
    this.baId = baId;
    this.authToken = authToken;
  }

  @Override
  public Boolean call() {
    return handleFormSubmission();
  }

  private boolean handleFormSubmission() {
    boolean outerbreak = false;
    for (DisplayGroupAdvanced displayGroupAdvanced : requestForm.getDisplayGroupsAdvanced()) {
      for (Map.Entry<Long, FieldAdvanced> entry : displayGroupAdvanced.getFieldsMap().entrySet()) {
        FieldAdvanced fieldAdvanced = entry.getValue();
        String[] tmpArray = fieldAdvanced.getFieldType().split("\\.");
        String actualType = "";
        if (tmpArray.length > 0) {
          actualType = tmpArray[tmpArray.length - 1];
        }
        if (fieldAdvanced.getFormComponent() != null) {
          View formComponent = fieldAdvanced.getFormComponent();
          if (fieldAdvanced.isFieldHasOptions()) {
            if (formComponent instanceof Spinner) {
              fieldAdvanced.setFieldValue(((Spinner) formComponent).getSelectedItem().toString());
            }
          } else if (actualType.equals("BooleanData")) {
            if (formComponent instanceof CheckBox) {
              fieldAdvanced.setFieldValue(String.valueOf(((CheckBox) formComponent).isChecked()));
            }
          } else if (actualType.equals("FileData")) {
            if (!fieldAdvanced.getFiles().isEmpty()) {
              String uuid = uploadFile(fieldAdvanced.getFiles().get(0));
              if (uuid.equals("failed")) {
                outerbreak = true;
                mActivity.runOnUiThread(new Runnable() {
                  public void run() {
                    Toast.makeText(mActivity, "File upload failed", Toast.LENGTH_LONG).show();
                  }
                });
                break;
              }
              fieldAdvanced.setFieldValue(uuid);
            }
          } else if (actualType.equals("FileDataList")) {
            if (!fieldAdvanced.getFiles().isEmpty()) {
              boolean wholeBreak = false;
              // we need to clear as in case of retry it will be already poplated.
              fieldAdvanced.getFieldValues().clear();
              for (File file : fieldAdvanced.getFiles()) {
                String uuid = uploadFile(file);
                if (uuid.equals("failed")) {
                  outerbreak = true;
                  wholeBreak = true;
                  mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                      Toast.makeText(mActivity, "File upload failed", Toast.LENGTH_LONG).show();
                    }
                  });

                  break;
                }
                fieldAdvanced.getFieldValues().add(uuid);
              }
              if (wholeBreak) {
                break;
              }
            }
          } else {
            if (formComponent instanceof EditText) {
              String value = ((EditText) formComponent).getText().toString();
              if (actualType.equals("UserDataList") || actualType.equals("StringDataList") || actualType.equals("RealDataList") || actualType.equals("IntegerDataList")) {
                fieldAdvanced.setFieldValues(Arrays.asList(value.split(",")));
              } else {
                fieldAdvanced.setFieldValue(value);
              }
            }
          }
        }
      }
      if (outerbreak) {
        break;
      }
    }
    if (!outerbreak) {
      //progressDialog.setMessage("Data submission started");
      try {
        JsonObject jsonObject = Ion
            .with(mActivity)
            .load(ApiUrlBuilder.submitRequestForm(baId))
            .setHeader("auth-token", authToken)
            .setTimeout(60 * 60 * 1000)
            .setJsonPojoBody(requestForm)
            .asJsonObject()
            .get();
        Log.d("Form data", "This is imporatnt - " + jsonObject.toString());
        return (jsonObject.has("message") && jsonObject.get("message").getAsString().equals("Request created"));
      } catch (Exception e) {
        return false;
      }
    }
    return false;
  }

  private String uploadFile(File file) {
    try {
      // We are blocking the ui beacause until file is uploaded we can't get uuid
      // which is needed to submit form.
      //progressDialog.setMessage("Uploading file " + file.getName());
      JsonObject jsonObject = Ion
          .with(mActivity)
          .load(ApiUrlBuilder.uploadFile(baId))
          .setHeader("auth-token", authToken)
          .setTimeout(60 * 60 * 1000)
          .setMultipartFile("file", file)
          .asJsonObject()
          .get();
      if (jsonObject.has("uuid")) {
        //progressDialog.setMessage("File upload successful.");
        return jsonObject.get("uuid").getAsString();
      }
    } catch (Exception e) {
      // pass
    }
    //progressDialog.setMessage("File upload for " + file.getName() + " failed");
    return "failed";
  }
}
