package vn.edu.usth.wikiapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ArticleFragmentAdapter extends RecyclerView.Adapter<ArticleFragmentAdapter.RecyclerViewHolder> {

    Context context;
    ArrayList<SearchResult> SearchResultArrayList;

    public ArticleFragmentAdapter(Context context, ArrayList<SearchResult> SearchResultArrayList) {
        this.context = context;
        this.SearchResultArrayList = SearchResultArrayList;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView desc;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.searchTitleHome);
            desc = itemView.findViewById(R.id.searchSubDescHome);

        }
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

    }

    @Override
    public int getItemCount() {
        return SearchResultArrayList.size();
    }
}
