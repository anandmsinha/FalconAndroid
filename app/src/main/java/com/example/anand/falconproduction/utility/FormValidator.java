package com.example.anand.falconproduction.utility;

import java.util.ArrayList;

/**
 * Created by anand on 18/12/14.
 * <p/>
 * A class used for storing form validation error messages.
 */
public class FormValidator {

  private boolean textError = false;
  private ArrayList<String> errorMessages = new ArrayList<>();

  public ArrayList<String> getErrorMessages() {
    return errorMessages;
  }

  public void addMessage(String message) {
    errorMessages.add(message);
  }

  public boolean isTextError() {
    return textError;
  }

  public void setTextError(boolean textError) {
    this.textError = textError;
  }

}
