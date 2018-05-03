package com.example.brandonmain.listviewexample1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_INGREDIENTS = "ingredients";
    public static final String TABLE_NEEDS = "needs";
    public static final String TABLE_RECIPES = "recipes";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_EXPIRE = "expiration_time";
    public static final String COLUMN_METHOD = "method";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_HAVE = "have";
    public static final String COLUMN_RECIPE = "recipe";
    public static final String COLUMN_INGREDIENT = "ingredient";


    private static final String DATABASE_NAME = "fridkj.db";
    private static final int DATABASE_VERSION = 5;

    // Database creation sql statement
    private static final String DATABASE_CREATE_I = "create table "
            + TABLE_INGREDIENTS + "( " + COLUMN_NAME
            + " text primary key, "+ COLUMN_PRICE
            + " real default 0, " + COLUMN_QUANTITY
            + " integer default 0, " + COLUMN_EXPIRE
            + " string default '', " + COLUMN_HAVE
            + " integer default 0)";
    private  static final String DATABASE_CREATE_R = "create table "
            + TABLE_RECIPES + "( " + COLUMN_NAME + " text primary key, "
            + COLUMN_METHOD + " text DEFAULT '', "
            + COLUMN_URL + " text default '')";
    private static final String DATABASE_CREATE_N = "create table "
            + TABLE_NEEDS +"( "+ COLUMN_RECIPE + "  text,"
            + COLUMN_INGREDIENT + " text, primary key(" + COLUMN_RECIPE + "," + COLUMN_INGREDIENT + "))";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_I);
        database.execSQL(DATABASE_CREATE_R);
        database.execSQL(DATABASE_CREATE_N);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEEDS);
        onCreate(db);
    }
}