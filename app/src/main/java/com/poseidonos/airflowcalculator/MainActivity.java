package com.poseidonos.airflowcalculator;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.poseidonos.airflowcalculator.fragments.MainInfoFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainInfoFragment fragment = new MainInfoFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_content,fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    //TODO: add a toolbar to the mainActivity and add a back button

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() > 0){
            Log.i("MAIN_ACTIVITY", "popping backstack");
            getFragmentManager().popBackStack();
        } else {
            Log.i("MAIN_ACTIVITY", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }
}
