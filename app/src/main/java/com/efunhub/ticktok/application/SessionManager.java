package com.efunhub.ticktok.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.efunhub.ticktok.R;

public class SessionManager extends Application {
    private static Context mContext;
    private static SharedPreferences mSharedPreferences;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        //Obtain the shared preference instance.
        mSharedPreferences = mContext.getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);

    }

    public static Context getContext() {
        return mContext;
    }

    public static void onSaveLoginDetails(String auto_customer_id,String name, String mobile,
                                          String email, String country, String otp_status,
                                          String status, String token,String points,String aiifa_reg) {

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("AUTOCUSTOMERID", auto_customer_id);
        editor.putString("NAME", name);
        editor.putString("MOBILE", mobile);
        editor.putString("EMAIL", email);
        editor.putString("COUNTRY", country);
        editor.putString("OTP_STATUS", otp_status);
        editor.putString("STATUS", status);
        editor.putString("TOKEN", token);
        editor.putString("POINTS", points);
        editor.putString("AIIFA", aiifa_reg);
        editor.commit();
    }

    public static void onSavePoints(String points) {

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("POINTS", points);
        editor.commit();
    }
    public static void onSaveAiifaRegistration(String reg) {

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("AIIFA", reg);
        editor.commit();
    }

    public static void onSaveCampaignData(String userId) {

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("user_auto_id", userId);
        editor.commit();
    }
    public static String onGetCampaignData() {
        return mSharedPreferences.getString("user_auto_id", "");
    }

    public static String onGetAiifaRegistration() {
        return mSharedPreferences.getString("AIIFA", "");
    }

    public static String onGetAutoCustomerId() {
        return mSharedPreferences.getString("AUTOCUSTOMERID", "");
    }

    public static String onGetPoints() {
        return mSharedPreferences.getString("POINTS", "");
    }

    public static String onGetCustomerName() {
        return mSharedPreferences.getString("NAME", "");
    }


}