package vn.edu.usth.wikiapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class OnThisDayAdapter extends RecyclerView.Adapter<OnThisDayAdapter.RecyclerViewHolder> {
    private Context context;
    private ArrayList<OnThisDayResult> OnThisDayResultArrayList;

    public OnThisDayAdapter(Context context, ArrayList<OnThisDayResult> OnThisDayResultArrayList) {
        this.context = context;
        this.OnThisDayResultArrayList = OnThisDayResultArrayList;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView desc;
        TextView yearAgo;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.onThisDayYear);
            desc = itemView.findViewById(R.id.OnThisDayDesc);
            yearAgo = itemView.findViewById(R.id.onThisDayYearAgo);
        }

    }


    @NonNull
    @Override



    public OnThisDayAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.onthisday_result, parent,false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OnThisDayAdapter.RecyclerViewHolder holder, int position) {
        OnThisDayResult model = OnThisDayResultArrayList.get(position);
        holder.title.setText(model.getTitle());
        holder.desc.setText((model.getDesc()));
        holder.yearAgo.setText(getYearAgo(Integer.parseInt(model.getTitle())));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = model.getUrl();
                Intent intent = new Intent(context, ArticleActivity.class);
                intent.putExtra("message_key", id);

                context.startActivity(intent);

            }
        });
    }

    public static String toTitleCase(String givenString) {
        String[] arr = givenString.split(" ");
        StringBuffer sb = new StringBuffer();

        if(arr.length ==0) {
            return "";
        }
        else {
            for (int i = 0; i < arr.length; i++) {
                sb.append(Character.toUpperCase(arr[i].charAt(0)))
                        .append(arr[i].substring(1)).append(" ");
            }
            return sb.toString().trim();
        }

    }

    public static String getYearAgo(int givenYear) {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        if(year-givenYear == 1) {
            return "A year ago";
        }
        else {
            return String.valueOf(year-givenYear) + " years ago";
        }
    }

    @Override
    public int getItemCount() {
        return OnThisDayResultArrayList.size();
    }
}
