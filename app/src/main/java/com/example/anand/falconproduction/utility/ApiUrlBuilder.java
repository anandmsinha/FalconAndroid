package com.example.anand.falconproduction.utility;

/**
 * Created by anand on 16/12/14.
 *
 * This class creates url's from which api can be accessed.
 */
public class ApiUrlBuilder {

  public static String getRequestForm(long baId) {
    return ApplicationConstants.baseAppUrl
        + "actions/requestForm?clientId=1&baId=" + baId;
  }

}
