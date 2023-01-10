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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class VersionsAdapter extends RecyclerView.Adapter<VersionsAdapter.VersionVH> {

    ArrayList<Versions> versionsList;
    private Context context;

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    public VersionsAdapter(Context context, ArrayList<Versions> versionsList) {
        this.context = context;
        this.versionsList = versionsList;
    }

    @NonNull
    @Override
    public VersionVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
        return new VersionVH(view);
    }


    @Override
    public void onBindViewHolder(@NonNull VersionVH holder, int position) {

        Versions versions = versionsList.get(position);
        holder.codeNameTxt.setText(versions.getCodeName());
        holder.descriptionTxt.setText(versions.getDescription());
        boolean isExpandable = versionsList.get(position).isExpandable();
        holder.expandable_layout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
        holder.imgView.setImageResource(isExpandable ? R.drawable.ic_up : R.drawable.ic_down);
//        for(int i = 0 ; i < versions.getGalleryArr().size(); i++) {
//            Log.i("imgUrl", versions.getGalleryArr().get(i).getUrl());
//        }
        Log.i("runm","asfdkjaksjf");

        if(versions.getCodeName().equals("Gallery")) {
            holder.versionImageRv.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(holder.versionImageRv.getContext());
            VersionImageRvAdapter versionImageRvAdapter = new VersionImageRvAdapter(context, versions.getGalleryArr());
            holder.versionImageRv.setLayoutManager(layoutManager);
            holder.versionImageRv.setAdapter(versionImageRvAdapter);
            versionImageRvAdapter.notifyDataSetChanged();
            holder.versionImageRv.setRecycledViewPool(viewPool);
        }
        else {
            holder.versionImageRv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return versionsList.size();
    }


    public class VersionVH extends RecyclerView.ViewHolder {

        TextView codeNameTxt, descriptionTxt;
        ImageView imgView;
        LinearLayout linearLayout;
        LinearLayout expandable_layout;
        RecyclerView versionImageRv;


        public VersionVH(@NonNull View itemView) {
            super(itemView);

            codeNameTxt= itemView.findViewById(R.id.code_name);
            descriptionTxt = itemView.findViewById(R.id.desc);
            imgView = itemView.findViewById(R.id.openCloseArrow);
            linearLayout = itemView.findViewById(R.id.linear_layout);
            expandable_layout = itemView.findViewById(R.id.expandable_layout);
            versionImageRv = itemView.findViewById(R.id.galleryRVChild);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Versions versions = versionsList.get(getAbsoluteAdapterPosition());
                    versions.setExpandable(!versions.isExpandable());

                    notifyItemChanged(getAbsoluteAdapterPosition());
                }
            });
        }
    }
}
