package com.example.anand.falconproduction.utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.anand.falconproduction.models.create.DisplayGroupAdvanced;
import com.example.anand.falconproduction.models.create.FieldAdvanced;
import com.example.anand.falconproduction.models.create.RequestForm;

import java.util.Map;
import java.util.Set;
import java.io.File;
import java.util.List;

/**
 * Created by anand on 1/12/14.
 *
 * Helper class for building ui elements.
 */
public class UiBuilder {

  private static final String TAG = UiBuilder.class.getName();

  public static TextView getTextView(Activity activity, String text) {
    TextView tmpTextView = new TextView(activity);
    tmpTextView.setText(text);
    tmpTextView.setTextSize(14);
    return tmpTextView;
  }

  public static TextView createBoldTextView(Activity activity, String text) {
    TextView tmpTextView = getTextView(activity, text);
    tmpTextView.setTextSize(16);
    tmpTextView.setTypeface(null, Typeface.BOLD);
    return tmpTextView;
  }

  public static Spinner createSpinner(Activity activity, Set<String> values) {
    Spinner spinner = new Spinner(activity);
    spinner.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, values.toArray(new String[values.size()])));
    return spinner;
  }

  public static EditText createEditText(Activity activity) {
    EditText editText = new EditText(activity);
    editText.setSingleLine(true);
    return editText;
  }

  public static EditText createTextInput(Activity activity) {
    EditText editText = createEditText(activity);
    editText.setSingleLine(false);
    return editText;
  }

  public static Button createButton(Activity activity, String text) {
    Button button = new Button(activity);
    button.setText(text);
    return button;
  }

  public static CheckBox createCheckbox(Activity activity) {
    CheckBox checkBox = new CheckBox(activity);
    checkBox.setChecked(true);
    return checkBox;
  }

  public static AutoCompleteTextView createAutoCompleteText(Activity activity) {
    return new AutoCompleteTextView(activity);
  }

  public static ProgressDialog createProgressDialog(Activity activity, String title, String content) {
    ProgressDialog mProgressDialog = new ProgressDialog(activity);
    mProgressDialog.setTitle(title);
    mProgressDialog.setMessage(content);
    mProgressDialog.setIndeterminate(false);
    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    return mProgressDialog;
  }

  /**
   * This method builds the form for create request and update action
   * @param activity - Context
   * @param parentLaout - Main layout of view
   * @param requestForm - Instance of form
   */
  public static void buildFormUi(Activity activity, LinearLayout parentLaout, RequestForm requestForm, View.OnClickListener formSubmitAction) {
    Log.d(TAG, "buildFormUi called");
    for (DisplayGroupAdvanced displayGroupAdvanced : requestForm.getDisplayGroupsAdvanced()) {
      LinearLayout displayGroupBlock = LayoutBuilder.getStandardFalconLayout(activity);
      TextView displayGroupHeading = createBoldTextView(activity, displayGroupAdvanced.getActionDisplayGroupTitle());
      displayGroupBlock.addView(displayGroupHeading);

      for (Map.Entry<Long, FieldAdvanced> fieldEntry : displayGroupAdvanced.getFieldsMap().entrySet()) {
        View formComponentView = fieldEntry.getValue().getUiComponent(activity);
        if (formComponentView != null) {
          displayGroupBlock.addView(createBoldTextView(activity, fieldEntry.getValue().getFieldDisplayName()));
          if (fieldEntry.getValue().getFieldUiComponentType() == 8) {
            displayGroupBlock.addView(fieldEntry.getValue().getFilesNameTextView());
          }
          displayGroupBlock.addView(formComponentView);
        }
      }
      parentLaout.addView(displayGroupBlock);
    }
    LinearLayout processResults = LayoutBuilder.getStandardFalconLayout(activity);
    Button submitFormButton = UiBuilder.createButton(activity, "Submit");
    submitFormButton.setOnClickListener(formSubmitAction);
    processResults.addView(submitFormButton);
    parentLaout.addView(processResults);
  }

  public static void addFilesToList(long fieldId, List<File> files, RequestForm requestForm) {
    if (files != null && !files.isEmpty()) {
      for (DisplayGroupAdvanced displayGroupAdvanced : requestForm.getDisplayGroupsAdvanced()) {
        if (displayGroupAdvanced.getFieldsMap().containsKey(fieldId)) {
          FieldAdvanced tmpFieldAdvanced = displayGroupAdvanced.getFieldsMap().get(fieldId);
          tmpFieldAdvanced.getFiles().addAll(files);
          String existingFiles = tmpFieldAdvanced.getFilesNameTextView().getText().toString();
          for (File file : files) {
            existingFiles += file.getAbsolutePath() + ", ";
          }
          tmpFieldAdvanced.getFilesNameTextView().setText(existingFiles);
          break;
        }
      }
    }
  }

  public static void fillLayoutWithMessage(Activity activity, LinearLayout linearLayout, String message) {
    LayoutBuilder.transformToFalconLayout(activity, linearLayout);
    TextView messageText = createBoldTextView(activity, message);
    linearLayout.addView(messageText);
  }
}
