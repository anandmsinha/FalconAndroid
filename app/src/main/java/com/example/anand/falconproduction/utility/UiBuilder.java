package com.example.anand.falconproduction.utility;

import android.app.Activity;
import android.graphics.Typeface;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Set;

/**
 * Created by anand on 1/12/14.
 *
 * Helper class for building ui elements.
 */
public class UiBuilder {

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
    AutoCompleteTextView autoCompleteTextView = new AutoCompleteTextView(activity);
    autoCompleteTextView.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_dropdown_item_1line, new String[] {"ram", "balram"}));
    return autoCompleteTextView;
  }
}
