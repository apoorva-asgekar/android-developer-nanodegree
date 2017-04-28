package com.example.android.waitlist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.waitlist.data.WaitlistContract.WaitlistEntry;

// TODOCOMPLETED (1) extend the SQLiteOpenHelper class
public class WaitlistDbHelper extends SQLiteOpenHelper {

    // TODOCOMPLETED (2) Create a static final String called DATABASE_NAME and set it to "waitlist.db"
    private static final String DATABASE_NAME = "waitlist.db";

    // TODOCOMPLETED (3) Create a static final int called DATABASE_VERSION and set it to 1
    private static final int DATABASE_VERSION = 1;

    // TODOCOMPLETED (4) Create a Constructor that takes a context and calls the parent constructor
    public WaitlistDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // TODOCOMPLETED (5) Override the onCreate method
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_WAITLIST_QUERY = "CREATE TABLE " + WaitlistEntry.TABLE_NAME + "(" +
                WaitlistEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WaitlistEntry.COLUMN_GUEST_NAME + " TEXT NOT NULL, " +
                WaitlistEntry.COLUMN_PARTY_SIZE + " INTEGER NOT NULL, " +
                WaitlistEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";
        db.execSQL(SQL_CREATE_WAITLIST_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WaitlistEntry.TABLE_NAME + ";");
        onCreate(db);
    }


    // TODOCOMPLETED (6) Inside, create an String query called SQL_CREATE_WAITLIST_TABLE that will create the table

        // TODOCOMPLETED (7) Execute the query by calling execSQL on sqLiteDatabase and pass the string query SQL_CREATE_WAITLIST_TABLE

    // TODOCOMPLETED (8) Override the onUpgrade method

        // TODOCOMPLETED (9) Inside, execute a drop table query, and then call onCreate to re-create it

}