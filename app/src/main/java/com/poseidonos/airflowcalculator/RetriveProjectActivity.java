package com.poseidonos.airflowcalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class RetriveProjectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrive_project);

        ListView listView = findViewById(R.id.farms_listview);

        View emptyView = findViewById(R.id.empty_view_layout);
        listView.setEmptyView(emptyView);

        //Enable button click if the listview is empty
        //TODO: implement the adapter for this class
        //DO NOT USE THIS ACTIVITY YET, OTHERWSISE THE APP WILL CRASH
        if(listView.getAdapter().getCount() == 0){
            Button btnStartNewConfiguration = findViewById(R.id.btn_start_new_calc);
            btnStartNewConfiguration.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(RetriveProjectActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

        }
    }
}
