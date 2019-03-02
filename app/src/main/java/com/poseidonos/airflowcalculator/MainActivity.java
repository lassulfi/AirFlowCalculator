package com.poseidonos.airflowcalculator;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.poseidonos.airflowcalculator.controller.FarmController;
import com.poseidonos.airflowcalculator.fragments.MainInfoFragment;

import static com.poseidonos.airflowcalculator.data.FarmContract.FarmEntry.COLUMN_ACTIVE_COMPRESSORS;
import static com.poseidonos.airflowcalculator.data.FarmContract.FarmEntry.COLUMN_ACTIVE_PENS;
import static com.poseidonos.airflowcalculator.data.FarmContract.FarmEntry.COLUMN_ACTIVE_WALKWAY_CHANNELS;
import static com.poseidonos.airflowcalculator.data.FarmContract.FarmEntry.COLUMN_AVAILABLE_COMPRESSORS;
import static com.poseidonos.airflowcalculator.data.FarmContract.FarmEntry.COLUMN_CHANNEL_PER_PEN;
import static com.poseidonos.airflowcalculator.data.FarmContract.FarmEntry.COLUMN_COMPRESSOR_OUTPUT;
import static com.poseidonos.airflowcalculator.data.FarmContract.FarmEntry.COLUMN_FARM_NAME;
import static com.poseidonos.airflowcalculator.data.FarmContract.FarmEntry.COLUMN_NUMBER_PENS;
import static com.poseidonos.airflowcalculator.data.FarmContract.FarmEntry.COLUMN_NUMBER_WALKWAY_CHANNELS;
import static com.poseidonos.airflowcalculator.data.FarmContract.FarmEntry.COLUMN_PANEL_GENERATION;
import static com.poseidonos.airflowcalculator.data.FarmContract.FarmEntry.COLUMN_READ_PRESSURE;
import static com.poseidonos.airflowcalculator.data.FarmContract.FarmEntry._ID;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri mClickedFarmUri;

    private FarmController mFarmController;

    private Toolbar toolbar;

    private static final String FARM_PARCELABLE = "farm_parceable";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.main_activity_toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFarmController = new FarmController(getApplicationContext());

        Intent intent = getIntent();
        if(intent != null){
            mClickedFarmUri = intent.getData();
            if(mClickedFarmUri == null){
                mFarmController.createNewFarm();
            } else {
                mFarmController.setUri(mClickedFarmUri);
                getSupportLoaderManager().initLoader(0, null, this);
            }
        }

        Bundle bundle = new Bundle();
        bundle.putParcelable(FARM_PARCELABLE, mFarmController);

        MainInfoFragment fragment = new MainInfoFragment();
        fragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_content,fragment);
        //ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 0){
            Log.i("MAIN_ACTIVITY", "popping backstack");
            getSupportFragmentManager().popBackStack();
        } else {
            Log.i("MAIN_ACTIVITY", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = new String[] {_ID,
                COLUMN_FARM_NAME,
                COLUMN_PANEL_GENERATION,
                COLUMN_AVAILABLE_COMPRESSORS,
                COLUMN_COMPRESSOR_OUTPUT,
                COLUMN_ACTIVE_COMPRESSORS,
                COLUMN_NUMBER_PENS,
                COLUMN_CHANNEL_PER_PEN,
                COLUMN_ACTIVE_PENS,
                COLUMN_NUMBER_WALKWAY_CHANNELS,
                COLUMN_ACTIVE_WALKWAY_CHANNELS,
                COLUMN_READ_PRESSURE};

        Loader<Cursor> loader;
        switch (id){
            case 0:
                loader = new CursorLoader(this, mFarmController.getUri(), projection,
                        null, null, null);
                return  loader;
            default: return null;
        }
    }

    //FIXME
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor.moveToFirst()){
            mFarmController.createNewFarm();
            mFarmController.setNameSite(cursor.getString(cursor.getColumnIndex(COLUMN_FARM_NAME)));
            mFarmController.setPanelGen(cursor.getInt(cursor.getColumnIndex(COLUMN_PANEL_GENERATION)));
            mFarmController.setAvailNumComps(cursor.getInt(cursor.getColumnIndex(COLUMN_AVAILABLE_COMPRESSORS)));
            mFarmController.setCompFlow(cursor.getInt(cursor.getColumnIndex(COLUMN_COMPRESSOR_OUTPUT)));
            mFarmController.setNumComps(cursor.getInt(cursor.getColumnIndex(COLUMN_AVAILABLE_COMPRESSORS)));
            mFarmController.setNumPens(cursor.getInt(cursor.getColumnIndex(COLUMN_NUMBER_PENS)));
            mFarmController.setChanPerPen(cursor.getInt(cursor.getColumnIndex(COLUMN_CHANNEL_PER_PEN)));
            mFarmController.setActivePens(cursor.getInt(cursor.getColumnIndex(COLUMN_ACTIVE_PENS)));
            mFarmController.setChnlWalkway(cursor.getInt(cursor.getColumnIndex(COLUMN_NUMBER_WALKWAY_CHANNELS)));
            mFarmController.setActiveChnlWalk(cursor.getInt(cursor.getColumnIndex(COLUMN_ACTIVE_WALKWAY_CHANNELS)));
            mFarmController.setReadPressure(cursor.getInt(cursor.getColumnIndex(COLUMN_READ_PRESSURE)));
            mFarmController.setLoaded(true);
        }
        cursor.close();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFarmController.createNewFarm();
        mFarmController.setLoaded(false);

    }
}
