package vn.edu.usth.wikiapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ArticleActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Versions> versionsList;
    Toolbar toolbar;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    SwitchCompat switchCompat;
    TextView search;
    TextView button;
    TextView receiver_msg;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private StringRequest mBigRequest;
    private JsonObjectRequest mJsonObjectRequest;
    String otherUrl = "https://en.wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&titles=pizza&format=json";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        recyclerView = findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.toolbarCustom);
        search = findViewById(R.id.searchButton);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSearchActivity();
            }
        });


        button = findViewById(R.id.homeButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMain();
            }
        });
        Intent intent = getIntent();
        String str = intent.getStringExtra("message_key");

        String url = "https://en.wikipedia.org/w/api.php?action=query&titles="+str+"&prop=extracts&explaintext&format=json";

        initData(url);


        // drawer layout instance to toggle the menu icon to open
//        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        Toast.makeText(this, "Message Key ID from previous activity" + str, Toast.LENGTH_SHORT).show();

        // to make the Navigation drawer icon always appear on the action bar
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void openSearchActivity() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }



    public void openMain() {
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


//    private void setRecyclerView() {
//        initData();
//    }



    private void initData(String url) {
        versionsList = new ArrayList<Versions>();

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
                        versionsList.add(new Versions(contentArr.get(i)[0],contentArr.get(i)[1]));

                    }

                    VersionsAdapter versionsAdapter = new VersionsAdapter(getApplicationContext(),versionsList);
                    recyclerView.setAdapter(versionsAdapter);
                    versionsAdapter.notifyDataSetChanged();

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

    @Override
    public void onPause() {
        // Always call the superclass method first
        super.onPause();
        Log.i("Weather Activity","onPause() finished");
    }

    @Override
    public void onResume() {
        // Always call the superclass method first
        super.onResume();
        Log.i("Weather Activity","onResume() finished");
    }

    @Override
    public void onStop() {
        // Always call the superclass method first
        super.onStop();
        Log.i("Weather Activity","onStop() finished");
    }

    @Override
    public void onDestroy() {
        // Always call the superclass method first
        super.onDestroy();
        Log.i("Weather Activity","onDestroy() finished");
    }

    public void onCustomToggleClick(View view) {
        Toast.makeText(this, "CustomToggle", Toast.LENGTH_SHORT).show();
    }

    public void onClickSearch(View view) {
        Toast.makeText(this, "Search custom", Toast.LENGTH_SHORT).show();
    }



    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}