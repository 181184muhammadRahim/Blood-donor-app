package com.example.assignment1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;

public class DonerDBHelper extends SQLiteOpenHelper implements Serializable {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Doner.db";
    public DonerDBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Donors (Name TEXT, " +
                "District TEXT," +
                "DonateStatus INTEGER," +
                "PhoneNumber TEXT," +
                "PhoneVisibility INTEGER," +
                "BloodGroup TEXT)";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS Donors");
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db,oldVersion,newVersion);
    }
}
