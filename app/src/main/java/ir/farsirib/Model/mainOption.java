package ir.farsirib.Model;

/**
 * Created by alireza on 29/05/2017.
 */

public class mainOption {


    public mainOption(String title, String image_url,int page_index) {
        this.title = title;
        this.image_url = image_url;
        this.page_index = page_index;
    }

    public mainOption() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    private String title;
    private String image_url;
    private int page_index;

    public int getPage_index() {
        return page_index;
    }

    public void setPage_index(int page_index) {
        this.page_index = page_index;
    }


}
