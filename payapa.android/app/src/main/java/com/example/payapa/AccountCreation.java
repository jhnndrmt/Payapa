package com.example.payapa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AccountCreation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        Button userInfoNextBtn = findViewById(R.id.user_info_nextBtn);
        EditText firstNameInput = findViewById(R.id.fname);
        EditText middleNameInput = findViewById(R.id.middleName);
        EditText lastNameInput = findViewById(R.id.lastName);

        TextView signInBtn = findViewById(R.id.signin);

        userInfoNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if any of the fields are empty
                if (firstNameInput.getText().toString().isEmpty() ||
                        middleNameInput.getText().toString().isEmpty() ||
                        lastNameInput.getText().toString().isEmpty()) {

                    // Display a toast if any field is empty
                    Toast.makeText(AccountCreation.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // If all fields are filled, proceed to the next activity
                    Intent intent = new Intent(AccountCreation.this, AccountAdditionalCreation.class);
                    intent.putExtra("firstName", firstNameInput.getText().toString());
                    intent.putExtra("middleName", middleNameInput.getText().toString());
                    intent.putExtra("lastName", lastNameInput.getText().toString());
                    startActivity(intent);
                }
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountCreation.this, LoginActivity.class));

            }
        });
    }
}
