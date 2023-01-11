package vn.edu.usth.wikiapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class activity_signup extends AppCompatActivity {

    private EditText inputusername, inputpassword, inputpassword2;
    Button signup_button;
    ActionBar actionBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    ImageView showhidebtn1, showhidebtn2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        inputusername = findViewById(R.id.inputusername);
        inputpassword = findViewById(R.id.inputpassword);
        inputpassword2 = findViewById(R.id.inputpassword2);
        showhidebtn1 = findViewById(R.id.show_pass_btn);
        showhidebtn2 = findViewById(R.id.show_pass_btn2);

        showhidebtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowHidePass(view, inputpassword, 0);
            }
        });
        showhidebtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowHidePass(view, inputpassword2, 1);
            }
        });

        signup_button = findViewById(R.id.signup_button);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCredentials();
            }
        });

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Toast.makeText(this,"Not Logged In", Toast.LENGTH_SHORT).show();
            Log.i("status","not logged in");
        }
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
            signUp();
            Toast.makeText(activity_signup.this, "Create account successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public void signUp() {
        String checkusername = inputusername.getText().toString();
        String checkpassword = inputpassword.getText().toString();

        mAuth.createUserWithEmailAndPassword(checkusername, checkpassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("signUpStatus", "createUserWithEmail:success");
                            finish();
                            Intent intent = new Intent(activity_signup.this, MainActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("signUpStatus", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(activity_signup.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void initiateDocument() {
        CollectionReference dbCourses = db.collection("userData");
        FirebaseUser user = mAuth.getCurrentUser();
        String userEmail = user.getEmail();
        String userId = FirebaseAuth.getInstance().getUid();
        SavedInstance userData = new SavedInstance();
        userData.setFavorite(new ArrayList<String>());
        userData.setEmail(userEmail);
        userData.setUid(userId);
        dbCourses.document(userId).set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i( "Status updated", "Data added");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i( "Status updated", "Data not added");
            }
        });
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

    public void ShowHidePass(View view, EditText password, int number){
        switch (number) {
            case 0:
                if (view.getId() == R.id.show_pass_btn) {

                    if (password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                        ((ImageView) (view)).setImageResource(R.drawable.hide_password);

                        //Show Password
                        password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    } else {
                        ((ImageView) (view)).setImageResource(R.drawable.show_password);

                        //Hide Password
                        password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    }
                }
            case 1:
                if (view.getId() == R.id.show_pass_btn2) {

                    if (password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                        ((ImageView) (view)).setImageResource(R.drawable.hide_password);

                        //Show Password
                        password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    } else {
                        ((ImageView) (view)).setImageResource(R.drawable.show_password);

                        //Hide Password
                        password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    }
                }
        }
    }
}