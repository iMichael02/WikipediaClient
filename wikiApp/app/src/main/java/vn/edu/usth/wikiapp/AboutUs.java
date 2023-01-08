package vn.edu.usth.wikiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;


public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        Element adsElement = new Element();
        View aboutPage = new AboutPage(this, R.style.Widget_App_AboutPage)
                .isRTL(false)
                .setImage(R.drawable.enwiki)
                .setDescription(" This is our project on Mobile Application Development. We want to send our thanks to Mr. Tran Giang Son for everything we have learnt in this course." +"\n" + "Our group members: " + "\n" + "Nguyen Tuong Quang Hai BI11-073 " + "\n" + "Bui Tuan Minh BI11-167" + "\n" + "Nguyen Hoang Nam BI11-196 " + "\n" + "Phan Minh Quang BI11-232 " )
                .addItem(new Element().setTitle("Version 1.0"))
                .addGroup("CONNECT WITH US!")
                .addEmail("namnh.bi11-196@st.usth.edu.vn")
                .addFacebook("https://www.facebook.com/profile.php?id=100008666803066")
                .addInstagram("https://www.instagram.com/")
                .addGitHub("https://github.com/iMichael02/WikipediaClient")
                .addItem(createCopyright())
                .create();
        setContentView(aboutPage);
    }
    private Element createCopyright()
    {
        Element copyright = new Element();
        @SuppressLint("DefaultLocale") final String copyrightString = String.format("Copyright %d", Calendar.getInstance().get(Calendar.YEAR));
        copyright.setTitle(copyrightString);
        // copyright.setIcon(R.mipmap.ic_launcher);
        copyright.setGravity(Gravity.CENTER);
        copyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutUs.this,copyrightString,Toast.LENGTH_SHORT).show();
            }
        });
        return copyright;
    }
}
