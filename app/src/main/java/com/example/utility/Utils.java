package com.example.utility;

import android.content.Context;
import android.net.ConnectivityManager;

public class Utils {
    public static final String BASE_URL="https://api.imgur.com/3/";
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
    public static boolean checkString(String check) {//true if null::false if present
        if (check == null
                || check.isEmpty()
                || check.equalsIgnoreCase("")
                || check.length() == 0
                || check.equalsIgnoreCase("null")) {
            return true;
        } else
            return false;
    }
    public static String getUrl(String url)
    {
        String myUrl;
        if(!url.contains("//i."))
        {
            try {
                String[] path=url.split("//");
                myUrl=path[0]+"//i."+path[1];
                return myUrl.trim();
            } catch (Exception e) {
                e.printStackTrace();
                return url;
            }
        }
        else
            return url;

    }
}
