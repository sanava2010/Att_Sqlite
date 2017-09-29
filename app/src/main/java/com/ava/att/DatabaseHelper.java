package com.ava.att;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "attendance.db";
    public static final String TABLE_NAME="students";
    public static final String COL1 = "RollNo";
    public static final String COL2 = "Name";

    /*public void getTbName(String TbName)
    {
        TABLE_NAME=TbName;
        return;
    }*/
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    public void dlRows()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String del="delete from "+TABLE_NAME;
        db.execSQL(del);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (RollNo REAL PRIMARY KEY AUTOINCREMENT, " +
                " Name TEXT)";
        db.execSQL(createTable);
        Log.d(TAG, "Table created!!!!!!!!!!!!!&&&&&&&&&&&&&&&&&77 " );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(float roll,String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //contentValues.put(COL1,roll);
        contentValues.put(COL2, name);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }
}