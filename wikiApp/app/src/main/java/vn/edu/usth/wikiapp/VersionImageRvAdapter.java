package vn.edu.usth.wikiapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VersionImageRvAdapter extends RecyclerView.Adapter<VersionImageRvAdapter.VersionImageRvVH> {
    ArrayList<VersionImage> urlList;
    private Context context;


    public VersionImageRvAdapter(Context context, ArrayList<VersionImage> urlList) {
        this.context = context;
        this.urlList = urlList;
    }

    @NonNull
    @Override
    public VersionImageRvAdapter.VersionImageRvVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.version_image_row, parent, false);
        return new VersionImageRvVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VersionImageRvAdapter.VersionImageRvVH holder, int position) {
        VersionImage model = urlList.get(position);
        new ImageLoadTask(model.getUrl(), holder.versionImg).execute();
        holder.versionText.setText(model.getTitle());
    }

    public class VersionImageRvVH extends RecyclerView.ViewHolder {
        ImageView versionImg;
        TextView versionText;
        public VersionImageRvVH(@NonNull View itemView) {
            super(itemView);
            versionImg = itemView.findViewById(R.id.versionImg);
            versionText = itemView.findViewById(R.id.versionText);
        }
    }

    public void clearList() {
        int size = urlList.size();
        urlList.clear();
        notifyItemRangeChanged(0, size);
    }

    @Override
    public int getItemCount() {
        return urlList.size();
    }
}
