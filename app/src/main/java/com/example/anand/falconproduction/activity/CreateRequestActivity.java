package com.example.anand.falconproduction.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anand.falconproduction.R;
import com.example.anand.falconproduction.activity.utility.MultipleFilesSelectionActivity;
import com.example.anand.falconproduction.interfaces.ProcessAfterDrawer;
import com.example.anand.falconproduction.models.create.DisplayGroupAdvanced;
import com.example.anand.falconproduction.models.create.FieldAdvanced;
import com.example.anand.falconproduction.models.create.RequestForm;
import com.example.anand.falconproduction.utility.ApiUrlBuilder;
import com.example.anand.falconproduction.utility.LayoutBuilder;
import com.example.anand.falconproduction.utility.UiBuilder;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by anand on 16/12/14.
 * <p/>
 * Activity class to handle create request of Ba.
 */
public class CreateRequestActivity extends BaseDrawerActivity implements ProcessAfterDrawer {

  private static final String TAG = "CreateRequestActivity";
  long baId;
  RequestForm requestForm;

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

            requestForm = new RequestForm(result);
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
    super.startActivity(intent);
  }

  /**
   * Set files parameter of FieldAdvanced after recieving intent data from file upload
   * activity
   *
   * @param requestCode - id of FieldAdvanced
   * @param resultCode - result code
   * @param data - actual data
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
    }
    progressDialog.dismiss();
  }

  @Override
  public long getBaId() {
    return baId;
  }
}
