package com.example.ocbcpayment.base;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {
    private Context context;
    private SharedPreferences sharedPreferences;
    private static PreferenceUtil preferenceUtil;


    public static final String SESSIONKEY = "mobileKey";

    public PreferenceUtil(final Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getEditor() {
        return sharedPreferences.edit();
    }

    public static PreferenceUtil getInstance(Context context) {
        if (preferenceUtil == null) {
            preferenceUtil = new PreferenceUtil(context);
        }
        return preferenceUtil;
    }

    public void setSessionKey(String sessionKey) {
        SharedPreferences.Editor editor = getEditor();

        editor.putString(SESSIONKEY, sessionKey);
        editor.commit();
    }

    public String getSessionKey() {
        return sharedPreferences.getString(SESSIONKEY, "");
    }
}
