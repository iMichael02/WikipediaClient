package vn.edu.usth.wikiapp;

import android.widget.ImageView;

import java.util.ArrayList;

public class Versions {

    private String codeName, description;
    private ArrayList<VersionImage> galleryArr;
    private boolean expandable;

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }


    public ArrayList<VersionImage> getGalleryArr() {
        return galleryArr;
    }

    public void setGalleryArr(ArrayList<VersionImage> galleryArr) {
        this.galleryArr = galleryArr;
    }

    public Versions(String codeName, String description, ArrayList<VersionImage> galleryArr) {
        this.codeName = codeName;
        this.description = description;
        this.expandable= false;
        this.galleryArr = galleryArr;
    }



    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Versions{" +
                "codeName='" + codeName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
