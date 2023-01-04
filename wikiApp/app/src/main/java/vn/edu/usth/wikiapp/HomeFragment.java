package vn.edu.usth.wikiapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
    private RecyclerView mrRecyclerView;
    private RecyclerView nRecyclerView;
    private RecyclerView oRecyclerView;

    String dayText;
    String monthText;
    String title;




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
                Intent intent = new Intent(getContext(), ArticleActivity.class);
                intent.putExtra("message_key",title);
                getContext().startActivity(intent);

            }
        });

        mrRecyclerView = view.findViewById(R.id.homeMostRead);
        mrRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        nRecyclerView = view.findViewById(R.id.homeNews);
        nRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        oRecyclerView = view.findViewById(R.id.homeOTD);
        oRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dataInit();

    }

    private JSONArray getSortedMessages(JSONArray array) throws JSONException {
        List<JSONObject> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(array.getJSONObject(i));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list.sort((a1, a2) -> {
                try {
                    return Integer.compare(a1.getInt("rank"), a2.getInt("rank"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return 0;
            });
        }
        return new JSONArray(list);
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
        String dateText = dayText+"/"+monthText+"/"+year;
        String url = "https://en.wikipedia.org/api/rest_v1/feed/featured/"+String.valueOf(year)+"/"+String.valueOf(monthText)+"/"+String.valueOf(dayText);

        ArrayList<MostReadResult> mostReadList = new ArrayList<MostReadResult>();
        ArrayList<NewsResult> newsList = new ArrayList<NewsResult>();
        ArrayList<OnThisDayResult> otdList = new ArrayList<OnThisDayResult>();

        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject tfa = jsonObject.getJSONObject("tfa");
                    title = tfa.getString("title").replaceAll("_"," ");
                    JSONObject thumbnail = tfa.getJSONObject("thumbnail");
                    String source = thumbnail.getString("source");
                    JSONObject contentObject = tfa.getJSONObject("content_urls");
                    String extract = tfa.getString("extract");
                    new ImageLoadTask(source, todaysPhoto).execute();
                    todaysTitle.setText(title);
                    todaysDate.setText(dateText);
                    todaysContent.setText(extract);

                    JSONObject mostread = jsonObject.getJSONObject("mostread");
                    JSONArray articles = mostread.getJSONArray("articles");
                    JSONArray sortedJsonArray = getSortedMessages(articles);
                    for(int i = 0 ; i < 10; i ++) {
                        JSONObject article = sortedJsonArray.getJSONObject(i);
                        String mrTitle = article.getJSONObject("titles").getString("normalized");
                        String mrDesc = article.getString("description");
                        String rank = article.getString("rank");
                        String mrThumbnail;
                        if(article.has("thumbnail")) {
                            mrThumbnail = article.getJSONObject("thumbnail").getString("source");
                        }
                        else {
                            mrThumbnail = "https://phutungnhapkhauchinhhang.com/wp-content/uploads/2020/06/default-thumbnail.jpg";
                        }
                        mostReadList.add(new MostReadResult(mrTitle,mrDesc,String.valueOf(i),mrThumbnail, rank));
                    }
                    MostReadAdapter adapter = new MostReadAdapter(getContext(),mostReadList);
                    mrRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

//
                    JSONArray news = jsonObject.getJSONArray("news");
                    for(int i = 0 ; i < news.length(); i ++) {
                        JSONArray newsArticle = news.getJSONObject(i).getJSONArray("links");
                        for(int r = 0; r < newsArticle.length(); r++) {
                            JSONObject fullContent = newsArticle.getJSONObject(r);
                            String newsTitle = fullContent.getJSONObject("titles").getString("normalized");
                            String newsDesc = fullContent.getString("extract");
                            String newsThumbnail;
                            if(fullContent.has("thumbnail")) {
                                newsThumbnail = fullContent.getJSONObject("thumbnail").getString("source");
                            }
                            else {
                                newsThumbnail = "https://phutungnhapkhauchinhhang.com/wp-content/uploads/2020/06/default-thumbnail.jpg";
                            }
                            newsList.add(new NewsResult(newsTitle,newsDesc,String.valueOf(i)+String.valueOf(r),newsThumbnail));
                        }
                    }
                    for(int i = 0; i < newsList.size(); i++) {
                        Log.i("newsList", String.valueOf(newsList.get(i).getTitle()));
                    }
                    NewsResultAdapter newsAdapter = new NewsResultAdapter(getContext(),newsList);
                    nRecyclerView.setAdapter(newsAdapter);
                    nRecyclerView.setNestedScrollingEnabled(false);
                    newsAdapter.notifyDataSetChanged();


                    JSONArray onthisday = jsonObject.getJSONArray("onthisday");
                    for(int i = 0 ; i < onthisday.length(); i ++) {
                        JSONObject otdArticle = onthisday.getJSONObject(i);
                        String title = otdArticle.getJSONArray("pages").getJSONObject(0).getString("title");
                        String year = otdArticle.getString("year");
                        String desc = otdArticle.getJSONArray("pages").getJSONObject(0).getString("extract");
                        otdList.add(new OnThisDayResult(year,desc,title));

                    }
                    OnThisDayAdapter otdAdapter = new OnThisDayAdapter(getContext(),otdList);
                    oRecyclerView.setAdapter(otdAdapter);
                    otdAdapter.notifyDataSetChanged();

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