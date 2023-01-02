package vn.edu.usth.wikiapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
        holder.searchImageViewIV.setImageBitmap(getBitmapFromURL(model.getImageSrc()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Recycle Click" + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), ArticleActivity.class);
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return SearchResultArrayList.size();
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our views.
        private final TextView searchTitleTV;
        private final TextView searchSubDescTV;
        private final ImageView searchImageViewIV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our views with their ids.
            searchTitleTV = itemView.findViewById(R.id.searchTitle);
            searchSubDescTV = itemView.findViewById(R.id.searchSubDesc);
            searchImageViewIV = itemView.findViewById(R.id.searchImg);
        }
    }
}
