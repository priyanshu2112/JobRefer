package com.example.daivansh.jobreferer.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.daivansh.jobreferer.R;

/**
 * Created by Priyanshu on 06-06-2017.
 */

public class SPHelper {
    public static SharedPreferences sharedPreferences;
    static Context activityContext;






    public SPHelper(Context context, String preference)
    {

        activityContext = context;
        sharedPreferences = activityContext.getSharedPreferences(preference,Context.MODE_PRIVATE);


    }
    public static void putAllData(String... args)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(activityContext.getString(R.string.keyUID),args[0]);
        editor.putString(activityContext.getString(R.string.keyFIRSTNAME),args[1]);
        editor.putString(activityContext.getString(R.string.keyLASTNAME),args[2]);
        editor.putString(activityContext.getString(R.string.keyPHONENO),args[3]);
        editor.putString(activityContext.getString(R.string.keyEMAIL),args[4]);
        editor.putString(activityContext.getString(R.string.keyGENDER),args[5]);
        editor.putString(activityContext.getString(R.string.keyINTRESTS),args[6]);
        editor.putString(activityContext.getString(R.string.keyEXPERIENCE),args[7]);
        editor.putString(activityContext.getString(R.string.keyLOCATION),args[8]);
        editor.commit();
    }
    public static void putData(String key,String value)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();

    }
    public static String getData(String key)
    {
        return sharedPreferences.getString(key,"NOT_PRESENT");
    }

    public static void deleteData(String key)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();

    }
    public static void updateData(String key,String value)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }
    public static void deletePrefernce(String preference)
    {
        sharedPreferences = activityContext.getSharedPreferences(preference,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

}
