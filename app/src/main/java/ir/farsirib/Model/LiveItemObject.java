package ir.farsirib.Model;

/**
 * Created by alireza on 2017/08/09.
 */

public class LiveItemObject {

    private String name;
    private String photo;

    public LiveItemObject(String name, String photo) {
        this.name = name;
        this.photo = photo;
    }
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



}
