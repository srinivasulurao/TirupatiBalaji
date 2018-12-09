package com.example.nsrin.tirupatibalaji;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="drupal_db";
    public static final String TABLE_NAME="drupal_table";
    public static final String col_1="id";
    public static final String col_2="username";
    public static final String col_3="password";
    public static final String col_4="csrf_token";
    public static final String col_5="logout_token";


    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL("CREATE TABLE "+TABLE_NAME+" (id INTEGER PRIMARY KEY AUTOINCREMENT, username, password, csrf_token, logout_token) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
      onCreate(db);
    }
    public boolean insertData(String username, String password, String csrf_token, String logout_token){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(col_2,username);
        cv.put(col_3,password);
        cv.put(col_4,csrf_token);
        cv.put(col_5,logout_token);

        long result= db.insert(TABLE_NAME, null, cv);

        if(result==-1)
            return false;
        else 
            return true;
    }
}
