package vn.edu.usth.wikiapp;

public class SearchResult {
    private String title;
    private String id;
    private String subDesc;

    public SearchResult(String title, String subDesc, String id) {
        this.title = title;
        this.subDesc = subDesc;
        this.id = id;
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
}
