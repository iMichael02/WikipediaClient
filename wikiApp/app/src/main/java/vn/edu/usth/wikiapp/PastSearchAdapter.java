package vn.edu.usth.wikiapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PastSearchAdapter extends RecyclerView.Adapter<PastSearchAdapter.PastSearchVH> {
    ArrayList<String> searchedList;

    private Context context;
    private SearchView searchView;

    public PastSearchAdapter(ArrayList<String> searchedList, Context context, SearchView searchView) {
        this.searchedList = searchedList;
        this.context = context;
        this.searchView = searchView;
    }

    @NonNull
    @Override
    public PastSearchVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.past_search,parent,false);
        return new PastSearchVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PastSearchVH holder, int position) {
        String pastSearch = searchedList.get(position);
        holder.pastSearch.setText(pastSearch);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setQuery(pastSearch, true);
            }
        });
    }


    public class PastSearchVH extends RecyclerView.ViewHolder {
        TextView pastSearch;

        public PastSearchVH(@NonNull View itemView) {
            super(itemView);
            pastSearch = itemView.findViewById(R.id.textPastSearch);
        }
    }

    public int getItemCount() {
        return searchedList.size();
    }

}
