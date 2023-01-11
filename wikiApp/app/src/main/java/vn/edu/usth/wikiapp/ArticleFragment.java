package vn.edu.usth.wikiapp;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArticleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArticleFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private ImageRequest mImageRequest;
    private StringRequest mBigRequest;
    private JsonObjectRequest mJsonObjectRequest;
    private String url = "https://en.wikipedia.org/w/api.php?action=query&titles=pizza&prop=extracts&explaintext&format=json";


    private RecyclerView recyclerView;
    private ArticleFragmentAdapter articleFragmentAdapter;
    private ArrayList<SearchResult> SearchResultArrayList;
    LinearLayout linearLayout;
    TextView textView;
    SearchView searchView;
    private FirebaseFirestore db;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseAuth mAuth;


    public ArticleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArticleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArticleFragment newInstance(String param1, String param2) {
        ArticleFragment fragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_article, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerViewHome);
        textView = view.findViewById(R.id.pastSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // initializing our adapter class.
        searchView = view.findViewById(R.id.searchViewHome);
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null) {
            getPastSearch();
            linearLayout = view.findViewById(R.id.missingText);
            textView.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
        }
        else {
            linearLayout = view.findViewById(R.id.missingText);
            textView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);

        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(user!=null) {
                    linearLayout = view.findViewById(R.id.missingText);
                    linearLayout.setVisibility(View.GONE);
                    getPastSearch();
                    if (newText.length() > 0) {
                        // Search
                        textView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        dataInitCustom(newText);
                    } else {
                        // Do something when there's no input
                        textView.setVisibility(View.VISIBLE);
                        getPastSearch();
                    }
                }
                else {
                    linearLayout = view.findViewById(R.id.missingText);
                    linearLayout.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);

                    if (newText.length() > 0) {
                        // Search
                        recyclerView.setVisibility(View.VISIBLE);
                        linearLayout = view.findViewById(R.id.missingText);
                        linearLayout.setVisibility(View.GONE);
                        dataInitCustom(newText);
                    } else {
                        // Do something when there's no input
                        linearLayout = view.findViewById(R.id.missingText);
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                }
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                recyclerView.setVisibility(View.GONE);
                return false;
            }
        });

    }

    private void getPastSearch() {
        db = FirebaseFirestore.getInstance();
        CollectionReference dbCourses = db.collection("userPastSearches");
        FirebaseUser user = mAuth.getCurrentUser();
        DocumentReference docRef = dbCourses.document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<String> pastList = (ArrayList<String>) document.getData().get("past");
                        PastSearchAdapter pastAdapter = new PastSearchAdapter(pastList, getContext(), getView().findViewById(R.id.searchViewHome));
                        recyclerView.setAdapter(pastAdapter);
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    }
                    else {
                        Log.d(TAG, "none " );
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void dataInitCustom(String text) {

        // below line we are creating a new array list
        String shortUrl = "https://en.wikipedia.org/w/api.php?action=query&formatversion=2&generator=prefixsearch&gpssearch="+text+"&gpslimit=10&prop=pageimages%7Cpageterms&piprop=thumbnail&pithumbsize=50&pilimit=10&redirects=&wbptterms=description&format=json";
        SearchResultArrayList = new ArrayList<SearchResult>();
        mRequestQueue = Volley.newRequestQueue(getContext());
        mStringRequest = new StringRequest(Request.Method.GET, shortUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject queryObject = jsonObject.getJSONObject("query");
                    JSONArray pageObject = queryObject.getJSONArray("pages");

                    for(int i = 0; i < pageObject.length(); i++) {
                        JSONObject firstObject = pageObject.getJSONObject(i);
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
                        SearchResultArrayList.add(new SearchResult(title,fullDesc,String.valueOf(i),imageUrl));
                    }
                    ArrayList<SearchResult> filteredlist = new ArrayList<SearchResult>();
                    String key = searchView.getQuery().toString();
                    ArticleFragmentAdapter adapter = new ArticleFragmentAdapter(getContext(), SearchResultArrayList, key);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    for (SearchResult item : SearchResultArrayList) {
                        filteredlist.add(item);
                    }
                    if (filteredlist.isEmpty()) {
                        Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        adapter.filterList(filteredlist);
                    }

                    adapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "ERROR :" + error.toString());
            }
        });
        mRequestQueue.add(mStringRequest);
    }

}