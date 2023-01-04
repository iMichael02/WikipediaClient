package vn.edu.usth.wikiapp;

public class MostReadResult {
    private String title;
    private String id;
    private String subDesc;
    private String imageSrc;
    private String rank;

    public MostReadResult(String title, String subDesc, String id, String imageSrc, String rank) {
        this.title = title;
        this.subDesc = subDesc;
        this.id = id;
        this.imageSrc = imageSrc;
        this.rank = rank;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubDesc() {
        return subDesc;
    }

    public void setSubDesc(String subDesc) {
        this.subDesc = subDesc;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
