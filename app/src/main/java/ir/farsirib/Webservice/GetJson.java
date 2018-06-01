package ir.farsirib.Webservice;

import android.os.Build;
import android.os.StrictMode;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by alireza on 27/09/2016.
 */
public class GetJson {

    String result = "";


    public GetJson() {


    }


    public String JsonRequest(String url) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);
        }


        HttpClient httpClient = new DefaultHttpClient();

        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept", "application/josn");
        httpGet.setHeader("Content-type", "application/josn");

        HttpResponse httpResponse;

        try {
            httpResponse = httpClient.execute(httpGet);
            httpResponse.setHeader("Content-Type", "UTF-8");
            HttpEntity httpEntity = httpResponse.getEntity();

            if (httpEntity != null) {

                InputStream inputStream = httpEntity.getContent();

                result = ConvertStreamToText(inputStream);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


        return result;
    }


    public static String ConvertStreamToText(InputStream inputStream) throws UnsupportedEncodingException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        StringBuilder stringBuilder = new StringBuilder();


        String line = "";

        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return stringBuilder.toString();
    }


}
