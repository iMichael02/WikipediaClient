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
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


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
    private DatabaseReference mDatabase;
    SwitchCompat switchCompat;
    TextView search;
    TextView button;
    TextView receiver_msg;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    TextView userEmail;
    private StringRequest mBigRequest;
    private JsonObjectRequest mJsonObjectRequest;
    String otherUrl = "https://en.wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&titles=pizza&format=json";
    DisplayMetrics displayMetrics = new DisplayMetrics();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        recyclerView = findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.toolbarCustom);
        db = FirebaseFirestore.getInstance();


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Intent intent = getIntent();
        String str = intent.getStringExtra("message_key");
        String url = "https://en.wikipedia.org/w/api.php?action=query&prop=extracts|pageimages|pageterms&explaintext&format=json&format=json&pithumbsize="+String.valueOf(getScreenWidth())+"&titles="+str;
        initData(url);

        if(currentUser!= null) {
            userEmail = findViewById(R.id.userEmailNavArticle);
            userEmail.setText(currentUser.getEmail());
            ToggleButton toggleBtn = findViewById(R.id.bookmark);
            toggleBtn.setVisibility(View.VISIBLE);
            CollectionReference dbCourses = db.collection("userData");
            String userEmail = currentUser.getEmail();
            String userId = FirebaseAuth.getInstance().getUid();
            ArrayList<String> favList;

            dbCourses.document(userId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    ArrayList<String> favList = (ArrayList<String>) document.getData().get("favorite");
                                    if(favList.contains(str)) {
                                        Log.d("statusUpate", "Document exists");
                                        toggleBtn.setChecked(true);
                                        toggleBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // on below line we are checking if
                                                // toggle button is checked or not.
                                                if (toggleBtn.isChecked()) {
                                                    Intent intent = getIntent();
                                                    String str = intent.getStringExtra("message_key");
                                                    favList.add(str);
                                                    SavedInstance user = new SavedInstance();
                                                    user.setFavorite(favList);
                                                    user.setEmail(userEmail);
                                                    user.setUid(userId);
                                                    dbCourses.document(userId).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(ArticleActivity.this, "data removed from fav", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(ArticleActivity.this, "Fail to add data " + e, Toast.LENGTH_SHORT).show();

                                                        }
                                                    });
                                                    Log.i("new Status","favorite true");
                                                } else {
                                                    Intent intent = getIntent();
                                                    String str = intent.getStringExtra("message_key");
                                                    favList.remove(str);
                                                    SavedInstance user = new SavedInstance();
                                                    user.setFavorite(favList);
                                                    user.setEmail(userEmail);
                                                    user.setUid(userId);
                                                    dbCourses.document(userId).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(ArticleActivity.this, "data added to fav", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(ArticleActivity.this, "Fail to add data " + e, Toast.LENGTH_SHORT).show();

                                                        }
                                                    });
                                                    Log.i("new Status","favorite false");

                                                }
                                            }
                                        });
                                    }
                                    else {
                                        toggleBtn.setChecked(false);
                                        toggleBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // on below line we are checking if
                                                // toggle button is checked or not.
                                                if (toggleBtn.isChecked()) {
                                                    Intent intent = getIntent();
                                                    String str = intent.getStringExtra("message_key");
                                                    favList.add(str);
                                                    SavedInstance user = new SavedInstance();
                                                    user.setFavorite(favList);
                                                    user.setEmail(userEmail);
                                                    user.setUid(userId);
                                                    dbCourses.document(userId).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(ArticleActivity.this, "data removed from fav", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(ArticleActivity.this, "Fail to add data " + e, Toast.LENGTH_SHORT).show();

                                                        }
                                                    });
                                                    Log.i("new Status","favorite true");
                                                } else {
                                                    Intent intent = getIntent();
                                                    String str = intent.getStringExtra("message_key");
                                                    favList.remove(str);
                                                    SavedInstance user = new SavedInstance();
                                                    user.setFavorite(favList);
                                                    user.setEmail(userEmail);
                                                    user.setUid(userId);
                                                    dbCourses.document(userId).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(ArticleActivity.this, "data added to fav", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(ArticleActivity.this, "Fail to add data " + e, Toast.LENGTH_SHORT).show();

                                                        }
                                                    });
                                                    Log.i("new Status","favorite false");

                                                }
                                            }
                                        });
                                    }
                                } else {
                                    Log.d("statusUpate", "No such user data exists");
                                }
                            } else {
                                Log.d("statusUpate", "get failed with ", task.getException());
                            }
                        }
                    });




        }



        button = findViewById(R.id.homeButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMain();
            }
        });



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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void writeNewUser(String title) {
        Intent intent = getIntent();
        String str = intent.getStringExtra("message_key");
        CollectionReference dbCourses = db.collection("userData");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userEmail = currentUser.getEmail();
        String userId = FirebaseAuth.getInstance().getUid();


        dbCourses.document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<String> currArr = (ArrayList<String>) document.getData().get("favorite");
                        currArr.add(str);

                        SavedInstance user = new SavedInstance();
                        user.setFavorite(currArr);
                        user.setEmail(userEmail);
                        user.setUid(userId);
                        dbCourses.document(userId).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ArticleActivity.this, "data added", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ArticleActivity.this, "Fail to add data " + e, Toast.LENGTH_SHORT).show();

                            }
                        });

                    } else {
                        ArrayList<String> arr = new ArrayList<String>();
                        arr.add(str);
                        SavedInstance user = new SavedInstance();
                        user.setFavorite(arr);
                        user.setEmail(userEmail);
                        user.setUid(userId);
                        dbCourses.document(userId).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ArticleActivity.this, "data added", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ArticleActivity.this, "Fail to add data " + e, Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });




//        ArrayList<String> arr = new ArrayList<String>();
//        SavedInstance user = new SavedInstance();
//        user.setFavorite(new ArrayList<String>());
//        user.setEmail(userEmail);
//        user.setUid(userId);
//        dbCourses.document(userId).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Toast.makeText(ArticleActivity.this, "data added", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(ArticleActivity.this, "Fail to add data " + e, Toast.LENGTH_SHORT).show();
//
//            }
//        });
//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                mDatabase.setValue(user);
//                Toast.makeText(ArticleActivity.this, "data added", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(ArticleActivity.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
//
//            }
//        });
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
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
}

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


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
                    String title = extractedObject.getString("title");
                    JSONObject terms = extractedObject.getJSONObject("terms");
                    String desc = terms.getString("description");
                    String Text = extractedObject.getString("extract");
                    String thumbnail;
                    if(extractedObject.has("thumbnail")) {
                        JSONObject thumbnailObj = extractedObject.getJSONObject("thumbnail");
                        thumbnail = thumbnailObj.getString("source");
                    }
                    else {
                        thumbnail = "https://phutungnhapkhauchinhhang.com/wp-content/uploads/2020/06/default-thumbnail.jpg";
                    }
                    Log.i("thumbnail",thumbnail);
                    ImageView mainPhoto = findViewById(R.id.mainPhoto);
                    new ImageLoadTask(thumbnail, mainPhoto).execute();

                    TextView titleView = findViewById(R.id.articleTitle);
                    TextView descView = findViewById(R.id.articleDesc);

                    titleView.setText(title);
                    String myString = desc.replaceAll("\"","").replaceAll("\\[","").replaceAll("\\]","");
                    descView.setText(toTitleCase(myString));

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

    public static String toTitleCase(String givenString) {
        String[] arr = givenString.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
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