package com.example.alirahal.androidcourse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    static String REMEMBER_ME_PREFERENCES= "RememberMeFile";

    DatabaseHandler databaseHandler;

    EditText usernameEditText;
    EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseHandler = new DatabaseHandler(this);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);


        SharedPreferences sharedPreferences = getSharedPreferences(REMEMBER_ME_PREFERENCES, MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);
        if (username != null) {
            String password = sharedPreferences.getString("password", "No name defined");
            usernameEditText.setText(username);
            passwordEditText.setText(password);
            ((CheckBox) findViewById(R.id.rememberMeCheckBox)).setChecked(true);

        }

    }

    public void loginHandler(View view) {
        User user = new User(usernameEditText.getText().toString(), passwordEditText.getText().toString());
        databaseHandler.getUser(user);
        if (user.getUser_id() == -1)
            Toast.makeText(this, "Wrong Username or Password", Toast.LENGTH_SHORT).show();
        else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("user", user);

            if(((CheckBox)findViewById(R.id.rememberMeCheckBox)).isChecked()) {
                SharedPreferences.Editor editor = getSharedPreferences(REMEMBER_ME_PREFERENCES, MODE_PRIVATE).edit();
                editor.putString("username", usernameEditText.getText().toString());
                editor.putString("password", passwordEditText.getText().toString());
                editor.apply();
            }


            startActivity(intent);
        }

    }

    public void signUpHandler(View view) {
        User user = new User(usernameEditText.getText().toString(), passwordEditText.getText().toString());
        if (databaseHandler.signUp(user))
            Toast.makeText(this, "Signed Up Successfully", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Username Already Taken", Toast.LENGTH_LONG).show();

    }
}
