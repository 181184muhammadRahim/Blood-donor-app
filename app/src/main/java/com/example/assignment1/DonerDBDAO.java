package com.example.assignment1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class DonerDBDAO implements IDonerDAO, Serializable {
    private Context context;
    public DonerDBDAO(Context ctx){
        context = ctx;
    }
    @Override
    public void save(Donor d) {
        DonerDBHelper dbHelper = new DonerDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("Name",d.Name);
        content.put("District",d.District);
        content.put("DonateStatus",d.DonateStatus);
        content.put("PhoneNumber",d.PhoneNumber);
        content.put("PhoneVisibility",d.PhoneVisibility);
        content.put("BloodGroup",d.BloodGroup);
        db.insert("Donors",null,content);
    }

    @Override
    public void save(ArrayList<Donor> objects) {
        for (Donor obj: objects){
            save(obj);
        }
    }

    @Override
    public ArrayList<Hashtable<String, String>> load() {
        DonerDBHelper dbHelper = new DonerDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM Donors";
        Cursor cursor = db.rawQuery(query,null);
        ArrayList<Hashtable<String,String>> objects = new

                ArrayList<Hashtable<String, String>>();

        while(cursor.moveToNext()){
            Hashtable<String,String> obj = new Hashtable<String, String>();
            String [] columns = cursor.getColumnNames();
            for(String col : columns){
                obj.put(col.toLowerCase(),cursor.getString(cursor.getColumnIndexOrThrow(col)));

            }
            objects.add(obj);
        }
        return objects;
    }

    @Override
    public Hashtable<String, String> load(String id) {
        return null;
    }
}
