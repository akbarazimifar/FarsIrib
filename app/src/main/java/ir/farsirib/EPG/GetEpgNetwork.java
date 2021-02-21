package ir.farsirib.EPG;

/**
 * Created by alireza on 10/07/2017.
 */
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.*;
import java.lang.reflect.Type;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.*;


import ir.farsirib.Webservice.GetJson;


public class GetEpgNetwork {

    Gson json;

    public InfoEpg[] GetEpgNetwork(TKOrder TKorder) {

        InfoEpg[] ListEpg = null;
        String ser = serializeToJson(TKorder);
//        json.wri
//
//                JsonSerializer serializer = new JsonSerializer() {
//                    @Override
//                    public JsonElement serialize(Object src, Type typeOfSrc, JsonSerializationContext context) {
//                        return null;
//                    }
//                };
//
//                serializer.
//        JSONSerializer serializer = new JSONSerializer();
//        return serializer.serialize( p );
//
//        MemoryStream mem = new MemoryStream();

        return  ListEpg;
    }

    public String serializeToJson(TKOrder myClass) {
        Gson gson = new Gson();
        String j = gson.toJson(myClass);
        return j;
    }
}
