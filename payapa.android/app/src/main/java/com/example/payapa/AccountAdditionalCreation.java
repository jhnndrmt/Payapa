package com.example.payapa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AccountAdditionalCreation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_additional_information);

        EditText ageInput = findViewById(R.id.age_input);
        EditText homeAddress = findViewById(R.id.complete_address);
        EditText contactNumber = findViewById(R.id.mobile_number);
        EditText emailAddress = findViewById(R.id.email_address);
        EditText fbLink = findViewById(R.id.facebook_link);

        Button additionalInfoNextBtn = findViewById(R.id.user_additional_info_nextBtn);
        TextView signInBtn = findViewById(R.id.signin);

        // Dropdown id's
        Spinner genderSpinner = findViewById(R.id.gender_spinner);
        Spinner courseSpinner = findViewById(R.id.course_spinner);
        Spinner yearSpinner = findViewById(R.id.year_spinner);

        // Populate gender dropdown
        ArrayAdapter<CharSequence> genderAdapter = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.gender_array)) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0; // Disable the default "Select Gender"
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (position == 0) {
                    // Make the default item "Select Gender" gray and unselectable
                    ((TextView) view).setTextColor(getResources().getColor(android.R.color.darker_gray));
                } else {
                    ((TextView) view).setTextColor(getResources().getColor(android.R.color.black));
                }
                return view;
            }
        };
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        // Populate course dropdown
        ArrayAdapter<CharSequence> courseAdapter = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.course_array)) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0; // Disable the default "Select Course"
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (position == 0) {
                    // Make the default item "Select Course" gray and unselectable
                    ((TextView) view).setTextColor(getResources().getColor(android.R.color.darker_gray));
                } else {
                    ((TextView) view).setTextColor(getResources().getColor(android.R.color.black));
                }
                return view;
            }
        };
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(courseAdapter);

        // Populate year level dropdown
        ArrayAdapter<CharSequence> yearAdapter = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.year_array)) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0; // Disable the default "Select Year Level"
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (position == 0) {
                    // Make the default item "Select Year Level" gray and unselectable
                    ((TextView) view).setTextColor(getResources().getColor(android.R.color.darker_gray));
                } else {
                    ((TextView) view).setTextColor(getResources().getColor(android.R.color.black));
                }
                return view;
            }
        };
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        String firstNameInput = getIntent().getStringExtra("firstName");
        String middleNameInput = getIntent().getStringExtra("middleName");
        String lastNameInput = getIntent().getStringExtra("lastName");

        additionalInfoNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedGender = genderSpinner.getSelectedItem().toString();
                String selectedCourse = courseSpinner.getSelectedItem().toString();
                String selectedYear = yearSpinner.getSelectedItem().toString();

                if (ageInput.getText().toString().isEmpty() ||
                        homeAddress.getText().toString().isEmpty() ||
                        contactNumber.getText().toString().isEmpty() ||
                        emailAddress.getText().toString().isEmpty() ||
                        fbLink.getText().toString().isEmpty()) {
                    Toast.makeText(AccountAdditionalCreation.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                }
                else if (selectedGender.equals("Select Gender") || selectedCourse.equals("Select Course") || selectedYear.equals("Select Year Level")) {
                    Toast.makeText(AccountAdditionalCreation.this, "Please select a valid option for all fields", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(AccountAdditionalCreation.this, AccountCreationRegistration.class);

                    intent.putExtra("firstName", firstNameInput);
                    intent.putExtra("middleName", middleNameInput);
                    intent.putExtra("lastName", lastNameInput);
                    intent.putExtra("age", ageInput.getText().toString());
                    intent.putExtra("homeAddress", homeAddress.getText().toString());
                    intent.putExtra("contactNumber", contactNumber.getText().toString());
                    intent.putExtra("emailAddress", emailAddress.getText().toString());
                    intent.putExtra("fbLink", fbLink.getText().toString());
                    intent.putExtra("gender", selectedGender);
                    intent.putExtra("course", selectedCourse);
                    intent.putExtra("year", selectedYear);

                    startActivity(intent);
                }
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountAdditionalCreation.this, LoginActivity.class));

            }
        });
    }
}
