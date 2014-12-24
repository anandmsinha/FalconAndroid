package com.example.anand.falconproduction.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anand.falconproduction.R;
import com.example.anand.falconproduction.interfaces.ProcessAfterDrawer;
import com.example.anand.falconproduction.models.create.DisplayGroupAdvanced;
import com.example.anand.falconproduction.models.create.FieldAdvanced;
import com.example.anand.falconproduction.models.create.RequestForm;
import com.example.anand.falconproduction.utility.ApiUrlBuilder;
import com.example.anand.falconproduction.utility.FormValidator;
import com.example.anand.falconproduction.utility.LayoutBuilder;
import com.example.anand.falconproduction.utility.UiBuilder;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by anand on 16/12/14.
 * <p/>
 * Activity class to handle create request of Ba.
 */
public class CreateRequestActivity extends BaseDrawerActivity implements ProcessAfterDrawer, View.OnClickListener {

  private static final String TAG = "CreateRequestActivity";
  long baId;
  RequestForm requestForm;
  ProgressDialog mProgressDialog;

  @Override
  protected void onCreate(Bundle savedInstance) {
    super.onCreate(savedInstance);
    setContentView(R.layout.create_request, this);
  }

  @Override
  public void fillDetails(int groupCount) {
    baId = intentBundle.getLong("baId");
    final ProgressDialog progressDialog = new ProgressDialog(this);
    progressDialog.setTitle("Loading....");
    progressDialog.setIndeterminate(false);
    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    progressDialog.show();
    Ion.with(this)
        .load(ApiUrlBuilder.getRequestForm(baId))
        .setHeader("auth-token", authToken)
        .asJsonObject()
        .setCallback(new FutureCallback<JsonObject>() {
          @Override
          public void onCompleted(Exception e, JsonObject result) {
            if (e != null || !result.has("actionDisplayGroups")) {
              Toast.makeText(CreateRequestActivity.this, "Failed to load form try again", Toast.LENGTH_LONG).show();
              progressDialog.dismiss();
              return;
            }

            requestForm = new RequestForm(result, baId);
            buildUi(progressDialog);
          }
        });
  }

  /**
   * This method has been overriden so that we can put ba id into intent.
   *
   * @param intent - passing intent
   */
  @Override
  public void startActivity(Intent intent) {
    intent.putExtra("baId", baId);
    intent.putExtra("group", groupId);
    super.startActivity(intent);
  }

  /**
   * Set files parameter of FieldAdvanced after recieving intent data from file upload
   * activity
   *
   * @param requestCode - id of FieldAdvanced
   * @param resultCode  - result code
   * @param data        - actual data
   */
  @Override
  @SuppressWarnings("unchecked")
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode > 0 && resultCode == RESULT_OK) {
      ArrayList<File> files = (ArrayList<File>) data.getSerializableExtra("upload");
      if (files != null && !files.isEmpty()) {
        for (DisplayGroupAdvanced displayGroupAdvanced : requestForm.getDisplayGroupsAdvanced()) {
          if (displayGroupAdvanced.getFieldsMap().containsKey((long) requestCode)) {
            FieldAdvanced tmpFieldAdvanced = displayGroupAdvanced.getFieldsMap().get((long) requestCode);
            // Todo - Handle case for same file selected twice.
            tmpFieldAdvanced.getFiles().addAll(files);
            Log.d(TAG, "Size of files recieved " + files.size());
            String existingFiles = tmpFieldAdvanced.getFilesNameTextView().getText().toString();
            for (File file : files) {
              existingFiles += file.getAbsolutePath() + ", ";
            }
            tmpFieldAdvanced.getFilesNameTextView().setText(existingFiles);
          }
        }
      }
    }
  }

  private void buildUi(ProgressDialog progressDialog) {
    LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_request_create_content);
    if (requestForm != null) {
      for (DisplayGroupAdvanced displayGroupAdvanced : requestForm.getDisplayGroupsAdvanced()) {
        LinearLayout displayGroupBlock = LayoutBuilder.getStandardFalconLayout(this);
        TextView dispHeading = UiBuilder.createBoldTextView(this, displayGroupAdvanced.getActionDisplayGroupTitle());
        displayGroupBlock.addView(dispHeading);

        for (Map.Entry<Long, FieldAdvanced> entry : displayGroupAdvanced.getFieldsMap().entrySet()) {
          View formView = entry.getValue().getUiComponent(this);
          if (formView != null) {
            displayGroupBlock.addView(UiBuilder.createBoldTextView(this, entry.getValue().getFieldDisplayName()));
            if (entry.getValue().getFieldUiComponentType() == 8) {
              displayGroupBlock.addView(entry.getValue().getFilesNameTextView());
            }
            displayGroupBlock.addView(formView);
          }
        }
        mainLayout.addView(displayGroupBlock);
      }
      LinearLayout processResults = LayoutBuilder.getStandardFalconLayout(this);
      Button submitFormButton = UiBuilder.createButton(this, "Submit");
      submitFormButton.setOnClickListener(this);
      processResults.addView(submitFormButton);
      mainLayout.addView(processResults);
    }
    progressDialog.dismiss();
  }

  @Override
  public void onClick(View v) {
    v.setEnabled(false);

    FormValidator formValidator = requestForm.isValid();
    if (formValidator.getErrorMessages().isEmpty()) {
      if (!formValidator.isTextError()) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Submission in progress");
        mProgressDialog.setMessage("Preparing form");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        new SubmitRequestForm().execute(requestForm);
      }
    } else {
      AlertDialog.Builder builder = new AlertDialog.Builder(CreateRequestActivity.this);
      StringBuilder stringBuilder = new StringBuilder();
      for (String tmp : formValidator.getErrorMessages()) {
        stringBuilder.append(tmp);
      }
      builder.setMessage(stringBuilder.toString())
          .setCancelable(false)
          .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
          });
      AlertDialog alertDialog = builder.create();
      alertDialog.show();
    }
    v.setEnabled(true);
  }

  @Override
  public long getBaId() {
    return baId;
  }

  /**
   * This function submits the form in background thread.
   *
   * @param progressDialog - progress dialog
   */
 /* private void handleSubmission(ProgressDialog progressDialog) {
    FormSubmit formSubmit = new FormSubmit(requestForm, this, authToken, baId);
    FutureTask<Boolean> submissionData = new FutureTask<>(formSubmit);
    submissionData.run();
    // wait for task to complete.
    while (true) {
      try {
        Boolean data = submissionData.get(100L, TimeUnit.MILLISECONDS);
        if (data != null) {
          progressDialog.dismiss();
          if (data) {
            Toast.makeText(this, "Success !!!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
          } else {
            Toast.makeText(this, "Form submission failed.", Toast.LENGTH_LONG).show();
          }
          break;
        }
      } catch (Exception e) {
        progressDialog.dismiss();
        Toast.makeText(this, "Form submission failed.", Toast.LENGTH_LONG).show();
        break;
      }
    }
  }*/

  /**
   * Right now we are using asynctask later on we need to find better solution
   * to this as file upload and process can be really long. Using Async Task here
   * is not totally incorrect as The main problem with long running process in AsyncTask
   * is memory leak due t change of activty but we are blocking our activity son chances
   * of memory leak should not be there.
   */
  class SubmitRequestForm extends AsyncTask<RequestForm, String, Boolean> {

    RequestForm mainForm;

    protected void onPreExecute() {
      super.onPreExecute();
      mProgressDialog.show();
    }

    protected Boolean doInBackground(RequestForm... requestForms) {
      mainForm = requestForms[0];
      return handleFormSubmission();
    }

    protected void onProgressUpdate(String... messages) {
      mProgressDialog.setMessage(messages[0]);
    }

    @Override
    protected void onPostExecute(Boolean status) {
      if (status) {
        mProgressDialog.dismiss();
        Toast.makeText(CreateRequestActivity.this, "Request created", Toast.LENGTH_LONG).show();
        Intent mainIntent = new Intent(CreateRequestActivity.this, MainActivity.class);
        startActivity(mainIntent);
        return;
      } Toast.makeText(CreateRequestActivity.this, "Request creation failed.", Toast.LENGTH_LONG).show();
    }

    private boolean handleFormSubmission() {
      // Fill data in request form.
      boolean outerbreak = false; // outerbreak is variable which is used to track file upload failure. In case of file upload fail we cancel form submission.
      for (DisplayGroupAdvanced displayGroupAdvanced : mainForm.getDisplayGroupsAdvanced()) {
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
                  publishProgress("File upload failed");
                  break;
                }
                fieldAdvanced.setFieldValue(uuid);
              }
            } else if (actualType.equals("FileDataList")) {
              if (!fieldAdvanced.getFiles().isEmpty()) {
                boolean wholeBreak = false;
                // we need to clear as in case of retry it will be already populated.
                fieldAdvanced.getFieldValues().clear();
                for (File file : fieldAdvanced.getFiles()) {
                  String uuid = uploadFile(file);
                  if (uuid.equals("failed")) {
                    outerbreak = true;
                    wholeBreak = true;
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
                  String [] tmpVals = value.split(",");
                  List<String> values = new ArrayList<>();
                  for (String tmp : tmpVals) {
                    // If we don't do this this add empty fields in
                    if (!tmp.equals("")) {
                      values.add(tmp);
                    }
                  }
                  fieldAdvanced.setFieldValues(values);
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

      // If file upload has not failed the submit form
      if (!outerbreak) {
        publishProgress("Data submission started");
        try {
          JsonObject jsonObject = Ion
              .with(CreateRequestActivity.this)
              .load(ApiUrlBuilder.submitRequestForm(baId))
              .setHeader("auth-token", authToken)
              .setTimeout(60 * 60 * 1000)
              .setJsonPojoBody(mainForm)
              .asJsonObject()
              .get();
          publishProgress(jsonObject.get("message").toString());
          if (jsonObject.has("message")) {
            if (jsonObject.get("message").getAsString().equals("Request created")) {
              return true;
            }
          }
          return false;
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
        publishProgress("Uploading file " + file.getName());
        JsonObject jsonObject = Ion
            .with(CreateRequestActivity.this)
            .load(ApiUrlBuilder.uploadFile(baId))
            .setHeader("auth-token", authToken)
            .setTimeout(60 * 60 * 1000)
            .setMultipartFile("file", file)
            .asJsonObject()
            .get();
        if (jsonObject.has("uuid")) {
          publishProgress("File upload successful.");
          return jsonObject.get("uuid").getAsString();
        }
      } catch (Exception e) {
        // pass
      }
      publishProgress("File upload for " + file.getName() + " failed");
      return "failed";
    }
  }
}
