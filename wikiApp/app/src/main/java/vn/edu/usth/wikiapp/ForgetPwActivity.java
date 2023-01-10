package vn.edu.usth.wikiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ForgetPwActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextView textView;
    LinearLayout layout;
    TextView cdTV;
    Button button;
    EditText editText;
    ImageView backbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pw);
        button = findViewById(R.id.rsButton);
        editText = findViewById(R.id.enterEmailET);
        layout = findViewById(R.id.cdText);
        cdTV = findViewById(R.id.cooldown);
        mAuth = FirebaseAuth.getInstance();
        ImageView backbtn = findViewById(R.id.back_btn_settings);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        FirebaseUser currentUser = mAuth.getCurrentUser();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                generateCooldown(editText.getText().toString());
                layout.setVisibility(View.VISIBLE);
                new CountDownTimer(50000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            // Used for formatting digit to be in 2 digits only
                            NumberFormat f = new DecimalFormat("00");
                            long min = (millisUntilFinished / 60000) % 60;
                            long sec = (millisUntilFinished / 1000) % 60;
                            cdTV.setText(f.format(min) + ":" + f.format(sec));
                            button.setOnClickListener(null);
                        }

                        // When the task is over it will print 00:00:00 there
                        public void onFinish() {
                            cdTV.setText("00:00");
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                        generateCooldown(editText.getText().toString());
                                        new CountDownTimer(50000, 1000) {
                                            public void onTick(long millisUntilFinished) {
                                                // Used for formatting digit to be in 2 digits only
                                                NumberFormat f = new DecimalFormat("00");
                                                long min = (millisUntilFinished / 60000) % 60;
                                                long sec = (millisUntilFinished / 1000) % 60;
                                                cdTV.setText(f.format(min) + ":" + f.format(sec));
                                                button.setOnClickListener(null);
                                            }

                                            // When the task is over it will print 00:00:00 there

                                            public void onFinish() {
                                                cdTV.setText("00:00");
                                                layout.setVisibility(View.GONE);
                                            }

                                        }.start();

//                                    else {
//                                        Toast.makeText(ForgetPwActivity.this, "Enter your email.", Toast.LENGTH_SHORT).show();
//                                    }
                                }
                            });
                        }

                    }.start();

//                else {
//                    Toast.makeText(ForgetPwActivity.this, "Enter your email.", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

    public void generateCooldown(String email) {
//        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        String email = currentUser.getEmail();
        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("statusMail","email sent");
            }
        });
    }
}