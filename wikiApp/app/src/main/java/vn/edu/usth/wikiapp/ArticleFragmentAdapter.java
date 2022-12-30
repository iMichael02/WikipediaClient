package vn.edu.usth.wikiapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ArticleFragmentAdapter extends RecyclerView.Adapter<ArticleFragmentAdapter.RecyclerViewHolder> {

    private Context context;
    private ArrayList<SearchResult> PastSearchResultArrayList;
    private ArrayList<SearchResult> SearchResultArrayList;
    private RecyclerView recyclerView;


    public ArticleFragmentAdapter(Context context, ArrayList<SearchResult> SearchResultArrayList) {
        this.context = context;
        this.SearchResultArrayList = SearchResultArrayList;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView desc;
        TextView id;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.searchTitleHome);
            desc = itemView.findViewById(R.id.searchSubDescHome);
            id = itemView.findViewById(R.id.idHome);
        }

    }

    public void filterList(ArrayList<SearchResult> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        SearchResultArrayList = filterlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ArticleFragmentAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.search_result_home, parent,false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleFragmentAdapter.RecyclerViewHolder holder, int position) {
        SearchResult model = SearchResultArrayList.get(position);
        holder.title.setText(model.getTitle());
        holder.desc.setText(model.getSubDesc());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = model.getId();
                Intent intent = new Intent(context, ArticleActivity.class);
                intent.putExtra("message_key", id);

                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return SearchResultArrayList.size();
    }
}
