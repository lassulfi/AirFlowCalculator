package com.poseidonos.airflowcalculator.fragments;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

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

                //Creates the fragments for user inputs
                createInformationFragments();
            }
        });

        return rootView;
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

        //Replace whatever is in the fragment_container view with the new fragment
        //and add the transaction back to stack
        ft.replace(R.id.fragment_content, new AdtionalInfoFragment());
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        //Commit the transacton
        ft.commit();
    }
}
