package com.example.marketplaceproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.ContentValues;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;

public class AccountCreationActivity extends AppCompatActivity {

    private EditText fName, lName, email, pass, confirm_pass;
    private ImageView visible;
    private Button login;
    private DatabaseHelper db;

    private boolean toggleVisible = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
        db = new DatabaseHelper(this);
        db.getWritableDatabase();

        // Find views
        fName = findViewById(R.id.edittext_firstName);
        lName = findViewById(R.id.edittext_lastName);
        email = findViewById(R.id.edittext_email);
        pass = findViewById(R.id.edittext_password);
        confirm_pass = findViewById(R.id.edittext_confirmPass);
        login = findViewById(R.id.button_login);
        visible = findViewById(R.id.imageView_visible);

        // Toggle Visibility
        visible.setOnClickListener(v -> {
            if (toggleVisible) {
                // Hide Password
                visible.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_visibility_off, null));
                pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                confirm_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                toggleVisible = false;
            } else {
                // Show Password
                visible.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_visible, null));
                pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                confirm_pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                toggleVisible = true;
            }
        });

        login.setOnClickListener(view -> {
            validateInfo();
        });
    }

    public void validateInfo() {
        String first_name = fName.getText().toString();
        String last_name = lName.getText().toString();
        String email_addr = this.email.getText().toString();
        String password = pass.getText().toString();
        String confirm_password = confirm_pass.getText().toString();

        if (first_name.isEmpty() || last_name.isEmpty() || email_addr.isEmpty() ||
                password.isEmpty() || confirm_password.isEmpty()) {
            Toast.makeText(this, "Please fill out the highlighted fields.", Toast.LENGTH_SHORT).show();

            // Highlight empty fields
            if (first_name.isEmpty()) {
                fName.setBackgroundColor(getResources().getColor(R.color.error, null));
            } else {
                fName.setBackgroundColor(getResources().getColor(R.color.light_gray, null));
            } if (last_name.isEmpty()){
                lName.setBackgroundColor(getResources().getColor(R.color.error, null));
            } else {
                lName.setBackgroundColor(getResources().getColor(R.color.light_gray, null));
            } if (email_addr.isEmpty()){
                this.email.setBackgroundColor(getResources().getColor(R.color.error, null));
            } else {
                this.email.setBackgroundColor(getResources().getColor(R.color.light_gray, null));
            } if (password.isEmpty()){
                this.pass.setBackgroundColor(getResources().getColor(R.color.error, null));
            } else {
                this.pass.setBackgroundColor(getResources().getColor(R.color.light_gray, null));
            } if (confirm_password.isEmpty()){
                this.confirm_pass.setBackgroundColor(getResources().getColor(R.color.error, null));
            } else {
                this.confirm_pass.setBackgroundColor(getResources().getColor(R.color.light_gray, null));
            }
        } else if (db.userExists(email_addr)) {
            Toast.makeText(this, "Email already in use.", Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(confirm_password)) {
            Toast.makeText(this, "Passwords are different", Toast.LENGTH_SHORT).show();
        } else {
            ContentValues values = new ContentValues();

            values.put(DatabaseHelper.COLUMN_FIRST_NAME, first_name);
            values.put(DatabaseHelper.COLUMN_LAST_NAME, last_name);
            values.put(DatabaseHelper.COLUMN_EMAIL, email_addr);
            values.put(DatabaseHelper.COLUMN_PASS, password);

            long rowID = db.insert(values);

            if (rowID == -1) {
                Toast.makeText(this, "Error, try again", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

}