package vn.edu.usth.wikiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    Button backButton;
    // creating variables for
    // our ui components.
    private RecyclerView courseRV;
    ActionBar actionBar;

    // variable for our adapter
    // class and array list
    private SearchResultAdapter adapter;
    private ArrayList<SearchResult> SearchResultArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_search);

        // initializing our variables.
        courseRV = findViewById(R.id.idRVCourses);

        // calling method to
        // build recycler view.
        buildRecyclerView();

    }



    // calling on create option menu
    // layout to inflate our menu file.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // below line is to get our inflater
        MenuInflater inflater = getMenuInflater();

        // inside inflater we are inflating our menu file.
        inflater.inflate(R.menu.search_menu, menu);

//         below line is to get our menu item.
        MenuItem searchItem = menu.findItem(R.id.actionSearch);
//
//        // getting search view of our item.
        SearchView searchView = (SearchView) searchItem.getActionView();
//
//        // below line is to call set on query text listener method.
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
        return true;
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
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist);
        }
    }

    private void buildRecyclerView() {

        // below line we are creating a new array list
        SearchResultArrayList = new ArrayList<SearchResult>();

        // below line is to add data to our array list.
        SearchResultArrayList.add(new SearchResult("DSA", "DSA Self Paced Course"));
        SearchResultArrayList.add(new SearchResult("JAVA", "JAVA Self Paced Course"));
        SearchResultArrayList.add(new SearchResult("C++", "C++ Self Paced Course"));
        SearchResultArrayList.add(new SearchResult("Python", "Python Self Paced Course"));
        SearchResultArrayList.add(new SearchResult("Fork CPP", "Fork CPP Self Paced Course"));

        // initializing our adapter class.
        adapter = new SearchResultAdapter(SearchResultArrayList, SearchActivity.this);

        // adding layout manager to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(this);


        // setting layout manager
        // to our recycler view.
        courseRV.setLayoutManager(manager);

        // setting adapter to
        // our recycler view.
        courseRV.setAdapter(adapter);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}