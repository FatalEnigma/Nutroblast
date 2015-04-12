package com.code4betterhackathon.nutroblast.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.code4betterhackathon.nutroblast.MainActivity;
import com.code4betterhackathon.nutroblast.R;

public class EditDetailsFragment extends Fragment implements View.OnClickListener {


    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private Button acceptButton;
    private EditText nameEditText, ageEditText, heightEditText, weightEditText;
    private RadioGroup genderRadioGroup;
    private RadioButton femaleButton;
    private Spinner spinner;
    private int[] checkBoxIDs = {R.id.ironCheckBox, R.id.calorieCheckBox, R.id.glucoseCheckBox, R.id.cholesterolCheckBox};
    private View rootView;

    public enum weightOptionEnum {
        NONE, WEIGHT_LOSS, WEIGHT_GAIN;
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static EditDetailsFragment newInstance(int sectionNumber) {
        EditDetailsFragment fragment = new EditDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public EditDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_details, container, false);

        spinner = (Spinner) rootView.findViewById(R.id.calorieSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.weight_setting_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        acceptButton = (Button) rootView.findViewById(R.id.updateDetailsButton);
        acceptButton.setOnClickListener(this);
        nameEditText = (EditText) rootView.findViewById(R.id.nameEditText);
        ageEditText = (EditText) rootView.findViewById(R.id.agedEditText);
        heightEditText = (EditText) rootView.findViewById(R.id.heightEditText);
        weightEditText = (EditText) rootView.findViewById(R.id.weightEditText);
        genderRadioGroup = (RadioGroup) rootView.findViewById(R.id.genderRadioGroup);
        femaleButton = (RadioButton) rootView.findViewById(R.id.femaleRadioButton);
        this.rootView = rootView;
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.updateDetailsButton:
                if (performValidation()) {
                    Toast.makeText(getActivity(), "All Valid", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean performValidation() {
        String name, age, height, weight;
        boolean isMale = false, validationError = false;
        boolean[] goals = new boolean[4];
        weightOptionEnum weightOptionEnum = EditDetailsFragment.weightOptionEnum.NONE;

        if (nameEditText.getText().toString().isEmpty()) {
            nameEditText.setError("You need to enter a name");
            validationError = true;
        } else {
            name = nameEditText.getText().toString();
        }

        if (ageEditText.getText().toString().isEmpty()) {
            ageEditText.setError("You need to enter an age");
            validationError = true;
        } else if (Integer.parseInt(ageEditText.getText().toString()) < 1 || Integer.parseInt(ageEditText.getText().toString()) > 127) {
            ageEditText.setError("You age does not appear valid");
            validationError = true;
        } else {
            age = ageEditText.getText().toString();
        }

        if (heightEditText.getText().toString().isEmpty()) {
            heightEditText.setError("You need to enter a height");
            validationError = true;
        } else if (Integer.parseInt(heightEditText.getText().toString()) < 30 || Integer.parseInt(heightEditText.getText().toString()) > 250) {
            heightEditText.setError("Your height does not appear valid");
            validationError = true;
        } else {
            height = heightEditText.getText().toString();
        }

        if (weightEditText.getText().toString().isEmpty()) {
            weightEditText.setError("You need to enter a weight");
            validationError = true;
        } else if (Integer.parseInt(weightEditText.getText().toString()) < 30 || Integer.parseInt(weightEditText.getText().toString()) > 500) {
            weightEditText.setError("Your weight does not appear valid");
            validationError = true;
        } else {
            weight = weightEditText.getText().toString();
        }

        if (genderRadioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getActivity(), "You must select a gender!", Toast.LENGTH_SHORT).show();
            validationError = true;
        } else {
            isMale = genderRadioGroup.getCheckedRadioButtonId() == R.id.femaleRadioButton ? false : true;
        }

        boolean oneCheckBoxSelected = false;

        for (int id = 0; id < checkBoxIDs.length; id++) {
            CheckBox checkBox = (CheckBox) rootView.findViewById(checkBoxIDs[id]);
            if (checkBox.isChecked()) {
                oneCheckBoxSelected = true;

                if (id == 1) {
                    weightOptionEnum = spinner.getSelectedItemPosition() == 0 ? EditDetailsFragment.weightOptionEnum.WEIGHT_GAIN : EditDetailsFragment.weightOptionEnum.WEIGHT_LOSS;
                } else {
                    goals[id] = true;
                }
            }
        }

        if (!oneCheckBoxSelected) {
            Toast.makeText(getActivity(), "You must select at least one focus", Toast.LENGTH_SHORT).show();
            validationError = true;
        }

        return !validationError;
    }
}