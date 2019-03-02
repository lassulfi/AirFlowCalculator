package com.poseidonos.airflowcalculator.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.poseidonos.airflowcalculator.data.FarmContract.*;

public class FarmDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "farms.db";

    public FarmDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_FARMS_TABLE = "CREATE TABLE IF NOT EXISTS " + FarmEntry.TABLE_NAME + " (" +
                FarmEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FarmEntry.COLUMN_FARM_NAME + " TEXT NOT NULL, " +
                FarmEntry.COLUMN_PANEL_GENERATION + " INTEGER NOT NULL, " +
                FarmEntry.COLUMN_AVAILABLE_COMPRESSORS + " INTEGER NOT NULL, " +
                FarmEntry.COLUMN_COMPRESSOR_OUTPUT + " INTEGER NOT NULL, " +
                FarmEntry.COLUMN_ACTIVE_COMPRESSORS + " INTEGER NOT NULL, " +
                FarmEntry.COLUMN_NUMBER_PENS + " INTEGER NOT NULL, " +
                FarmEntry.COLUMN_CHANNEL_PER_PEN + " INTEGER NOT NULL, " +
                FarmEntry.COLUMN_ACTIVE_PENS + " INTEGER NOT NULL, " +
                FarmEntry.COLUMN_NUMBER_WALKWAY_CHANNELS + " INTEGER NOT NULL, " +
                FarmEntry.COLUMN_ACTIVE_WALKWAY_CHANNELS + " INTEGER NOT NULL, " +
                FarmEntry.COLUMN_READ_PRESSURE + " INTEGER NOT NULL, " +
                FarmEntry.COLUMN_TARGET_FLOW_DISPLAY + " DOUBLE, " +
                FarmEntry.COLUMN_COMPARATIVE_STANDARD_METER + " DOUBLE);";

        db.execSQL(SQL_CREATE_FARMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion == 1){
            String SQL_DROP_FARMS_TABLE = "DROP TABLE IF EXISTS " + DATABASE_NAME;
            db.execSQL(SQL_DROP_FARMS_TABLE);
        }
        if(oldVersion > 1){
            onCreate(db);
        }
    }
}
