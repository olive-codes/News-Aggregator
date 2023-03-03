package com.oliviabecht.obechtnewsaggreg;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class articlesLoader implements Runnable {

    private final String urlToUse;
    private static final String API = "36351164334442678015383aced0fe97";
    private final MainActivity mainActivity;

    private RequestQueue queue;
    long start;

    public articlesLoader(MainActivity mainActivity, String urlToUse, RequestQueue queue) {
        this.mainActivity = mainActivity;
        this.urlToUse = urlToUse;
        this.queue = queue;
    }

    //@Override
    public void run() {

        start = System.currentTimeMillis();

        Response.Listener<JSONObject> listener = response -> {
            try {

                //JSONArray articlesArray = response.getJSONArray("articles");
                //ArrayList<News> articlesList = new ArrayList<>();

                ArrayList<News> newsList = new ArrayList<>();
                JSONArray jsonArray = response.getJSONArray("articles");

                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    //title, description (body), url, urlToImage, publishedAt (date)
                    String title = jsonObject.getString("title");
                    String description = jsonObject.getString("description");
                    String url = jsonObject.getString("url");
                    String urlToImage = jsonObject.getString("urlToImage");
                    String publishedAt = jsonObject.getString("publishedAt");

                    News news = new News(title, description, url, urlToImage, publishedAt);

                    System.out.println(news);

                    newsList.add(news);

                    System.out.println(newsList);
                }


                mainActivity.runOnUiThread(() ->
                        mainActivity.acceptResults(newsList));


            } catch (Exception e) {
                System.out.println(e);
            }
        };

        Response.ErrorListener error = error1 -> {
            try {
                //TODO: MAYBE CHANGE THIS NEXT LINE TO ARTICLE INSTEAD OF JSON OBJECT NAMING
                JSONObject jsonObject = new JSONObject(new String(error1.networkResponse.data));
                //setTitle("Duration: " + (System.currentTimeMillis() - start));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlToUse,
                        null, listener, error) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("User-Agent", "News-App");
                        headers.put("X-Api-Key", API);
                        return headers;
                    }

                };
        queue.add(jsonObjectRequest);


        /*
        mainActivity.runOnUiThread(() ->
                mainActivity.acceptResults(newsList));
    } catch (Exception e) {
        mainActivity.runOnUiThread(() ->
                mainActivity.acceptResults(null));
    }
         */



    }
    //outside of run
}
//last punctuation