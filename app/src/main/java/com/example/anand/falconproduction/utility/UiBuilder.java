package com.example.anand.falconproduction.utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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

import com.example.anand.falconproduction.R;
import com.example.anand.falconproduction.activity.EditActionActivity;
import com.example.anand.falconproduction.activity.MainActivity;
import com.example.anand.falconproduction.models.ActionModel;
import com.example.anand.falconproduction.models.create.DisplayGroupAdvanced;
import com.example.anand.falconproduction.models.create.FieldAdvanced;
import com.example.anand.falconproduction.models.create.RequestForm;
import com.example.anand.falconproduction.models.view.DisplayGroup;

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

  /**
   * Right now for text input we are using a normal EditText field.
   * Todo - Use a rich text editor here.
   * @param activity - Calling activity
   * @return - Instance of Editor.
   */
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

  /**
   * Create a simple autocomplete text view
   * @param activity - Calling activity
   * @return - Instance of AutoCompleteTextView
   */
  public static AutoCompleteTextView createAutoCompleteText(Activity activity) {
    return new AutoCompleteTextView(activity);
  }

  /**
   * Create a normal progress dialog
   * @param activity - Calling activity
   * @param title - Main title
   * @param content - Main content
   * @return - Instance of ProgressDialog
   */
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

  public static void buildViewActivityUi(final Activity activity, final ActionModel actionModel, Button updateActionBtn, LinearLayout mainLayout, ProgressDialog progressDialog) {
    Log.d(TAG, "buildViewActivityUi called");

    mainLayout.findViewById(R.id.back_to_ba_button).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent finalIntent = new Intent(activity, MainActivity.class);
        activity.startActivity(finalIntent);
      }
    });
    
    if (actionModel.isCanUpdateAction()) {
      updateActionBtn.setVisibility(View.VISIBLE);
      updateActionBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent finalIntent = new Intent(activity, EditActionActivity.class);
          finalIntent.putExtra("actionId", actionModel.getActionId());
          activity.startActivity(finalIntent);
        }
      });
    }
    TextView mainActionTitle = (TextView) mainLayout.findViewById(R.id.main_action_view_title);
    mainActionTitle.setText(actionModel.getDisplayTitle());
    Log.d(TAG, "Size of display groups " + actionModel.getDisplayGroups().size());
    for (DisplayGroup displayGroup : actionModel.getDisplayGroups()) {
      LinearLayout linearLayout = LayoutBuilder.getStandardFalconLayout(activity);
      TextView dispHeading = UiBuilder.createBoldTextView(activity, displayGroup.getTitle());
      linearLayout.addView(dispHeading);
      TextView otherDetails = new TextView(activity);
      otherDetails.setText(Html.fromHtml(displayGroup.getFieldsHtml()));
      otherDetails.setMovementMethod(LinkMovementMethod.getInstance());
      linearLayout.addView(otherDetails);
      mainLayout.addView(linearLayout);
    }
    progressDialog.dismiss();
  }

  /**
   * This methods is used when activity result method is called after file
   * has been selected in filepicker.
   * @param fieldId - fieldid of the file field
   * @param files - list of selected files
   * @param requestForm - - Instance of form
   */
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

  /**
   * General utility method to fill a layout with bold text
   * @param activity - calling activity
   * @param linearLayout - Main layout
   * @param message - Bold message to be shown
   */
  public static void fillLayoutWithMessage(Activity activity, LinearLayout linearLayout, String message) {
    LayoutBuilder.transformToFalconLayout(activity, linearLayout);
    TextView messageText = createBoldTextView(activity, message);
    linearLayout.addView(messageText);
  }
}
