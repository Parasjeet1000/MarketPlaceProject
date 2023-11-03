package com.example.marketplaceproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.accounts.Account;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText pass;
    private DatabaseHelper db;
    private boolean toggleVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        db.getWritableDatabase();

        TextView createAcc = findViewById(R.id.textview_createAccount);
        Button loginBtn = findViewById(R.id.button_login);
        ImageView visible = findViewById(R.id.imageView_visible);

        email = findViewById(R.id.edittext_email);
        pass = findViewById(R.id.edittext_password);

        // Toggle password visibility
        visible.setOnClickListener(v -> {
            if (toggleVisible) {
                // Hide Password
                visible.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_visibility_off, null));
                pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                toggleVisible = false;
            } else {
                // Show Password
                visible.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_visible, null));
                pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                toggleVisible = true;
            }
        });

        loginBtn.setOnClickListener(v -> checkCredentials());

        createAcc.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), AccountCreationActivity.class);
            startActivity(intent);
        });

    }

    public void checkCredentials() {
        String email_addr = email.getText().toString();
        String password = pass.getText().toString();

        if (db.userExists(email_addr)) {
            Cursor user = db.getUser(email_addr);
            user.moveToNext();
            String checkPass = user.getString(user.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASS));

            if (password.equals(checkPass)) {
                Toast.makeText(this, "Logging in...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Password Incorrect.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Email not registered.", Toast.LENGTH_SHORT).show();
        }
    }
}