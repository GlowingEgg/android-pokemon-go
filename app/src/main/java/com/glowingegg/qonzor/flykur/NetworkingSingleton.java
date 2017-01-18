package com.glowingegg.qonzor.flykur;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import static android.R.string.ok;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;


public class NetworkingSingleton {
    private static NetworkingSingleton networkingSingleton = null;
    private final String TAG = "NetworkingSingleton";
    final String apiKey = "5cd031185533a70eda0c8d7096540149";
    final String apiAddress = "https://api.flickr.com/services/rest/?method=flickr.photos.search";

    private NetworkingSingleton(){}

    public static NetworkingSingleton getInstance(){
        if(networkingSingleton == null){
            networkingSingleton = new NetworkingSingleton();
        }
        return networkingSingleton;
    }

    public ArrayList<Picture> getPictures(String searchKey){
        ArrayList<Picture> pictures = new ArrayList<>();
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(apiAddress + "&api_key=" + apiKey + "&text=" + searchKey + "&format=json")
                    .build();
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            String responseString = responseBody.string();
            responseString = responseString.substring(14, responseString.length() - 1);
            JSONObject jsonObject = new JSONObject(responseString);
            JSONArray jsonArray = jsonObject.getJSONObject("photos").getJSONArray("photo");
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject photo = jsonArray.optJSONObject(i);
                String id = photo.getString("id");
                String owner = photo.getString("owner");
                String secret = photo.getString("secret");
                String server = photo.getString("server");
                String farm = photo.getString("farm");
                String title = photo.getString("title");
                String isPublic = photo.getString("ispublic");
                Picture picture = new Picture(id, owner, secret, server, farm, title, isPublic);
                pictures.add(picture);
            }
        }
        catch(IOException e){
            Log.e(TAG, "fucked the call");
            return null;
        }
        catch(JSONException e){
            Log.e(TAG, "fucked the JSON");
            return null;
        }
        return pictures;
    }
}
