package com.code4betterhackathon.nutroblast.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
        this.rootView = rootView;
        loadUserDetails();
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
                performValidation();
                break;
        }
    }

    private void performValidation() {
        String name = null;
        int age = -1;
        int height = -1;
        float weight = -1;
        boolean isMale = false;
        boolean[] goals = new boolean[4];
        weightOptionEnum weightOptionEnum = EditDetailsFragment.weightOptionEnum.NONE;

        boolean validationError = false;

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
            age = Integer.parseInt(ageEditText.getText().toString());
        }

        if (heightEditText.getText().toString().isEmpty()) {
            heightEditText.setError("You need to enter a height");
            validationError = true;
        } else if (Integer.parseInt(heightEditText.getText().toString()) < 30 || Integer.parseInt(heightEditText.getText().toString()) > 250) {
            heightEditText.setError("Your height does not appear valid");
            validationError = true;
        } else {
            height = Integer.parseInt(heightEditText.getText().toString());
        }

        if (weightEditText.getText().toString().isEmpty()) {
            weightEditText.setError("You need to enter a weight");
            validationError = true;
        } else if (Float.parseFloat(weightEditText.getText().toString()) < 30 || Float.parseFloat(weightEditText.getText().toString()) > 500) {
            weightEditText.setError("Your weight does not appear valid");
            validationError = true;
        } else {
            weight = Float.parseFloat(weightEditText.getText().toString());
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

        if (!validationError) {
            saveUserDetails(name, age, height, weight, isMale, goals, weightOptionEnum);
        }
    }

    private void saveUserDetails(String name, int age, int height, float weight, Boolean isMale, boolean[] goals, weightOptionEnum weightOptionEnum) {
        SharedPreferences prefs = getActivity().getSharedPreferences(
                "com.code4betterhackathon.nutroblast", Context.MODE_PRIVATE);

        prefs.edit()
                .putString("name", name)
                .putInt("age", age)
                .putInt("height", height)
                .putFloat("weight", weight)
                .putBoolean("isMale", isMale)
                .putBoolean("0", goals[0])
                .putBoolean("2", goals[2])
                .putBoolean("3", goals[3])
                .putString("weightOptionEnum", weightOptionEnum.name())
                .apply();

        Toast.makeText(getActivity(), getString(R.string.details_saved), Toast.LENGTH_SHORT).show();
    }

    private void loadUserDetails() {
        String name = null;
        int age = -1;
        int height = -1;
        float weight = -1;
        boolean isMale = false;
        boolean[] goals = new boolean[4];
        weightOptionEnum weightOptionEnum = EditDetailsFragment.weightOptionEnum.NONE;

        SharedPreferences prefs = getActivity().getSharedPreferences(
                "com.code4betterhackathon.nutroblast", Context.MODE_PRIVATE);

        name = prefs.getString("name", "");
        age = prefs.getInt("age", -1);
        height = prefs.getInt("height", -1);
        weight = prefs.getFloat("weight", -1);
        isMale = prefs.getBoolean("isMale", true);
        goals[0] = prefs.getBoolean("0", false);
        goals[2] = prefs.getBoolean("2", false);
        goals[3] = prefs.getBoolean("3", false);
        weightOptionEnum = EditDetailsFragment.weightOptionEnum.valueOf(prefs.getString("weightOptionEnum", "NONE"));

        // Populate Fields
        if (name != "") {
            nameEditText.setText(name);
        }
        if (age != -1) {
            ageEditText.setText(Integer.toString(age));
        }
        if (height != -1) {
            heightEditText.setText(Integer.toString(height));
        }
        if (weight != -1) {
            weightEditText.setText(Float.toString(weight));
        }
        if (isMale) {
            genderRadioGroup.check(R.id.maleRadioButton);
        } else {
            genderRadioGroup.check(R.id.femaleRadioButton);
        }
        for (int x = 0; x < checkBoxIDs.length; x++) {
            CheckBox checkBox = (CheckBox) rootView.findViewById(checkBoxIDs[x]);
            if (x != 1) {
                checkBox.setChecked(goals[x]);
            } else {
                switch (weightOptionEnum) {
                    case WEIGHT_GAIN:
                        spinner.setSelection(0);
                        checkBox.setChecked(true);
                        break;
                    case WEIGHT_LOSS:
                        spinner.setSelection(1);
                        checkBox.setChecked(true);
                        break;
                }
            }
        }
    }
}