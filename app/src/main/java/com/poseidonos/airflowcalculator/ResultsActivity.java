package com.poseidonos.airflowcalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Button btnSaveCalc = findViewById(R.id.btnSaveCal);
        btnSaveCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        "Calculation saved", Toast.LENGTH_SHORT).show();
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
