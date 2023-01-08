package vn.edu.usth.wikiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import static android.content.ContentValues.TAG;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ViewPager2 mViewPager2;
    private BottomNavigationView mBottomNavigationView;
    SwitchCompat switchCompat;
    LinearLayout settings;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private StringRequest mBigRequest;
    private JsonObjectRequest mJsonObjectRequest;
    private String url = "https://en.wikipedia.org/w/api.php?action=query&titles=pizza&prop=extracts&explaintext&format=json";
    String otherUrl = "https://en.wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&titles=pizza&format=json";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle(getString(R.string.app_name));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);


        SharedPreferences sharedPreferences = null;

        sharedPreferences = getSharedPreferences( "night", 0);

        Boolean booleanValue = sharedPreferences.getBoolean("night_mode", true);
        if(booleanValue) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        TextView aboutUsTv = findViewById(R.id.about_us_article);
        mDrawerLayout = findViewById(R.id.drawer_layout_main);
        settings = findViewById(R.id.settings_home);
        mViewPager2 = findViewById(R.id.view_pager);
        mBottomNavigationView = findViewById(R.id.bottom_navigation);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        mViewPager2.setAdapter(adapter);
        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_home).setChecked(true);
                        break;
                    case 1:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_article).setChecked(true);
                        break;
                    case 2:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_account).setChecked(true);
                        break;
                }
            }
        });

        aboutUsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AboutUs.class);
                startActivity(intent);
            }
        });




        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case 0:
                        mViewPager2.setCurrentItem(0);
                        break;
                    case 1:
                        mViewPager2.setCurrentItem(1);
                        break;
                    case 2:
                        mViewPager2.setCurrentItem(2);
                        break;
                }
                return false;
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettings();
            }
        });
//        replaceFragment(new ArticleFragment());
        getData();
    }


    public void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        mRequestQueue = Volley.newRequestQueue(this);

        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject queryObject = jsonObject.getJSONObject("query");
                    JSONObject pageObject = queryObject.getJSONObject("pages");
                    int pageNo = Integer.parseInt(pageObject.names().getString(0));
                    JSONObject extractedObject = pageObject.getJSONObject(String.valueOf(pageNo));
                    String Text = extractedObject.getString("extract");
                    String[] TextArray = Text.replaceAll("\n","~").replaceAll("\t","~").split("(?=((?<!=)[=]{2}\\s*[a-zA-Z0-9\\s]*\\s[=]{2}))");

                    List<String> list = new ArrayList<String>(Arrays.asList(TextArray));
                    for(int i = TextArray.length -1 ; i >0;  i--) {
                        if(list.get(i).replaceAll("\n","").replaceAll("\\s+", "").replaceAll("=","").length() < 30) {
                            list.remove(i);
                        }
                    }

                    ArrayList<String[]> contentArr = new ArrayList<>();
                    for(int i = 0; i < list.size(); i++) {
                        if(i == 0 ) {
                            contentArr.add(new String[]{"Description", String.valueOf(list.get(i)).replaceAll("~","")});
                        }
                        else {
                            String[] temp = list.get(i).split("(?<=([=]{2}\\s[a-zA-Z0-9\\s]{0,200}\\s[=]{2}))");
                            contentArr.add(new String[] {temp[0].replaceAll("== ","").replaceAll(" ==",""), temp[1].replaceAll("~","")});
                        }

                    }

                    for(int i = 0; i < contentArr.size(); i++) {
                        Log.i("data",String.valueOf(contentArr.get(i)[1]));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "ERRORERRORERROR :" + error.toString());
            }
        });
        mRequestQueue.add(mStringRequest);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager  = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_layout, fragment);
        fragmentTransaction.commit();

    }
//^[a-zA-Z0-9].*
//    (?<===)(.*)(?===)
}