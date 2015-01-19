package com.example.anand.falconproduction.utility;

/**
 * Created by anand on 26/11/14.
 * <p>
 * This class contains all application level constants which will just be used on android.
 * </p>
 */
public class ApplicationConstants {
  public static final String appSharedPreference = "app.falcon.production";

  public static final String appAuthToken = "jiberkiuo";

  public static String baseAppUrl;

  public static final String serverResponseToken = "token";

  public static final String clientToken = "clientToken";

  public static long clientId = 1L;

  public static void setBaseAppUrl(String s) {
    baseAppUrl = s;
  }

  public static void setClientId(long id) { clientId = id; }
}
