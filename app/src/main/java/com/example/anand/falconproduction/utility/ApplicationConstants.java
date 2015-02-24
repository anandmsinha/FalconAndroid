package com.example.anand.falconproduction.utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

  public static final String authTokenHeader = "auth-token";

  public static final String clientToken = "clientToken";

  public static final String businessArea = "businessArea";

  public static long clientId = 1L;

  public static final Map<String, String> clientTokes = new HashMap<String, String>() {{
    put("anand", "http://192.168.0.11:8080/falcon-dms/rest/api/");
    put("6081test", "https://mytbits.com:6081/falcon-dms/rest/api/");
    put("3081test", "https://mytbits.com:3081/falcon-dms/rest/api/");
    put("JSPLtest", "https://e-hub.in/falcon-dms/rest/api/");
    put("JPLtest", "https://dms.jindalpower.com/falcon-dms/rest/api/");
    put("UATtest", "http://10.36.1.62/falcon-dms/rest/api/");
  }};

  public static void setBaseAppUrl(String s) {
    baseAppUrl = s;
  }

  public static void setClientId(long id) { clientId = id; }
}
