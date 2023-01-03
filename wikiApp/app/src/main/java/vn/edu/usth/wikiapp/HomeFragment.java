package vn.edu.usth.wikiapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private RecyclerView recyclerView;
    String dayText;
    String monthText;




    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout linearLayout = (LinearLayout) getView().findViewById(R.id.clickDemo);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ArticleActivity.class);
                startActivity(intent);

            }
        });
        dataInit();
    }

    public void dataInit() {
        mRequestQueue = Volley.newRequestQueue(getContext());
        Date date;
        Calendar currentTime = Calendar.getInstance();
        int year = currentTime.get(Calendar.YEAR);
        int month = currentTime.get(Calendar.MONTH)+1;
        int day = currentTime.get(Calendar.DAY_OF_MONTH);
        if(month<10) {
            monthText = "0"+String.valueOf(month);
        }
        if(day<10) {
            dayText = "0"+String.valueOf(day);
        }

        TextView todaysTitle = getView().findViewById(R.id.todaysTitle);
        ImageView todaysPhoto = getView().findViewById(R.id.todaysPhoto);
        TextView todaysDate = getView().findViewById(R.id.todaysDate);
        TextView todaysContent = getView().findViewById(R.id.todaysContent);
        String dateText = day+"/"+month+"/"+year;
        String url = "https://en.wikipedia.org/api/rest_v1/feed/featured/"+String.valueOf(year)+"/"+String.valueOf(monthText)+"/"+String.valueOf(dayText);

        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject tfa = jsonObject.getJSONObject("tfa");
                    String title = tfa.getString("title").replaceAll("_"," ");
                    JSONObject thumbnail = tfa.getJSONObject("thumbnail");
                    String source = thumbnail.getString("source");
                    JSONObject contentObject = tfa.getJSONObject("content_urls");
                    String extract = tfa.getString("extract");
                    new ImageLoadTask(source, todaysPhoto).execute();
                    todaysTitle.setText(title);
                    todaysDate.setText(dateText);
                    todaysContent.setText(extract);
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