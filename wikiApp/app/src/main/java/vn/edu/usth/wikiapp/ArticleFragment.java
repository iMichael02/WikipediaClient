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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    private void filter(String text, ArticleFragmentAdapter adapter) {
        // creating a new array list to filter our data.

        ArrayList<SearchResult> filteredlist = new ArrayList<SearchResult>();
//

//        // running a for loop to compare elements.
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
        }
        else {

            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist);
//            dataInit();
//            Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
//            adapter.filterList(filteredlist);

        }

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
        FirebaseUser user = mAuth.getCurrentUser();

        // initializing our adapter class.


        searchView = view.findViewById(R.id.searchViewHome);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                if(user!=null) {
                    linearLayout = view.findViewById(R.id.missingText);
                    linearLayout.setVisibility(View.GONE);

                    if (newText.length() > 0) {
                        // Search
                        textView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        dataInitCustom(newText);
                    } else {
                        // Do something when there's no input
                        textView.setVisibility(View.VISIBLE);
                        Toast t = Toast.makeText(getContext(), "close", Toast.LENGTH_SHORT);
                        recyclerView.setVisibility(View.GONE);
                        t.show();
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
                        Toast t = Toast.makeText(getContext(), "close", Toast.LENGTH_SHORT);
                        recyclerView.setVisibility(View.GONE);
                        linearLayout = view.findViewById(R.id.missingText);
                        linearLayout.setVisibility(View.VISIBLE);
                        t.show();
                    }
                }


                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Toast t = Toast.makeText(getContext(), "close", Toast.LENGTH_SHORT);
                recyclerView.setVisibility(View.GONE);
                t.show();
                return false;
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
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
                    ArticleFragmentAdapter adapter = new ArticleFragmentAdapter(getContext(), SearchResultArrayList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    for (SearchResult item : SearchResultArrayList) {
                        // checking if the entered string matched with any item of our recycler view.
                        filteredlist.add(item);
                    }
                    if (filteredlist.isEmpty()) {
                        // if no item is added in filtered list we are
                        // displaying a toast message as no data found.
                        Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // at last we are passing that filtered
                        // list to our adapter class.
                        adapter.filterList(filteredlist);
                    }
                    // adding layout manager to our recycler view.
                    // setting adapter to
                    // our recycler view.

                    adapter.notifyDataSetChanged();


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

}