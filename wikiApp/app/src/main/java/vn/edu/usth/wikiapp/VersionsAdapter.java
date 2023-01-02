package vn.edu.usth.wikiapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VersionsAdapter extends RecyclerView.Adapter<VersionsAdapter.VersionVH> {

    ArrayList<Versions> versionsList;
    private Context context;


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

        public VersionVH(@NonNull View itemView) {
            super(itemView);

            codeNameTxt= itemView.findViewById(R.id.code_name);
            descriptionTxt = itemView.findViewById(R.id.desc);
            imgView = itemView.findViewById(R.id.openCloseArrow);

            linearLayout = itemView.findViewById(R.id.linear_layout);
            expandable_layout = itemView.findViewById(R.id.expandable_layout);

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
