package com.poseidonos.airflowcalculator;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.poseidonos.airflowcalculator.controller.FarmController;
import com.poseidonos.airflowcalculator.data.FarmContract;

import static com.poseidonos.airflowcalculator.data.FarmContract.FarmEntry.CONTENT_URI;
import static com.poseidonos.airflowcalculator.data.FarmContract.FarmEntry._ID;

public class ResultsActivity extends AppCompatActivity {

    private static final String FARM_PARCELABLE = "farm_parceable";

    private FarmController mFarmController;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        toolbar = findViewById(R.id.result_toolbar);
        toolbar.setTitle(R.string.toolbar_results_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFarmController = (FarmController) getIntent().getParcelableExtra(FARM_PARCELABLE);
        mFarmController.setContext(this);

        TextView targetFlowDisplayTextView = findViewById(R.id.target_flow_display_textview);
        TextView comparativeStandardMeterTextView = findViewById(R.id.comparative_standard_meter_textview);

        String targetFlowDisplay = getResources().getString(R.string.target_flow_display_textview).concat(" ")
                .concat(String.format("%.1f", mFarmController.getTargetFlowDisplay()));
        if(mFarmController.getPanelGen() == 4){
            targetFlowDisplay += " %";
        }

        String compartiveStandardMeter = getResources().getString(R.string.comparative_standard_meter_textview).concat(" ")
                .concat(String.format("%.1f", mFarmController.getStdReading()));

        targetFlowDisplayTextView.setText(targetFlowDisplay);
        comparativeStandardMeterTextView.setText(compartiveStandardMeter);

        Button btnSaveCalc = findViewById(R.id.btnSaveCal);
        btnSaveCalc.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                final ContentValues values = mFarmController.getFarmContentValues();
                //Query if the farm aready exists
                String[] projection = {_ID, FarmContract.FarmEntry.COLUMN_FARM_NAME};
                String selection = FarmContract.FarmEntry.COLUMN_FARM_NAME + " LIKE ?";
                String[] selectionArgs = {mFarmController.getNameSite()};
                final Cursor queryCursor = getContentResolver().query(CONTENT_URI,
                        projection, selection, selectionArgs, null, null);
                Uri uri = null;
                if(queryCursor.moveToFirst()){
                    AlertDialog.Builder builder = new AlertDialog
                            .Builder(ResultsActivity.this);
                    builder.setMessage(R.string.dialog_builder_update_message);
                    builder.setPositiveButton(R.string.dialog_builder_update_positive_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            //If there is cursor, updates the existing project
                            Uri updateUri = ContentUris.withAppendedId(CONTENT_URI,
                                    queryCursor.getInt(queryCursor.getColumnIndex(_ID)));
                            int lines = getContentResolver().update(updateUri, values, null, null);
                            if(lines != 0){
                                Toast.makeText(getApplication(), "Farm updated", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplication(), "Error saving data", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });
                    builder.setNegativeButton(R.string.dialog_builder_update_negative_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            if(dialog != null){
                                dialog.dismiss();
                            }
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    uri = getContentResolver().insert(CONTENT_URI, values);
                    if(uri == null){
                        Toast.makeText(getApplicationContext(),
                                "Error saving calculation", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Calculation saved", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Button btnNewCalc = findViewById(R.id.btnNewCalc);
        btnNewCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
