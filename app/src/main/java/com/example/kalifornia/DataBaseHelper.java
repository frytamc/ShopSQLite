package com.example.kalifornia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String ORDER_TABLE = "ORDER_TABLE";
    public static final String ID = "Id";
    public static final String COUNTRY = "Country";
    public static final String PRICE = "Price";
    public static final String DATE = "Date";
    public static final String CUSTOMER = "Customer";
    public static final String ARMY = "Army";
    public static final String MINERALS = "Minerals";
    public static final String TEAMS = "Teams";

    private Context context;

    public DataBaseHelper(@Nullable Context context) {
        super(context, "shop.db", null, 2);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + ORDER_TABLE + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COUNTRY + " TEXT, " +
                PRICE + " INTEGER, " +
                DATE + " TEXT, " +
                CUSTOMER + " TEXT, " +
                ARMY + " TEXT, " +
                MINERALS + " TEXT, " +
                TEAMS + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ORDER_TABLE);
        onCreate(db);
    }

    public boolean addOrder(String countrySpin, int price, String date, String customer, String armySpin, String minSpin, String teamSpin) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COUNTRY, countrySpin);
        contentValues.put(PRICE, price);
        contentValues.put(DATE, date);
        contentValues.put(CUSTOMER, customer);
        contentValues.put(ARMY, armySpin);
        contentValues.put(MINERALS, minSpin);
        contentValues.put(TEAMS, teamSpin);

        long result = sqLiteDatabase.insert(ORDER_TABLE, null, contentValues);
        sqLiteDatabase.close();

        return result != -1;
    }

}
