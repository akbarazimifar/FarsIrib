package ir.farsirib.Model;

/**
 * Created by alireza on 20/02/2017.
 */
public class program {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public program()
    {

    }

    public program(String name, String photo,int imageId,String desc) {
        this.name = name;
        this.photo = photo;
        this.imageId = imageId;
        this.desc = desc;
    }

    private String name;
    private String photo;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private int imageId;
    private String desc;

}
