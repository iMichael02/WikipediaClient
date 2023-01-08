package vn.edu.usth.wikiapp;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class SavedInstance {
    private ArrayList<String> favorite;
    private String uid;
    private String email;

    public ArrayList<String> getFavorite() {
        return favorite;
    }

    public void setFavorite(ArrayList<String> favorite) {
        this.favorite = favorite;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
