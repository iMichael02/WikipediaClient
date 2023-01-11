package vn.edu.usth.wikiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class ChangeLangActivity extends AppCompatActivity {
    TextView messageView;
    TextView btnVn, btnEnglish;
    Context context;
    Resources resources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_lang);
        btnVn = findViewById(R.id.viButton);
        btnEnglish = findViewById(R.id.eButton);
        messageView = (TextView) findViewById(R.id.changeLang);


        ImageView back_btn_settings = findViewById(R.id.back_btn_settings);
        back_btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // setting up on click listener event over the button
        // in order to change the language with the help of
        // LocaleHelper class
        btnEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        btnVn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

    }

}
