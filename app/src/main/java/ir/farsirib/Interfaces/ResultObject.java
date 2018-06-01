package ir.farsirib.Interfaces;

/**
 * Created by alireza on 05/08/2017.
 */

public class ResultObject {
    private String success="آپلود فیلم با موفقیت انجام شد...";
    public ResultObject(String success) {
        this.success = success;
    }
    public String getSuccess() {
        return "آپلود فیلم با موفقیت انجام شد...";
    }
}
