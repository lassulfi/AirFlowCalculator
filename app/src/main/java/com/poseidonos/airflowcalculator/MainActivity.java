package com.poseidonos.airflowcalculator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.poseidonos.airflowcalculator.controller.FarmController;
import com.poseidonos.airflowcalculator.fragments.MainInfoFragment;

public class MainActivity extends AppCompatActivity {

    private Uri mClickedFarmUri;

    private FarmController mFarmController;

    private Toolbar toolbar;

    private static final String FARM_PARCELABLE = "farm_parceable";
    private static final String URI_PARCELABLE = "uri_parceable";

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
        mFarmController.createNewFarm();

        Intent intent = getIntent();
        mClickedFarmUri = intent.getData();

        Bundle bundle = new Bundle();
        bundle.putParcelable(FARM_PARCELABLE, mFarmController);
        bundle.putParcelable(URI_PARCELABLE, mClickedFarmUri);

        MainInfoFragment fragment = new MainInfoFragment();
        fragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_content,fragment);
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

}
