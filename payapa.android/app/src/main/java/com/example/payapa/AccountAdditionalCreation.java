package com.example.payapa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
        Spinner roleSpinner = findViewById(R.id.role_spinner);
        Spinner departmentSpinner = findViewById(R.id.department_spinner);

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

        ArrayAdapter<CharSequence> roleAdapter = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.role_array)) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0; // Disable the default "Select User Role"
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (position == 0) {
                    // Make the default item "Select User Role" gray and unselectable
                    ((TextView) view).setTextColor(getResources().getColor(android.R.color.darker_gray));
                } else {
                    ((TextView) view).setTextColor(getResources().getColor(android.R.color.black));
                }
                return view;
            }
        };
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(roleAdapter);

        ArrayAdapter<CharSequence> departmentAdapter = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.department_array)) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0; // Disable the default "Select Department"
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (position == 0) {
                    // Make the default item "Select Department" gray and unselectable
                    ((TextView) view).setTextColor(getResources().getColor(android.R.color.darker_gray));
                } else {
                    ((TextView) view).setTextColor(getResources().getColor(android.R.color.black));
                }
                return view;
            }
        };
        departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentSpinner.setAdapter(departmentAdapter);

        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedRole = roleSpinner.getSelectedItem().toString();

                // Update the visibility based on the selected role
                if (selectedRole.equals("Student")) {
                    yearSpinner.setVisibility(View.VISIBLE);
                    courseSpinner.setVisibility(View.VISIBLE);
                    departmentSpinner.setVisibility(View.GONE);
                } else if (selectedRole.equals("Personnel") || selectedRole.equals("Faculty")) {
                    yearSpinner.setVisibility(View.GONE);
                    courseSpinner.setVisibility(View.GONE);
                    departmentSpinner.setVisibility(View.VISIBLE);
                } else {
                    // For any other roles, you can hide all or set them as needed
                    yearSpinner.setVisibility(View.GONE);
                    courseSpinner.setVisibility(View.GONE);
                    departmentSpinner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optionally handle the case where nothing is selected
            }
        });


        String firstNameInput = getIntent().getStringExtra("firstName");
        String middleNameInput = getIntent().getStringExtra("middleName");
        String lastNameInput = getIntent().getStringExtra("lastName");

        additionalInfoNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedGender = genderSpinner.getSelectedItem().toString();
                String selectedCourse = courseSpinner.getSelectedItem().toString();
                String selectedYear = yearSpinner.getSelectedItem().toString();
                String selectedRole = roleSpinner.getSelectedItem().toString();
                String selectedDepartment = departmentSpinner.getSelectedItem().toString();

                if (ageInput.getText().toString().isEmpty() ||
                        homeAddress.getText().toString().isEmpty() ||
                        contactNumber.getText().toString().isEmpty() ||
                        emailAddress.getText().toString().isEmpty() ||
                        fbLink.getText().toString().isEmpty()) {
                    Toast.makeText(AccountAdditionalCreation.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                }
                else if (selectedGender.equals("Select Gender") || selectedRole.equals("Select User Role") ||
                        (selectedRole.equals("Student") && (selectedCourse.equals("Select Course") || selectedYear.equals("Select Year Level"))) ||
                        ((selectedRole.equals("Personnel") || selectedRole.equals("Faculty")) && selectedDepartment.equals("Select Department"))) {
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
                    intent.putExtra("role", selectedRole);

                    if (selectedRole.equals("Student")) {
                        intent.putExtra("course", selectedCourse);
                        intent.putExtra("year", selectedYear);
                    } else if (selectedRole.equals("Personnel") || selectedRole.equals("Faculty")) {
                        intent.putExtra("department", selectedDepartment);
                    }

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
