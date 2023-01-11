package vn.edu.usth.wikiapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Parcelable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    private Button signup_button;
    private EditText username, password;
    private FirebaseAuth mAuth;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private ArrayList<SearchResult> SearchResultArrayList;
    private ArticleFragmentSavedAdapter adapter;
    private ImageView showhidebtn;



// ...


    public AccountFragment() {
        // Required empty public constructor
    }

    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Log.i("Status","Logged in successfully");
        }
        Log.i("asdfasd","asdfffffffffffgg");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            View view = inflater.inflate(R.layout.fragment_account_loggedin, container, false);
            String email = currentUser.getEmail();
            TextView emailText;
//            emailText = view.findViewById(R.id.emailText);
//            emailText.setText(email);
            SearchView searchView = view.findViewById(R.id.searchViewSaved);
            recyclerView = view.findViewById(R.id.savedPostsRV);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            dataInitCustom();

            return view;
        }
        else {
            View view = inflater.inflate(R.layout.fragment_account, container, false);
            username = view.findViewById(R.id.username);
            password = view.findViewById(R.id.password);
            showhidebtn = view.findViewById(R.id.show_pass_btn);
            String usernameValue = username.getText().toString();
            String passwordValue = password.getText().toString();

            showhidebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShowHidePass(view, password);
                }
            });
            TextView noPw = view.findViewById(R.id.noPw);
            noPw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), ForgetPwActivity.class);
                    startActivity(intent);

                }
            });
            Button signin_button = (Button) view.findViewById(R.id.signin_button);

            // username: admin, password: admin
            signin_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAuth.signInWithEmailAndPassword(String.valueOf(username.getText()), String.valueOf(password.getText()))
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("TAG", "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        getActivity().finish();
                                        Intent intent = new Intent(getContext(), MainActivity.class);
                                        startActivity(intent);

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("TAG", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(getContext(), "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });

            signup_button = (Button) view.findViewById(R.id.signup_button);
            signup_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    opensignup();
                }
            });
            return view;
        }
    }

    private void dataInitCustom() {
        db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getUid();
        ArrayList<String> arrayList = new ArrayList<String>();
        CollectionReference dbCourses = db.collection("userData");
        Handler handler = new Handler();
        dbCourses.document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        ArrayList<String> favList = (ArrayList<String>) document.getData().get("favorite");
                        SearchResultArrayList = new ArrayList<SearchResult>();
                        mRequestQueue = Volley.newRequestQueue(getContext());

                        for(int i = 0; i < favList.size(); i++ ) {
                            String shortUrl = "https://en.wikipedia.org/w/api.php?action=query&formatversion=2&generator=prefixsearch&gpssearch="+favList.get(i)+"&gpslimit=10&prop=pageimages%7Cpageterms&piprop=thumbnail&pithumbsize=50&pilimit=10&redirects=&wbptterms=description&format=json";
                            mStringRequest = new StringRequest(Request.Method.GET, shortUrl, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        JSONObject queryObject = jsonObject.getJSONObject("query");
                                        JSONArray pageObject = queryObject.getJSONArray("pages");
                                        JSONObject firstObject = pageObject.getJSONObject(0);
                                        String imageUrl;
                                        if(firstObject.has("thumbnail")) {
                                            JSONObject thumbnail = firstObject.getJSONObject("thumbnail");
                                            imageUrl = thumbnail.getString("source");
                                        }
                                        else {
                                            imageUrl = "https://phutungnhapkhauchinhhang.com/wp-content/uploads/2020/06/default-thumbnail.jpg";
                                        }
                                        JSONObject terms = firstObject.getJSONObject("terms");
                                        String title = firstObject.getString("title");
                                        JSONArray desc = terms.getJSONArray("description");
                                        String fullDesc = desc.getString(0);
                                        SearchResultArrayList.add(new SearchResult(title,fullDesc,"id",imageUrl));
                                        if(SearchResultArrayList.size()>0) {
                                            Log.i("dataUpdated",SearchResultArrayList.get(0).getTitle());
                                            SearchView searchView = getView().findViewById(R.id.searchViewSaved);
                                            adapter = new ArticleFragmentSavedAdapter(getContext(), SearchResultArrayList);
                                            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                                @Override
                                                public boolean onQueryTextSubmit(String query) {
                                                    return false;
                                                }

                                                @Override
                                                public boolean onQueryTextChange(String newText) {
                                                    // inside on query text change method we are
                                                    // calling a method to filter our recycler view.
                                                    filter(newText);
                                                    return false;
                                                }
                                            });
                                            recyclerView.setAdapter(adapter);
                                            adapter.notifyDataSetChanged();
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


//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                if(SearchResultArrayList.size()>0) {
//                                    Log.i("dataUpdated",SearchResultArrayList.get(0).getTitle());
//                                    SearchView searchView = getView().findViewById(R.id.searchViewSaved);
//                                    adapter = new ArticleFragmentSavedAdapter(getContext(), SearchResultArrayList);
//                                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                                        @Override
//                                        public boolean onQueryTextSubmit(String query) {
//                                            return false;
//                                        }
//
//                                        @Override
//                                        public boolean onQueryTextChange(String newText) {
//                                            // inside on query text change method we are
//                                            // calling a method to filter our recycler view.
//                                            filter(newText);
//                                            return false;
//                                        }
//                                    });
//                                    recyclerView.setAdapter(adapter);
//                                    adapter.notifyDataSetChanged();
//                                }
//                            }
//                        }, 1000);
                    }
                    else {
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
                }
            }

        });

        // below line we are creating a new array list
        // get ArrayList from firebase and combine it here

    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<SearchResult> filteredlist = new ArrayList<SearchResult>();

        // running a for loop to compare elements.
        for (SearchResult item : SearchResultArrayList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist);
        }
    }


    public void opensignup() {
        Intent intent = new Intent(getContext(), activity_signup.class);
        startActivity(intent);
    }

    public void ShowHidePass(View view, EditText password){
        if(view.getId()==R.id.show_pass_btn){

            if(password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.hide_password);

                //Show Password
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.show_password);

                //Hide Password
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }
}