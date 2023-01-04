package vn.edu.usth.wikiapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MostReadAdapter extends RecyclerView.Adapter<MostReadAdapter.RecyclerViewHolder> {
    private Context context;
    private ArrayList<MostReadResult> OnThisDayResultArrayList;

    public MostReadAdapter(Context context, ArrayList<MostReadResult> OnThisDayResultArrayList) {
        this.context = context;
        this.OnThisDayResultArrayList = OnThisDayResultArrayList;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView desc;
        TextView id;
        ImageView imageView;
        TextView rank;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.mostReadTitle);
            desc = itemView.findViewById(R.id.mostReadDesc);
            id = itemView.findViewById(R.id.mostReadId);
            imageView = itemView.findViewById(R.id.mostReadImg);
            rank = itemView.findViewById(R.id.mostReadRanking);
        }

    }


    @NonNull
    @Override



    public MostReadAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.mostread_result, parent,false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MostReadAdapter.RecyclerViewHolder holder, int position) {
        MostReadResult model = OnThisDayResultArrayList.get(position);
        holder.title.setText(model.getTitle());
        holder.desc.setText(toTitleCase(model.getSubDesc()));
        new ImageLoadTask(model.getImageSrc(), holder.imageView).execute();
        holder.rank.setText(model.getRank());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = model.getTitle();
                Intent intent = new Intent(context, ArticleActivity.class);
                intent.putExtra("message_key", id);

                context.startActivity(intent);

            }
        });
    }

    public static String toTitleCase(String givenString) {
        String[] arr = givenString.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    @Override
    public int getItemCount() {
        return OnThisDayResultArrayList.size();
    }
}
