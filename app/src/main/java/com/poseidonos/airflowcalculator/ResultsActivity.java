package com.poseidonos.airflowcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.poseidonos.airflowcalculator.controller.FarmController;

public class ResultsActivity extends AppCompatActivity {

    private static final String PARCEL_CONTROLLER = "parcel_controller";

    private FarmController mFarmController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        mFarmController = (FarmController) getIntent().getParcelableExtra(PARCEL_CONTROLLER);
        mFarmController.setContext(this);

        TextView targetFlowDisplayTextView = findViewById(R.id.target_flow_display_textview);
        TextView comparativeStandardMeterTextView = findViewById(R.id.comparative_standard_meter_textview);

        String targetFlowDisplay = getResources().getString(R.string.target_flow_display_textview)
                .concat(String.format("%.1f", mFarmController.getTargetFlowDisplay()));

        String compartiveStandardMeter = getResources().getString(R.string.comparative_standard_meter_textview)
                .concat(String.format("%.1f", mFarmController.getStdReading()));

        targetFlowDisplayTextView.setText(targetFlowDisplay);
        comparativeStandardMeterTextView.setText(compartiveStandardMeter);

        Button btnSaveCalc = findViewById(R.id.btnSaveCal);
        btnSaveCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFarmController.save()) {
                    Toast.makeText(getApplicationContext(),
                            "Calculation saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Error saving calculation", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(ResultsActivity.this, StartActivity.class);
                startActivity(intent);
                finish();

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
}
