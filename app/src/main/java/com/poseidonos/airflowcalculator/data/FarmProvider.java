package com.poseidonos.airflowcalculator.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.poseidonos.airflowcalculator.data.FarmContract.FarmEntry;

import static com.poseidonos.airflowcalculator.data.FarmContract.FarmEntry.*;

public class FarmProvider extends ContentProvider {

    private static final String LOG_TAG = FarmProvider.class.getSimpleName();

    private FarmDbHelper mFarmDbHelper;
    private static final int FARMS = 100;
    private static final int FARM_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static{
        sUriMatcher.addURI("com.poseidonos.airflowcalculator",
                FarmContract.PATH_FARMS, FARMS);
        sUriMatcher.addURI("com.poseidonos.airflowcalculator",
                FarmContract.PATH_FARMS + "/#", FARM_ID);
    }

    @Override
    public boolean onCreate() {
        mFarmDbHelper = new FarmDbHelper(getContext());

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mFarmDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);

        switch (match){
            case FARMS:
                cursor = database.query(TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case FARM_ID:
                selection = _ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalStateException("Cannot query URI " + uri);

        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case FARMS:
                return CONTENT_LIST_TYPE;
            case FARM_ID:
                return CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + "with match " + match);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case FARMS:
                return insertFarm(uri, values);
            default:
                throw new IllegalStateException("The uri" + uri + "does not support data insertion");
        }
    }

    private Uri insertFarm(Uri uri, ContentValues values) {
        SQLiteDatabase database = mFarmDbHelper.getWritableDatabase();

        long id = database.insert(TABLE_NAME, null, values);
        Log.v(LOG_TAG, "New line id " + id);
        if(id == -1){
            Log.e(LOG_TAG, "Error inserting line at " + uri);
            return  null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mFarmDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);

        switch (match){
            case FARMS:
                return database.delete(TABLE_NAME, selection, selectionArgs);
            case FARM_ID:
                selection = _ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return database.delete(TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalStateException("A uri "+ uri + "do not support deleting data");
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);

        switch (match){
            case FARMS:
                return updateFarms(uri, values, selection, selectionArgs);
            case FARM_ID:
                selection = _ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateFarms(uri, values, selection, selectionArgs);
             default:
                 throw new IllegalStateException("A uri "+ uri + "do not support updating data");
        }
    }

    private int updateFarms(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //If there are no values, do not update db
        if (values.size() == 0){
            return 0;
        }

        SQLiteDatabase database = mFarmDbHelper.getWritableDatabase();
        int lines = database.update(TABLE_NAME, values, selection, selectionArgs);
        if(lines != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return lines;
    }
}
