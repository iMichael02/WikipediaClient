package vn.edu.usth.wikiapp;

public class NewsResult {
    private String title;
    private String id;
    private String subDesc;
    private String imageSrc;

    public NewsResult(String title, String subDesc, String id, String imageSrc) {
        this.title = title;
        this.subDesc = subDesc;
        this.id = id;
        this.imageSrc = imageSrc;
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
}
