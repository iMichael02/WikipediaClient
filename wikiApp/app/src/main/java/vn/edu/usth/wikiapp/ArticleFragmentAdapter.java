package vn.edu.usth.wikiapp;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ArticleFragmentAdapter extends RecyclerView.Adapter<ArticleFragmentAdapter.RecyclerViewHolder> {

    private Context context;
    private ArrayList<SearchResult> PastSearchResultArrayList;
    private ArrayList<SearchResult> SearchResultArrayList;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String key;



    public ArticleFragmentAdapter(Context context, ArrayList<SearchResult> SearchResultArrayList, String key) {
        this.context = context;
        this.key = key;
        this.SearchResultArrayList = SearchResultArrayList;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView desc;
        TextView id;
        ImageView imageView;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.searchTitleHome);
            desc = itemView.findViewById(R.id.searchSubDescHome);
            id = itemView.findViewById(R.id.idHome);
            imageView = itemView.findViewById(R.id.searchImg);
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
        holder.desc.setText(toTitleCase(model.getSubDesc()));
        new ImageLoadTask(model.getImageSrc(), holder.imageView).execute();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = model.getTitle();
                Intent intent = new Intent(context, ArticleActivity.class);
                intent.putExtra("message_key", id);
                context.startActivity(intent);

                db = FirebaseFirestore.getInstance();
                CollectionReference dbCourses = db.collection("userPastSearches");
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser!= null) {
                    DocumentReference docRef = dbCourses.document(currentUser.getUid());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    ArrayList<String> pastList = (ArrayList<String>) document.getData().get("past");
//                                Log.i("asdfasd", (String) holder.searchView.getQuery());
                                    pastList.add(key);
                                    PastSearchInstance userData = new PastSearchInstance();
                                    userData.setUid(currentUser.getUid());
                                    userData.setEmail(currentUser.getEmail());
                                    userData.setPast(pastList);
                                    dbCourses.document(currentUser.getUid()).set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.i("upload new past","success");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.i("upload new past","failed");
                                        }
                                    });
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                }
                                else {
//                                Log.i("asdfasd", (String) holder.searchView.getQuery());
                                    ArrayList<String> pastList = new ArrayList<String>();
                                    pastList.add(key);
                                    PastSearchInstance userData = new PastSearchInstance();
                                    userData.setUid(currentUser.getUid());
                                    userData.setEmail(currentUser.getEmail());
                                    userData.setPast(pastList);
                                    dbCourses.document(currentUser.getUid()).set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.i("upload new past","success");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.i("upload new past","failed");
                                        }
                                    });
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });

                }
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
        return SearchResultArrayList.size();
    }
}
