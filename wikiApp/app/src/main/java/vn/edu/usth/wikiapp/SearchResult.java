package vn.edu.usth.wikiapp;

public class SearchResult {
    private String title;
    private String subDesc;

    public SearchResult(String title, String subDesc) {
        this.title = title;
        this.subDesc = subDesc;
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
}
