package com.example.anand.falconproduction.models.create;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.anand.falconproduction.R;
import com.example.anand.falconproduction.activity.utility.MultipleFilesSelectionActivity;
import com.example.anand.falconproduction.adapters.UserAutoCompleteAdapter;
import com.example.anand.falconproduction.fragments.DateTimePicker;
import com.example.anand.falconproduction.utility.DateFormatUtil;
import com.example.anand.falconproduction.utility.UiBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by anand on 16/12/14.
 * <p/>
 * This is a advanced field means it contains all possible information about
 * a field compared to plain Field class which contains minimal info.
 */
public class FieldAdvanced {

  private transient static final String TAG = FieldAdvanced.class.getName();

  private String fieldType;
  private transient String fieldDisplayName;
  private long fieldId;
  private transient String fieldRegexErrorMessage;
  private boolean fieldIsRequired;
  private boolean fieldHasOptions;
  private transient long fieldUiComponentType;
  private transient List<FieldOption> fieldOptions;
  private transient String fieldRequiredErrorMessage;
  private transient long fieldOrderId;
  private String fieldName;
  private transient String fieldRegex;
  private String fieldValue;
  private List<String> fieldValues = new ArrayList<>();
  // These two fields track old value of the field.
  private transient boolean isEditMode;

  public String getOldFieldValue() {
    return oldFieldValue;
  }

  private transient String oldFieldValue;
  private transient List<String> oldFieldValues = new ArrayList<>();
  // The following 5 properties are limited to filelist data type
  private transient View formComponent;
  private transient HashMap<String, String> fieldOptionsMap;
  private transient ArrayList<File> files = new ArrayList<>();
  private transient TextView filesNameTextView;

  public FieldAdvanced(JsonElement jsonElement) {
    this(jsonElement, false);
  }

  public FieldAdvanced(JsonElement jsonElement, boolean isEdit) {
    this.isEditMode = isEdit;
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
    /**
     * Fill old values if it is update form.
     */
    if (isEdit && jsonObject.has("fieldValue")) {
      JsonElement tmpJsonElement = jsonObject.get("fieldValue");
      if (tmpJsonElement.isJsonArray()) {
        JsonArray tmpJsonArray = tmpJsonElement.getAsJsonArray();
        for (JsonElement tmpValue : tmpJsonArray) {
          oldFieldValues.add(tmpValue.getAsString());
        }
      } else {
        String tmpVal = tmpJsonElement.getAsString();
        if (!"null".equals(tmpVal)) {
          oldFieldValue = tmpVal;
        }
      }
    }
    fieldRequiredErrorMessage = jsonObject.get("fieldRequiredErrorMessage").getAsString();
    fieldOrderId = jsonObject.get("fieldOrderId").getAsLong();
    fieldName = jsonObject.get("fieldOrderId").getAsString();
    fieldRegex = jsonObject.get("fieldRegex").getAsString();
  }

  public boolean isEditMode() {
    return isEditMode;
  }

  public List<String> getFieldValues() {
    return fieldValues;
  }

  public void setFieldValues(List<String> fieldValues) {
    this.fieldValues = fieldValues;
  }

  public String getFieldValue() {
    return fieldValue;
  }

  public void setFieldValue(String fieldValue) {
    this.fieldValue = fieldValue;
  }

  public View getFormComponent() {
    return formComponent;
  }

  public TextView getFilesNameTextView() {
    return filesNameTextView;
  }

  public void setFilesNameTextView(TextView filesNameTextView) {
    this.filesNameTextView = filesNameTextView;
  }

  public ArrayList<File> getFiles() {
    return files;
  }

  public void setFiles(ArrayList<File> files) {
    this.files = files;
  }

  public HashMap<String, String> getFieldOptionsMap() {
    return fieldOptionsMap;
  }

  public void setFieldOptionsMap(HashMap<String, String> fieldOptionsMap) {
    this.fieldOptionsMap = fieldOptionsMap;
  }

  /**
   * This method returns the form component of the field. Here formComponent
   * variable is instantiated and this variable is later used to keep track
   * of value as it is more efficient than using id's to track values.
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
        boolean shouldCheck = isEditMode && oldFieldValue != null;
        String toFillValue = null;
        if (shouldCheck) {
          for (FieldOption fieldOption : fieldOptions) {
            if (fieldOption.getName().equals(oldFieldValue)) {
              toFillValue = fieldOption.getDisplayName();
            }
            fieldOptionsMap.put(fieldOption.getDisplayName(), fieldOption.getName());
          }
        } else {
          for (FieldOption fieldOption : fieldOptions) {
            fieldOptionsMap.put(fieldOption.getDisplayName(), fieldOption.getName());
          }
        }
        Spinner mainSpinner = UiBuilder.createSpinner(activity, fieldOptionsMap.keySet());
        if (toFillValue != null) {
          // No need to cast again as it has already been set to array adapter in UiBuilder
          int position = ((ArrayAdapter<String>) mainSpinner.getAdapter()).getPosition(toFillValue);
          if (position >= 0) {
            mainSpinner.setSelection(position);
          }
        }
        formComponent = mainSpinner;
      } else if (actualType.equals("StringData") || actualType.equals("IntegerData") || actualType.equals("RealData")) {
        // Not using regex (matches method) above as it is a costly operation and most of the actualTypes will matches StringData in rare case they will try to match beyong that.
        EditText mainText = UiBuilder.createEditText(activity);
        if (isEditMode && oldFieldValue != null) {
          mainText.setText(oldFieldValue);
        }
        formComponent = mainText;
      } else if (actualType.equals("FileDataList") || actualType.equals("FileData")) {
        Button mainUploadButton = UiBuilder.createButton(activity, activity.getResources().getString(R.string.select_file));
        mainUploadButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(activity, MultipleFilesSelectionActivity.class);
            activity.startActivityForResult(intent, (int) fieldId);
          }
        });
        formComponent = mainUploadButton;
        // Create a list view
        String valToSet = "";
        if ("FileData".equals(actualType) && oldFieldValue != null) {
          valToSet = oldFieldValue;
        } else if ("FileDataList".equals(actualType) && !oldFieldValues.isEmpty()) {
          for (String tmp : oldFieldValues) {
            valToSet = valToSet + tmp + ", ";
          }
        }
        final String tmpFinalVal = valToSet;
        filesNameTextView = UiBuilder.getTextView(activity, valToSet);
        filesNameTextView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            // for removing files
            if (!files.isEmpty()) {
              String[] fileNames = new String[files.size()];
              boolean[] filesChecked = new boolean[files.size()];
              final ArrayList<File> finalList = new ArrayList<>(files);
              for (int i = 0, _l = files.size(); i < _l; ++i) {
                fileNames[i] = files.get(i).getAbsolutePath();
                filesChecked[i] = true;
              }
              AlertDialog.Builder builder = new AlertDialog.Builder(activity);
              builder.setTitle(activity.getResources().getString(R.string.uncheck_files));
              builder.setMultiChoiceItems(fileNames, filesChecked, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                  if (isChecked && finalList.get(which) == null) {
                    finalList.set(which, files.get(which));
                  } else if (!isChecked) {
                    finalList.set(which, null);
                  }
                }
              }).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                  finalList.removeAll(Collections.singleton(null));
                  files = finalList;
                  StringBuilder finalNameToBeSet = new StringBuilder();
                  finalNameToBeSet.append(tmpFinalVal);
                  for (File file : files) {
                    finalNameToBeSet.append(file.getAbsolutePath());
                    finalNameToBeSet.append(", ");
                  }
                  filesNameTextView.setText(finalNameToBeSet.toString());
                }
              }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
              });
              AlertDialog finalFileDialog = builder.create();
              finalFileDialog.show();
            }
          }
        });
      } else if (actualType.equals("BooleanData")) {
        CheckBox mainCheckbox = UiBuilder.createCheckbox(activity);
        if (isEditMode && oldFieldValue != null) {
          if ("true".equalsIgnoreCase(oldFieldValue)) {
            mainCheckbox.setChecked(true);
          }
        }
        formComponent = mainCheckbox;
      } else if (actualType.equals("TextData")) {
        EditText mainText = UiBuilder.createTextInput(activity);
        if (isEditMode && oldFieldValue != null) {
          mainText.setText(oldFieldValue);
        }
        formComponent = mainText;
      } else if (actualType.equals("UserDataList")) {
        MultiAutoCompleteTextView multiAutoCompleteTextView = new MultiAutoCompleteTextView(activity);
        multiAutoCompleteTextView.setAdapter(new UserAutoCompleteAdapter(activity, android.R.layout.simple_dropdown_item_1line));
        multiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        if (isEditMode && !oldFieldValues.isEmpty()) {
          StringBuilder output = new StringBuilder();
          for (String tmp : oldFieldValues) {
            output.append(tmp).append(", ");
          }
          multiAutoCompleteTextView.setText(output.toString());
        }
        formComponent = multiAutoCompleteTextView;
      } else if (actualType.equals("UserData")) {
        AutoCompleteTextView autoCompleteTextView = UiBuilder.createAutoCompleteText(activity);
        autoCompleteTextView.setAdapter(new UserAutoCompleteAdapter(activity, android.R.layout.simple_dropdown_item_1line));
        if (isEditMode && oldFieldValue != null) {
          autoCompleteTextView.setText(oldFieldValue);
        }
        formComponent = autoCompleteTextView;
      } else if (actualType.equals("DateData")) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM d, yyyy");
        final EditText dateInputText = UiBuilder.createEditText(activity);
        dateInputText.setInputType(InputType.TYPE_NULL);
        Calendar newCalender = Calendar.getInstance();
        if (isEditMode && oldFieldValue != null) {
          // In many case there has been format error so convert the format
          try {
            Date newDate = DateFormatUtil.parse(oldFieldValue);
            dateInputText.setText(simpleDateFormat.format(newDate));
          } catch (Exception e) {
            Log.e(TAG, "Error in old date format");
          }
        }
        final DatePickerDialog pickDateDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
          @Override
          public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            dateInputText.setText(simpleDateFormat.format(newDate.getTime()));
          }
        }, newCalender.get(Calendar.YEAR), newCalender.get(Calendar.MONTH), newCalender.get(Calendar.DAY_OF_MONTH));
        dateInputText.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            pickDateDialog.show();
          }
        });
        formComponent = dateInputText;
      } else if (actualType.equals("DateTimeData")) {
        /**
         * A custom component has been created to handle date time input.
         */
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d-MMM-yyyy,HH:mm:ss aaa");
        final EditText dateInputText = UiBuilder.createEditText(activity);
        dateInputText.setInputType(InputType.TYPE_NULL);
        if (isEditMode && oldFieldValue != null) {
          try {
            Log.d(TAG, "String recieved - " + oldFieldValue);
            Date newDate = DateFormatUtil.parse(oldFieldValue);
            dateInputText.setText(simpleDateFormat.format(newDate));
          } catch (Exception e) {
            Log.e(TAG, "Error in old date time format - " + e.getMessage());
          }
        }
        final Dialog dateTimeDialog = new Dialog(activity);
        final RelativeLayout mDateTimeDialogView =
            (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.date_time_dialog, null);
        final DateTimePicker mDateTimePicker =
            (DateTimePicker) mDateTimeDialogView.findViewById(R.id.DateTimePicker);

        mDateTimeDialogView.findViewById(R.id.SetDateTime).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Log.d("fAdvanced", "value recieved - " + mDateTimePicker.get());
            mDateTimePicker.clearFocus();
            dateInputText.setText(simpleDateFormat.format(mDateTimePicker.get().getTime()));
            dateTimeDialog.dismiss();
          }
        });

        mDateTimeDialogView.findViewById(R.id.CancelDialog).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            dateTimeDialog.cancel();
          }
        });

        mDateTimeDialogView.findViewById(R.id.ResetDateTime).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            mDateTimePicker.reset();
          }
        });
        dateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dateTimeDialog.setContentView(mDateTimeDialogView);
        dateInputText.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            dateTimeDialog.show();
          }
        });

        formComponent = dateInputText;
      } else {
        EditText editText = UiBuilder.createEditText(activity);
        if (isEditMode) {
          if (oldFieldValue != null) {
            editText.setText(oldFieldValue);
          } else if (!oldFieldValues.isEmpty()) {
            StringBuilder output = new StringBuilder();
            for (String tmp : oldFieldValues) {
              output.append(tmp).append(", ");
            }
            editText.setText(output.toString());
          }
        }
        formComponent = editText;
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

  public String getFieldDisplayName() {
    if (fieldIsRequired) {
      return fieldDisplayName + "*";
    }
    return fieldDisplayName;
  }

  public long getFieldId() {
    return fieldId;
  }

  public boolean isFieldIsRequired() {
    return fieldIsRequired;
  }

  public boolean isFieldHasOptions() {
    return fieldHasOptions;
  }

  public long getFieldUiComponentType() {
    return fieldUiComponentType;
  }

}
