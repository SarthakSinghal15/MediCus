package com.medicus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;

public class UserSessionManager
{
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    static int userid;
    int PRIVATE_MODE = 0;
    public static final String PREFER_NAME = "MedicusPref";
    public static final String IS_USER_LOGIN = "IsUserLoggedIn";

    public static final String U_NAME = "name";
    public static final String U_ADDR = "address";
    public static final String U_CONTACT = "contact";
    public static final String U_ID = "id";
    public static final String U_EMAIL = "email";
    public static final String U_ECONTACT = "emergency";
    public static final String U_TYPE = "type";

    public UserSessionManager(Context context)
    {
        this.context = context;
        pref = context.getSharedPreferences(PREFER_NAME,PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createUserLoginSession(String id, String name, String addr, String contact, String emergency,String type,String email)
    {
        editor.putBoolean(IS_USER_LOGIN,true);
        editor.putString(U_ID,id);
        editor.putString(U_NAME,name);
        editor.putString(U_ADDR,addr);
        editor.putString(U_CONTACT,contact);
        editor.putString(U_ECONTACT,emergency);
        editor.putString(U_TYPE,type);
        editor.putString(U_EMAIL,email);
        editor.commit();
    }

    public boolean checkLogin()
    {
        Log.i("Session Manager","Inside check login");
        if(!this.isUserLoggedIn())
        {
            Log.i("Session Manager","User not logged in");
            Intent intent = new Intent(context,LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //close all activities from stack
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //add flag to start new activity
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    public boolean isUserLoggedIn()
    {
        Log.i("Session Manager","in isUserLoggedIn()");
        return pref.getBoolean(IS_USER_LOGIN,false);
    }

    public HashMap<String,String> getUserDetails()
    {
        HashMap<String,String> user = new HashMap<>();

        user.put(U_ID,pref.getString(U_ID,null));
        user.put(U_NAME,pref.getString(U_NAME,null));
        user.put(U_ADDR,pref.getString(U_ADDR,null));
        user.put(U_CONTACT,pref.getString(U_CONTACT,null));
        user.put(U_ECONTACT,pref.getString(U_ECONTACT,null));
        user.put(U_TYPE,pref.getString(U_TYPE,null));
        user.put(U_EMAIL,pref.getString(U_EMAIL,null));
        userid = Integer.parseInt(pref.getString(U_ID,null));
        return user;
    }

    public String getUserType()
    {
        return pref.getString(U_TYPE,null);
    }

    public String getUserId() { return pref.getString(U_ID,null); }

    public void logoutUser()
    {
        editor.clear();
        editor.commit();

        Intent intent = new Intent(context,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //close all activities from stack
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //add flag to start new activity
        context.startActivity(intent);
    }

}
