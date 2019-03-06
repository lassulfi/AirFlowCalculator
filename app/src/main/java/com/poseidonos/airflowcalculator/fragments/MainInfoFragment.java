package com.poseidonos.airflowcalculator.fragments;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.poseidonos.airflowcalculator.R;
import com.poseidonos.airflowcalculator.controller.FarmController;
import com.poseidonos.airflowcalculator.data.FarmContract;

import java.util.ArrayList;
import java.util.List;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class MainInfoFragment extends Fragment {

    //Screen Elements
    private EditText nameSiteEditText;
    private Spinner panelGenSpinner;
    private EditText availNumCompsEditText;
    private EditText compFlowEditText;
    private Spinner numCompsSpinner;
    private Button nextButton;

    private FarmController mFarmController;
    private Uri mUriClickedItem;

    private static final String SCREEN_STATE = "screen_state";

    private static final String FARM_PARCELABLE = "farm_parceable";

    private static final String URI_PARCELABLE = "uri_parceable";

    public MainInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_info, container, false);

        //Retrieve elements from screen
        nameSiteEditText = rootView.findViewById(R.id.site_name_edittext);
        panelGenSpinner = rootView.findViewById(R.id.panel_generation_spinner);
        availNumCompsEditText = rootView.findViewById(R.id.available_compressors_edittext);
        compFlowEditText = rootView.findViewById(R.id.compressor_output_edittext);
        numCompsSpinner = rootView.findViewById(R.id.active_compressors_spinner);
        nextButton = rootView.findViewById(R.id.btnNext);

        Bundle bundle = getArguments();
        mFarmController = (FarmController) bundle.get(FARM_PARCELABLE);
        mUriClickedItem = (Uri) bundle.get(URI_PARCELABLE);

        if(mUriClickedItem != null){
            String[] projection = {_ID,
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
            Cursor cursor = getActivity().getContentResolver().query(mUriClickedItem, projection,
                    null, null, null);
            if(cursor != null && cursor.moveToFirst()){
                mFarmController.setNameSite(cursor.getString(cursor.getColumnIndex(COLUMN_FARM_NAME)));
                mFarmController.setPanelGen(cursor.getInt(cursor.getColumnIndex(COLUMN_PANEL_GENERATION)));
                mFarmController.setAvailNumComps(cursor.getInt(cursor.getColumnIndex(COLUMN_AVAILABLE_COMPRESSORS)));
                mFarmController.setCompFlow(cursor.getInt(cursor.getColumnIndex(COLUMN_COMPRESSOR_OUTPUT)));
                mFarmController.setNumComps(cursor.getInt(cursor.getColumnIndex(COLUMN_ACTIVE_COMPRESSORS)));
                mFarmController.setNumPens(cursor.getInt(cursor.getColumnIndex(COLUMN_NUMBER_PENS)));
                mFarmController.setChanPerPen(cursor.getInt(cursor.getColumnIndex(COLUMN_CHANNEL_PER_PEN)));
                mFarmController.setActivePens(cursor.getInt(cursor.getColumnIndex(COLUMN_ACTIVE_PENS)));
                mFarmController.setChnlWalkway(cursor.getInt(cursor.getColumnIndex(COLUMN_NUMBER_WALKWAY_CHANNELS)));
                mFarmController.setActiveChnlWalk(cursor.getInt(cursor.getColumnIndex(COLUMN_ACTIVE_WALKWAY_CHANNELS)));
                mFarmController.setReadPressure(cursor.getDouble(cursor.getColumnIndex(COLUMN_READ_PRESSURE)));
                mFarmController.setLoaded(true);
            }
        }

        if(mFarmController.isLoaded()){
            updateUI();
        }


        availNumCompsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                List<String> numCompsSpinnerValues = new ArrayList<>();
                if(editable.length() == 0){
                    numCompsSpinnerValues.add("1");
                } else {
                    int num = Integer.valueOf(editable.toString());
                    for (int i = 0; i < num; i++) {
                        numCompsSpinnerValues.add(Integer.toString(i + 1));
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.support_simple_spinner_dropdown_item, numCompsSpinnerValues);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

                numCompsSpinner.setAdapter(adapter);
                numCompsSpinner.setSelection(numCompsSpinnerValues.size() - 1);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Validation
                if (validateData()) return;

                String nameSite = nameSiteEditText.getText().toString();
                int panelGen = Integer.valueOf(panelGenSpinner.getSelectedItem().toString());
                int availNumComps = Integer.valueOf(availNumCompsEditText.getText().toString());
                int compFlow = Integer.valueOf(compFlowEditText.getText().toString());
                int numComps = Integer.valueOf(numCompsSpinner.getSelectedItem().toString());

                mFarmController.setNameSite(nameSite);
                mFarmController.setPanelGen(panelGen);
                mFarmController.setAvailNumComps(availNumComps);
                mFarmController.setCompFlow(compFlow);
                mFarmController.setNumComps(numComps);

                //Creates the fragments for user inputs
                createInformationFragments();
            }
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(SCREEN_STATE, mFarmController);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null) {
            mFarmController = savedInstanceState.getParcelable(SCREEN_STATE);
            updateUI();
        }
    }

    /**
     * Validate all user inputs for this fragment
     * @return true is there is invalid data or false if the data is OK
     */
    private boolean validateData() {
        if(nameSiteEditText.getText().length() == 0){
            Toast.makeText(getActivity(),
                    "Site name cannot be empty", Toast.LENGTH_SHORT).show();
            nameSiteEditText.requestFocus();
            return true;
        }

        if(availNumCompsEditText.getText().length() == 0){
            Toast.makeText(getActivity(), "Number of available compressors cannot be empty",
                    Toast.LENGTH_SHORT).show();
            availNumCompsEditText.requestFocus();
            return true;
        }
        if(availNumCompsEditText.getText().toString().equals("0")){
            Toast.makeText(getActivity(),
                    "Number of available compressor must be greater than zero", Toast.LENGTH_SHORT).show();
            availNumCompsEditText.requestFocus();
            return true;
        }

        if(compFlowEditText.getText().length() == 0){
            Toast.makeText(getActivity(), "Compressor Output CFM (FAD) cannot be empty",
                    Toast.LENGTH_SHORT).show();
            compFlowEditText.requestFocus();
            return true;
        }
        if(compFlowEditText.getText().toString().equals("0")){
            Toast.makeText(getActivity(),
                    "Compressor Output CFM (FAD) must be greater than zero", Toast.LENGTH_SHORT).show();
            compFlowEditText.requestFocus();
            return true;
        }
        return false;
    }

    /**
     * Creates the fragments for the input of information
     */
    private void createInformationFragments() {
        //Create a new fragment and transaction
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putParcelable(FARM_PARCELABLE, mFarmController);

        AdtionalInfoFragment fragment = new AdtionalInfoFragment();
        fragment.setArguments(bundle);

        //Replace whatever is in the fragment_container view with the new fragment
        //and add the transaction back to stack
        ft.replace(R.id.fragment_content, fragment);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        //Commit the transacton
        ft.commit();
    }

    private void updateUI() {
        nameSiteEditText.setText(mFarmController.getNameSite());
        switch (mFarmController.getPanelGen()){
            case 1:
                panelGenSpinner.setSelection(0);
                break;
            case 2:
                panelGenSpinner.setSelection(1);
                break;
            case 4:
                panelGenSpinner.setSelection(2);
                break;
        }
        availNumCompsEditText.setText(String.valueOf(mFarmController.getAvailNumComps()));
        compFlowEditText.setText(String.valueOf(mFarmController.getCompFlow()));
        List<String> spinner = new ArrayList<>();
        for(int i = 1; i <= mFarmController.getAvailNumComps(); i++){
            spinner.add(String.valueOf(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.support_simple_spinner_dropdown_item, spinner);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        numCompsSpinner.setAdapter(adapter);
        numCompsSpinner.setSelection(mFarmController.getNumComps() - 1);
    }

    public void resetUI(){
        nameSiteEditText.setText(null);
        availNumCompsEditText.setText(null);
        compFlowEditText.setText(null);
        numCompsSpinner.setSelection(0);
    }
}
