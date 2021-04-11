package com.example.tourbase;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Utils {
    public static boolean empty( final String s ) {
        return s == null || s.trim().isEmpty();
    }
    public static void updateUsersList(SQLiteDatabase mDB){
        Users.adapter.clear();
        Cursor cursor = mDB.rawQuery("SELECT Login FROM user", null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            Users.adapter.add(cursor.getString(cursor.getColumnIndex("Login")));
            cursor.moveToNext();
        }
        cursor.close();
        Users.adapter.notifyDataSetChanged();
    }
    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }


}
