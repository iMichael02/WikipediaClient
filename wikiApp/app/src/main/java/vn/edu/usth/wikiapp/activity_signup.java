package vn.edu.usth.wikiapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class activity_signup extends AppCompatActivity {

    private EditText inputusername, inputpassword, inputpassword2;
    Button signup_button;
    ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        inputusername = findViewById(R.id.inputusername);
        inputpassword = findViewById(R.id.inputpassword);
        inputpassword2 = findViewById(R.id.inputpassword2);
        signup_button = findViewById(R.id.signup_button);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCredentials();
            }
        });
    }

    private void checkCredentials() {
        String checkusername = inputusername.getText().toString();
        String checkpassword = inputpassword.getText().toString();
        if (checkusername.isEmpty() || checkusername.length() < 5) {
            showError(inputusername, "Your username is not valid! Username must have over 5 characters!");
            Toast.makeText(activity_signup.this, "Create account failed!", Toast.LENGTH_SHORT).show();
        } else if (checkpassword.isEmpty() || checkpassword.length() < 5) {
            showError(inputpassword, "Your password is not valid! Password must have over 5 characters!");
            Toast.makeText(activity_signup.this, "Create account failed!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(activity_signup.this, "Create account successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showError(EditText username, String s) {
        username.setError(s);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}