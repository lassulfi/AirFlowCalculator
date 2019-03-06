package com.poseidonos.airflowcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    private Button btnRetriveSavedFarm;
    private Button btnStartNewConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //Find the elements on screen
        btnRetriveSavedFarm = findViewById(R.id.btnRetrieveSavedFarmConfiguration);
        btnStartNewConfiguration = findViewById(R.id.btnStartNewConfiguration);

        //Giving actions to buttons
        btnRetriveSavedFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, RetriveProjectActivity.class);
                startActivity(intent);
            }
        });

        btnStartNewConfiguration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
