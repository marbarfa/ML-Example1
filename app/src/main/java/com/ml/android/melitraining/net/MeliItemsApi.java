package com.ml.android.melitraining.net;

import android.util.Log;
import com.ml.android.melitraining.dto.ItemDTO;
import com.ml.android.melitraining.dto.PictureDTO;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * Created by marbarfa on 4/27/14.
 */
public class MeliItemsApi {


    private static final String  TAG  = "HTTPCLientHelper";
    private static final String meliSearchURL = "https://api.mercadolibre.com/items/";

    public static ItemDTO getItem(String itemId) {
        ItemDTO itemDTO = new ItemDTO();

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet();

        // Execute the request
        HttpResponse response;
        try {
            httpget.setURI(new URI(meliSearchURL+itemId));
            response = httpclient.execute(httpget);
            // Examine the response status
            Log.i("REQUEST RESPONSE", response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                // A Simple JSON Response Read
                String resultStr = EntityUtils.toString(entity);
                JSONObject json = new JSONObject(resultStr);
                itemDTO.itemId = json.getString("id");
                itemDTO.siteId = json.getString("site_id");
                itemDTO.title = json.getString("title");
                itemDTO.subtitle = json.getString("subtitle");
                itemDTO.categoryId = json.getString("category_id");
                itemDTO.price = json.getDouble("price");
                itemDTO.startTime =  sdf.parse(json.getString("start_time"));
                itemDTO.endtime =  sdf.parse(json.getString("stop_time"));
                itemDTO.thumbnail = json.getString("thumbnail");
                itemDTO.status = json.getString("status");
                itemDTO.soldQuantity = json.getString("sold_quantity");
                itemDTO.quantity = json.getString("available_quantity");
                itemDTO.condition = json.getString("condition");

                itemDTO.pictures = new ArrayList<PictureDTO>();
                JSONArray arrayOfPictures = json.getJSONArray("pictures");
                for(int i=0; i<arrayOfPictures.length(); i++){
                    PictureDTO picture = new PictureDTO();
                    picture.pictureId = arrayOfPictures.getJSONObject(i).getString("id");
                    picture.url = arrayOfPictures.getJSONObject(i).getString("url");
                    itemDTO.pictures.add(picture);
                }

            }
        } catch (ClientProtocolException e) {
            Log.e(TAG, "Error in protocol request");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "IO Exception");
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing json");
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (Throwable t){
            Log.e(TAG, "Unknown error");
            t.printStackTrace();
        }
        return itemDTO;
    }


}
