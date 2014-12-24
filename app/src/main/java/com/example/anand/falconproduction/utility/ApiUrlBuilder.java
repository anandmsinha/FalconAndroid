package com.example.anand.falconproduction.utility;

/**
 * Created by anand on 16/12/14.
 *
 * This class creates url's from which api can be accessed.
 */
public class ApiUrlBuilder {

  public static String getClientId() {
    return String.valueOf(ApplicationConstants.clientId);
  }

  public static String getRequestForm(long baId) {
    return ApplicationConstants.baseAppUrl
        + "actions/requestForm?clientId=" + ApplicationConstants.clientId + "&baId=" + baId;
  }

  public static String getUserAutoComplete(String name) {
    return ApplicationConstants.baseAppUrl
        + "actions/usersAutocomplete?clientId=" + ApplicationConstants.clientId + "&name=" + name;
  }

  public static String getLogin() {
    return ApplicationConstants.baseAppUrl
        + "setup/login";
  }

  public static String uploadFile(long baId) {
    return ApplicationConstants.baseAppUrl
        + "request/uploadFile?clientId=" + ApplicationConstants.clientId + "&baId=" + baId;
  }

  public static String submitRequestForm(long baId) {
    return ApplicationConstants.baseAppUrl
        + "request/submitForm?clientId=" + ApplicationConstants.clientId + "&baId=" + baId;
  }
}
