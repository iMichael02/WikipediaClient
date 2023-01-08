package vn.edu.usth.wikiapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

public class SearchResult implements Parcelable {
    private String title;
    private String id;
    private String subDesc;
    private String imageSrc;

    public SearchResult(String title, String subDesc, String id, String imageSrc) {
        this.title = title;
        this.subDesc = subDesc;
        this.id = id;
        this.imageSrc = imageSrc;
    }

    protected SearchResult(Parcel in) {
        title = in.readString();
        id = in.readString();
        subDesc = in.readString();
        imageSrc = in.readString();
    }

    public static final Creator<SearchResult> CREATOR = new Creator<SearchResult>() {
        @Override
        public SearchResult createFromParcel(Parcel in) {
            return new SearchResult(in);
        }

        @Override
        public SearchResult[] newArray(int size) {
            return new SearchResult[size];
        }
    };

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubDesc() {
        return subDesc;
    }

    public void setSubDesc(String subDesc) {
        this.subDesc = subDesc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(id);
        parcel.writeString(subDesc);
        parcel.writeString(imageSrc);
    }
}
