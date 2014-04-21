package com.ml.android.myapplication3.common;

import android.util.Log;
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
import java.util.ArrayList;
import java.util.List;

public class HttpClientHelper {
    private static final String  TAG  = "HTTPCLientHelper";
    private static final String meliSearchURL = "https://api.mercadolibre.com/sites/MLA/search?q=";

  public static List<SearchResultRow> getProductSearch(String searchString) {
      List<SearchResultRow> rows = new ArrayList<SearchResultRow>();

      HttpClient httpclient = new DefaultHttpClient();

      // Prepare a request object
      //TODO: Hardcoded limit!. Missing an empty string sanity check...
      HttpGet httpget = new HttpGet();

      // Execute the request
      HttpResponse response;
      try {
          httpget.setURI(new URI(meliSearchURL+searchString+"&limit=100"));
          response = httpclient.execute(httpget);
          // Examine the response status
          Log.i("REQUEST RESPONSE", response.getStatusLine().toString());

          // Get hold of the response entity
          HttpEntity entity = response.getEntity();
          // If the response does not enclose an entity, there is no need
          // to worry about connection release

          if (entity != null) {
              // A Simple JSON Response Read
              String resultStr = EntityUtils.toString(entity);
              JSONObject json = new JSONObject(resultStr);
              if (Integer.parseInt(((JSONObject)json.get("paging")).get("total").toString()) > 0 ){
                  //Iterate over results.parse results if there are any..
                  JSONArray arrayOfResults = (JSONArray)json.get("results");
                  for(int i=0; i< arrayOfResults.length(); i++){
                      SearchResultRow row = new SearchResultRow();
                      row.productTitle = (String)arrayOfResults.getJSONObject(i).get("title");
                      if (row.productTitle.length() > 32){
                          row.productTitle = row.productTitle.subSequence(0, 32) + "...";
                      }
                      row.productPrice = Double.parseDouble(arrayOfResults.getJSONObject(i).get("price").toString());
                      rows.add(row);
                  }
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
      return rows;
  }


}
