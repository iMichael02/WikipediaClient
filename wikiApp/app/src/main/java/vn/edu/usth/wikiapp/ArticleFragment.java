package vn.edu.usth.wikiapp;

import android.content.Context;
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
import android.widget.Toast;

import java.util.ArrayList;

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

    private RecyclerView recyclerView;
    private ArticleFragmentAdapter articleFragmentAdapter;
    private ArrayList<SearchResult> SearchResultArrayList;
    SearchView searchView;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


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
        dataInit();



        recyclerView = view.findViewById(R.id.recyclerViewHome);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // initializing our adapter class.
        ArticleFragmentAdapter articleFragmentAdapter = new ArticleFragmentAdapter(getContext(),SearchResultArrayList);

        // adding layout manager to our recycler view.
        // setting adapter to
        // our recycler view.
        recyclerView.setAdapter(articleFragmentAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerView.OnItemTouchListener() {
                    @Override
                    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                        return false;
                    }

                    @Override
                    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

                    }

                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                    }
                }
        );

        articleFragmentAdapter.notifyDataSetChanged();

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
                filter(newText, articleFragmentAdapter);
                return false;
            }
        });
    }

    private void dataInit() {

        // below line we are creating a new array list
        SearchResultArrayList = new ArrayList<SearchResult>();

        // below line is to add data to our array list.
        SearchResultArrayList.add(new SearchResult("DSA", "DSA Self Paced Course", "1"));
        SearchResultArrayList.add(new SearchResult("JAVA", "JAVA Self Paced Course", "2"));
        SearchResultArrayList.add(new SearchResult("C++", "C++ Self Paced Course", "3"));
        SearchResultArrayList.add(new SearchResult("Python", "Python Self Paced Course", "4"));
        SearchResultArrayList.add(new SearchResult("Fork CPP", "Fork CPP Self Paced Course", "5"));
        SearchResultArrayList.add(new SearchResult("SDFSDFDFSSA", "DSA Self Paced Course", "6"));
        SearchResultArrayList.add(new SearchResult("JAVA", "JAVA Self Paced Course", "7"));
        SearchResultArrayList.add(new SearchResult("C++", "C++ Self Paced Course", "8"));
        SearchResultArrayList.add(new SearchResult("Python", "Python Self Paced Course", "9"));
        SearchResultArrayList.add(new SearchResult("Fork CPP", "Fork CPP Self Paced Course", "10"));
        SearchResultArrayList.add(new SearchResult("DGAFDGSA", "DSA Self Paced Course", "11"));
        SearchResultArrayList.add(new SearchResult("JAVA", "JAVA Self Paced Course", "12"));
        SearchResultArrayList.add(new SearchResult("C++", "C++ Self Paced Course", "13"));
        SearchResultArrayList.add(new SearchResult("Python", "Python Self Paced Course", "14"));
        SearchResultArrayList.add(new SearchResult("Fork CPP", "Fork CPP Self Paced Course", "15"));
        SearchResultArrayList.add(new SearchResult("DSAASDFASDF", "DSA Self Paced Course", "16"));
        SearchResultArrayList.add(new SearchResult("JAVA", "JAVA Self Paced Course", "17"));
        SearchResultArrayList.add(new SearchResult("C++", "C++ Self Paced Course", "18"));
        SearchResultArrayList.add(new SearchResult("Python", "Python Self Paced Course", "19"));
        SearchResultArrayList.add(new SearchResult("Fork CPP", "Fork CPP Self Paced Course", "20"));
        SearchResultArrayList.add(new SearchResult("DSA", "DSA Self Paced Course", "21"));
        SearchResultArrayList.add(new SearchResult("JAVA", "JAVA Self Paced Course", "23"));
        SearchResultArrayList.add(new SearchResult("C++", "C++ Self Paced Course", "24"));
        SearchResultArrayList.add(new SearchResult("Python", "Python Self Paced Course", "25"));
        SearchResultArrayList.add(new SearchResult("Fork CPP", "Fork CPP Self Paced Course", "26"));

    }

}