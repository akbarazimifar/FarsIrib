package ir.farsirib.Interfaces;

/**
 * Created by alireza on 05/08/2017.
 */

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
public interface VideoInterface {
    @Multipart
//    @POST("https://farsirib.cloud/Farsirib/webservice/videos/fileUpload.php")
    @POST("http://shahreraz.com/Farsirib/webservice/videos/fileUpload.php")
    //@POST("http://www.mob.shahreraz.com/club/app/videos/fileUpload.php")
    Call<ResultObject> uploadVideoToServer(@Part MultipartBody.Part video);
}