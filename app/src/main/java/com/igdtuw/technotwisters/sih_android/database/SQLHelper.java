package com.igdtuw.technotwisters.sih_android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Megha on 13-03-2016.
 */
public class SQLHelper extends SQLiteOpenHelper {

    final public static String DATABASE_NAME = "ToDoDb";
    final public static String TABLE_NAME = "ToDoTable";
    final public static String _ID = "_ID";
    final public static String TITLE = "Title";
    final public static String DATE = "Date";
    final public static String CONTENT = "Content";
    final public static String COLOR = "Color";

    public SQLHelper(Context context, int version){
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE "+TABLE_NAME+" ( "+_ID+" INTEGER , "+TITLE+" TEXT, "+ DATE+" TEXT, "+
                CONTENT+" TEXT, "+COLOR+" INTEGER "+" );";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
