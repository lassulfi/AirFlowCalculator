package com.poseidonos.airflowcalculator.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.poseidonos.airflowcalculator.ResultsActivity;
import com.poseidonos.airflowcalculator.controller.FarmController;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdtionalInfoFragment extends Fragment {

    private static final String FARM_PARCELABLE = "farm_parceable";

    //Screen elements
    private EditText numPensEditText;
    private EditText chanPerPenEditText;
    private Spinner activePensSpinner;
    private EditText chnlWalkwaysEditText;
    private Spinner activeChnlWalkSpinner;
    private EditText readPressureEditText;
    private Button calculateButton;

    private FarmController mFarmController;

    public AdtionalInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_adtional_info, container, false);

        //Retrieve elements from screen
        numPensEditText = rootView.findViewById(R.id.number_of_pens_edittext);
        chanPerPenEditText = rootView.findViewById(R.id.channels_per_pen_edittext);
        activePensSpinner = rootView.findViewById(R.id.active_pens_spinner);
        chnlWalkwaysEditText = rootView.findViewById(R.id.chnl_walkaway_edittext);
        activeChnlWalkSpinner = rootView.findViewById(R.id.active_walkways_channels_spinner);
        readPressureEditText = rootView.findViewById(R.id.panel_set_pressure_edittext);
        calculateButton = rootView.findViewById(R.id.btn_calculate);

        //Retrieve the controller
        Bundle bundle = getArguments();
        mFarmController = (FarmController) bundle.getParcelable(FARM_PARCELABLE);

        if(mFarmController.isLoaded()){
            updateUI();
        }

        numPensEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                List<String> spinnerValues = new ArrayList<>();
                if(editable.length() == 0){
                    spinnerValues.add("1");
                } else {
                    int num = Integer.valueOf(editable.toString());
                    for(int i = 0; i < num; i++){
                        spinnerValues.add(Integer.toString(i + 1));
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.support_simple_spinner_dropdown_item, spinnerValues);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

                activePensSpinner.setAdapter(adapter);
                activePensSpinner.setSelection(spinnerValues.size() - 1);
            }
        });

        chnlWalkwaysEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                List<String> spinnerValues = new ArrayList<>();
                if(editable.length() == 0){
                    spinnerValues.add("1");
                } else {
                    int num = Integer.valueOf(editable.toString());
                    for(int i = 0; i < num; i++){
                        spinnerValues.add(Integer.toString(i + 1));
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.support_simple_spinner_dropdown_item, spinnerValues);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

                activeChnlWalkSpinner.setAdapter(adapter);
                activeChnlWalkSpinner.setSelection(spinnerValues.size() - 1);
            }
        });

        //Add a click event to the button
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Fields validation
                if (fieldsValidation()) return;

                int numPens = Integer.valueOf(numPensEditText.getText().toString());
                int chanPerPen = Integer.valueOf(chanPerPenEditText.getText().toString());
                int activePen = Integer.valueOf(activePensSpinner.getSelectedItem().toString());
                int chnlWalkway = Integer.valueOf(chnlWalkwaysEditText.getText().toString());
                int activeChnlWalk = Integer.valueOf(activeChnlWalkSpinner.getSelectedItem().toString());
                double readPressure = Double.valueOf(readPressureEditText.getText().toString());

                mFarmController.setNumPens(numPens);
                mFarmController.setChanPerPen(chanPerPen);
                mFarmController.setActivePens(activePen);
                mFarmController.setChnlWalkway(chnlWalkway);
                mFarmController.setActiveChnlWalk(activeChnlWalk);
                mFarmController.setReadPressure(readPressure);

                mFarmController.calculate();

                Intent intent = new Intent(getActivity(), ResultsActivity.class);
                intent.putExtra(FARM_PARCELABLE, mFarmController);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return rootView;
    }

    private void updateUI() {
        numPensEditText.setText(String.valueOf(mFarmController.getNumPens()));
        chanPerPenEditText.setText(String.valueOf(mFarmController.getChanPerPen()));
        List<String> activePenSinnerValues = new ArrayList<>();
        for(int i = 0; i < mFarmController.getActivePens(); i++){
            activePenSinnerValues.add(Integer.toString(i + 1));
        }
        ArrayAdapter<String> activePenSpinnerAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.support_simple_spinner_dropdown_item, activePenSinnerValues);
        activePenSpinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        activePensSpinner.setAdapter(activePenSpinnerAdapter);
        activePensSpinner.setSelection(mFarmController.getActivePens() - 1);
        chnlWalkwaysEditText.setText(String.valueOf(mFarmController.getChnlWalkway()));
        List<String> activeChnlWalkValues = new ArrayList<>();
        for(int i = 0; i < mFarmController.getChnlWalkway(); i++){
            activeChnlWalkValues.add(Integer.toString(i + 1));
        }
        ArrayAdapter<String> activeChnlWalkAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.support_simple_spinner_dropdown_item, activeChnlWalkValues);
        activeChnlWalkAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        activeChnlWalkSpinner.setAdapter(activeChnlWalkAdapter);
        activeChnlWalkSpinner.setSelection(mFarmController.getChnlWalkway() - 1);
        readPressureEditText.setText(String.valueOf(mFarmController.getReadPressure()));
    }


    /**
     * Validates EditText input fields.
     * @return true if a field is not valid and false if all fields are valid.
     */
    private boolean fieldsValidation() {
        if(numPensEditText.getText().length() == 0){
            Toast.makeText(getActivity(),
                    "Number of pens cannot be empty", Toast.LENGTH_SHORT).show();
            numPensEditText.requestFocus();
            return true;
        }

        if(numPensEditText.getText().toString().equals("0")){
            Toast.makeText(getActivity(), "Number of pens must be greater than zero",
                    Toast.LENGTH_SHORT).show();
            numPensEditText.requestFocus();
            return true;
        }

        if(chnlWalkwaysEditText.getText().length() == 0){
            Toast.makeText(getActivity(), "Channels per Pen cannot be empty",
                    Toast.LENGTH_SHORT).show();
            chnlWalkwaysEditText.requestFocus();
            return true;
        }

        if(chnlWalkwaysEditText.getText().toString().equals("0")){
            Toast.makeText(getActivity(), "Channels per Pen must be greater than zero",
                    Toast.LENGTH_SHORT).show();
            chnlWalkwaysEditText.requestFocus();
            return true;
        }

        if(readPressureEditText.getText().length() == 0){
            Toast.makeText(getActivity(), "Panel Set Pressure (psi) cannot be empty",
                    Toast.LENGTH_SHORT).show();
            readPressureEditText.requestFocus();
            return true;
        }

        if(readPressureEditText.getText().toString().equals("0")){
            Toast.makeText(getActivity(), "Panel Set Pressure (psi) must be greater than zero",
                    Toast.LENGTH_SHORT).show();
            readPressureEditText.requestFocus();
            return true;
        }
        return false;
    }
}
