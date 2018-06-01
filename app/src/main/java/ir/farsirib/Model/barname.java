package ir.farsirib.Model;

/**
 * Created by alireza on 29/05/2017.
 */

public class barname {


    public barname(int category_id, String title, String image_url, String video_url, String description) {
        this.category_id = category_id;
        this.title = title;
        this.image_url = image_url;
        this.video_url = video_url;
        this.description = description;
    }

    public barname() {
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
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

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private int category_id;
    private String title;
    private String image_url;
    private String video_url;
    private String description;
}
