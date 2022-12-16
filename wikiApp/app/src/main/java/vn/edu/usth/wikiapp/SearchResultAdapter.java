package vn.edu.usth.wikiapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    // creating a variable for array list and context.
    private ArrayList<SearchResult> SearchResultArrayList;

    // creating a constructor for our variables.
    public SearchResultAdapter(ArrayList<SearchResult> SearchResultArrayList, Context context) {
        this.SearchResultArrayList = SearchResultArrayList;
    }

    // method for filtering our recyclerview items.
    public void filterList(ArrayList<SearchResult> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        SearchResultArrayList = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultAdapter.ViewHolder holder, int position) {
        // setting data to our views of recycler view.
        SearchResult model = SearchResultArrayList.get(position);
        holder.searchTitleTV.setText(model.getTitle());
        holder.searchSubDescTV.setText(model.getSubDesc());
    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return SearchResultArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our views.
        private final TextView searchTitleTV;
        private final TextView searchSubDescTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our views with their ids.
            searchTitleTV = itemView.findViewById(R.id.searchTitle);
            searchSubDescTV = itemView.findViewById(R.id.searchSubDesc);
        }
    }
}
