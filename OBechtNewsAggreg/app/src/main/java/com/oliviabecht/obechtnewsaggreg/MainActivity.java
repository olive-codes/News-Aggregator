package com.oliviabecht.obechtnewsaggreg;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import android.net.Uri;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.oliviabecht.obechtnewsaggreg.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import androidx.viewpager2.widget.ViewPager2;


public class MainActivity extends AppCompatActivity {

//professor gave two json files with info on country and language codes

//API KEY: 36351164334442678015383aced0fe97
    //All sources: https://newsapi.org/v2/sources
    //Article sources:  https://newsapi.org/v2/top-headlines?sources=
    //For example, if you wanted “cnn” news, the URL would be:
    //https://newsapi.org/v2/top-headlines?sources=cnn

    //API
    private static final String sourcesURL = "https://newsapi.org/v2/sources";
    private static final String articlesURL = "https://newsapi.org/v2/top-headlines?sources=";
    private static final String API = "36351164334442678015383aced0fe97";

//BINDING
    private RequestQueue queue;
    private ActivityMainBinding binding;
    private long start;

    //MAIN SOURCE DATA
    public static HashMap<String, ArrayList> topicToName = new HashMap<>();
    public static HashMap<String, ArrayList> languageToName = new HashMap<>();
    public static HashMap<String, ArrayList> countryToName = new HashMap<>();

    public static HashMap<String, ArrayList> fullLanguageToName = new HashMap<>();
    public static HashMap<String, ArrayList> fulllCountryToName = new HashMap<>();

    public static HashMap<String, String > nameToID = new HashMap<>();
    public static HashMap<String, String> nameToURL = new HashMap<>();
    //JSON FILE DATA
    public static HashMap<String, String > codeToLanguage = new HashMap<>();
    public static HashMap<String, String> codeToCountry = new HashMap<>();

//DRAWER LAYOUT
    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private TextView textView;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    ArrayList<String> articleNames = new ArrayList<>();

//VIEW PAGER
    private ViewPager2 viewPager2;
    private NewsAdapter newsAdapter;
    private final ArrayList<News> newsList = new ArrayList<>();

//RIGHT HAND MENU AND FILTERS
    private Menu opt_menu;
    private String topicFilter;
    private String countryFilter;
    private String languageFilter;

    private SubMenu topicMenu;
    private SubMenu languageMenu;
    private SubMenu countryMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //VOLLEY
        queue = Volley.newRequestQueue(this);

        //GET DATA
        performSourcesDownload();
        DownloadFromRawCountry();
        DownloadFromRawLanguage();


        //LEFT DRAWER
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.left_drawer);

        //VIEW PAGER
        viewPager2 = findViewById(R.id.view_pager);

        newsAdapter = new NewsAdapter(this, newsList);
        viewPager2.setAdapter(newsAdapter);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        //new Thread(new articlesLoader(this)).start();

        //mDrawerList.setOnItemClickListener(

        //        new Thread(new articlesLoader(this)).start();

        //);

        //DRAWER LAYOUT
        mDrawerList.setOnItemClickListener(
                (parent, view, position, id) -> selectItem(position));

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }



    }
//outside of oncreate__________________________________________________________________________________________________________

    /*
    Example of filtering an array of data that is a member variable

    public void filterData() {
        String[] filteredItems  = new String[items.length];
        int counter = 0;
        for(int i = 0; i < items.length; i++) {
            if(items[i].equals("English")){
                filteredItems[counter] = items[i];
            }
        }
    }

     */



    //DOWNLOAD DATA FROM MAIN SOURCES URL _____________________________________________________________________________________
    public void performSourcesDownload() {

        Uri.Builder buildURL = Uri.parse(sourcesURL).buildUpon();

        buildURL.appendQueryParameter("apiKey", API);
        String urlToUse = buildURL.build().toString();

        start = System.currentTimeMillis();

        Response.Listener<JSONObject> listener = response -> {
            try {

                JSONArray sourcesArray = response.getJSONArray("sources");
                //articleNames = new ArrayList<>();
                for(int i = 0; i < sourcesArray.length(); i++) {
                    JSONObject source = sourcesArray.getJSONObject(i);
                    articleNames.add(source.get("name").toString());


                    //topic
                    if (topicToName.containsKey(source.get("category").toString())) {
                        ArrayList<String> values = new ArrayList<>();
                        values = topicToName.get(source.get("category").toString());

                        if (source.get("name").toString() != null) {
                            String name = source.get("name").toString();
                            values.add(name);
                            topicToName.put(source.get("category").toString(), values);
                        }
                    }
                    else {
                        String name = source.get("name").toString();
                        ArrayList <String> newValue = new ArrayList<>();
                        newValue.add(name);
                        topicToName.put(source.get("category").toString(), newValue);
                    }

                    //language
                    if (languageToName.containsKey(source.get("language").toString())) {
                        ArrayList<String> values = new ArrayList<>();
                        values = languageToName.get(source.get("language").toString());

                        if (source.get("name").toString() != null) {
                            String name = source.get("name").toString();
                            values.add(name);
                            languageToName.put(source.get("language").toString(), values);
                        }
                    }
                    else {
                        String name = source.get("name").toString();
                        ArrayList <String> newValue = new ArrayList<>();
                        newValue.add(name);
                        languageToName.put(source.get("language").toString(), newValue);
                    }

                    //country
                    if (countryToName.containsKey(source.get("country").toString())) {
                        ArrayList<String> values = new ArrayList<>();
                        values = countryToName.get(source.get("country").toString());

                        if (source.get("name").toString() != null) {
                            String name = source.get("name").toString();
                            values.add(name);
                            countryToName.put(source.get("country").toString(), values);
                        }
                    }
                    else {
                        String name = source.get("name").toString();
                        ArrayList <String> newValue = new ArrayList<>();
                        newValue.add(name);
                        countryToName.put(source.get("country").toString(), newValue);
                    }


                    //ID
                    nameToID.put(source.get("name").toString(), source.get("id").toString());

                    //URL
                    nameToURL.put(source.get("name").toString(), source.get("url").toString());


                }

                setTitle("News Gateway (" + articleNames.size() + ")");
                mDrawerList.setAdapter(new ArrayAdapter<>(this,
                        R.layout.drawer_list_item, articleNames.toArray()));

                CreateLanguageHashMap();
                CreateCountryHashMap();

            } catch (Exception e) {
            }

        };




        Response.ErrorListener error = error1 -> {
            try {
                JSONObject jsonObject = new JSONObject(new String(error1.networkResponse.data));
                setTitle("Duration: " + (System.currentTimeMillis() - start));
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



    }
    //END OF DOWNLOADING FROM MAIN SOURCES URL_____________________________________________________________________________

    //DOWNLOAD FROM ATTACHED JSON FILES____________________________________________________________________________________________
    public void DownloadFromRawCountry() {
        try {
            InputStream is = this.getResources().openRawResource(R.raw.country_codes);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            StringBuilder result = new StringBuilder();
            for (String line; (line = reader.readLine()) != null; ) {
                result.append(line);
            }

            JSONObject codesParentObj = new JSONObject(result.toString());
            JSONArray countryCodesArray =codesParentObj.getJSONArray("countries");
            for (int i = 0; i < countryCodesArray.length(); i++) {
                JSONObject jsonObject = countryCodesArray.getJSONObject(i);
                String code = jsonObject.getString("code").toLowerCase();
                String name = jsonObject.getString("name");

                codeToCountry.put(code, name);
            }

        } catch (Exception e) {
        }

        //CreateLanguageHashMap();
       // CreateCountryHashMap();

    }

    public void DownloadFromRawLanguage() {
        try {
            InputStream is = this.getResources().openRawResource(R.raw.language_codes);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            StringBuilder result = new StringBuilder();
            for (String line; (line = reader.readLine()) != null; ) {
                result.append(line);
            }

            JSONObject codesParentObj = new JSONObject(result.toString());
            JSONArray countryCodesArray =codesParentObj.getJSONArray("languages");
            for (int i = 0; i < countryCodesArray.length(); i++) {
                JSONObject jsonObject = countryCodesArray.getJSONObject(i);
                String code = jsonObject.getString("code").toLowerCase();
                String name = jsonObject.getString("name");

                codeToLanguage.put(code, name);
            }

        } catch (Exception e) {
        }

        //CreateLanguageHashMap();
        //CreateCountryHashMap();

    }
    //END OF DOWNLOAD FROM ATTACHED JSON FILES______________________________________________________________________________________


    //LEFT HAND DRAWER ___________________________________________________________________________________________________________
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    //END OF LEFT HAND DRAWER_________________________________________________________________________________________________


    //CREATE RIGHT HAND MENU HERE_______________________________________________________________________________________________
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        opt_menu = menu;
        return true;
    }
//CREATE RIGHT HAND MENU HERE___________________________________________________________________________________________________


//USER CHOOSE WITH CATEGORY FROM RIGHT HAND MENU HERE_____________________________________________________________________________
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            Log.d(TAG, "onOptionsItemSelected: mDrawerToggle " + item);
            return true;
        }

        if (item.getItemId() == R.id.menuTopics) {
            loadSubMenuTopic();
        } else if (item.getItemId() == R.id.menuCountries) {
            loadSubMenuCountry();
        } else if (item.getItemId() == R.id.menuLanguages) {
            loadSubMenuLanguage();
        } else if (item.getItemId() == R.id.menuClearAll) {
            loadClearAll();
        } else {

            //.get sub menu gets child of item not parent?
            if (item.getItemId() == R.id.topicMenu) {
                for (int i = 0; i <= topicMenu.size(); i++) {
                    if (item == topicMenu.getItem(i)) {
                        topicFilter = item.toString();
                        if (topicFilter != null) {
                            filterSources();
                            return true;
                        }
                    }
                }
            } else if (item.getItemId() == R.id.languageMenu) {
                for (int i = 0; i <= languageMenu.size(); i++) {
                    if (item == languageMenu.getItem(i)) {
                        languageFilter = item.toString();
                        if (languageFilter != null) {
                            filterSources();
                            return true;
                        }
                    }
                }

            } else if (item.getItemId() == R.id.countryMenu) {
                for (int i = 0; i <= countryMenu.size(); i++) {
                    if (item == countryMenu.getItem(i)) {
                        countryFilter = item.toString();
                        if (countryFilter != null) {
                            filterSources();
                            return true;
                        }
                    }
                }

            }


        }
        return super.onOptionsItemSelected(item);
    }
    //END OF USER CHOOSE WITH CATEGORY FROM RIGHT HAND MENU______________________________________________________________________________

    //LEFT HAND MENU_____________________________________________________________________________________________________________
    private void selectItem(int position) {

        String articleToLoad = articleNames.get(position);
        String articleID = nameToID.get(articleToLoad);
        String tempURL = articlesURL + articleID;

        Uri.Builder buildURL = Uri.parse(tempURL).buildUpon();
        buildURL.appendQueryParameter("apiKey", API);
        String urlToUse = buildURL.build().toString();

        new Thread(new articlesLoader(this, urlToUse, queue)).start();

        mDrawerLayout.closeDrawer(findViewById(R.id.c_layout));

        //update Title
        setTitle(articleToLoad);





    }
    //END OF LEFT HAND MENU______________________________________________________________________________________________________________

    //LOAD RIGHT HAND SUB MENUS ______________________________________________________________________________________________________
    private void loadSubMenuTopic() {
        topicMenu = opt_menu.getItem(0).getSubMenu();
        topicMenu.clear();
        if(topicMenu != null) {

            ArrayList<String> topics = new ArrayList<>(topicToName.keySet());
            Collections.sort(topics);

            for (String topic : topics) {
            //for (String topic : topicToName.keySet()) {
                topicMenu.add(0, R.id.topicMenu, 0, topic);
               //topicMenu.add(topic);
            }
        }


    }


    private void loadSubMenuCountry() {
        countryMenu = opt_menu.getItem(1).getSubMenu();
        countryMenu.clear();
        if(countryMenu != null) {

            ArrayList<String> countries = new ArrayList<>(codeToCountry.values());
            Collections.sort(countries);

            for (String topic : countries) {
            //for (String topic : codeToCountry.values()) {
                countryMenu.add(0, R.id.countryMenu, 0, topic);
            }
        }

    }

    private void loadSubMenuLanguage() {
        languageMenu = opt_menu.getItem(2).getSubMenu();
        languageMenu.clear();
        if(languageMenu != null) {

            ArrayList<String> languages = new ArrayList<>(codeToLanguage.values());
            Collections.sort(languages);

            for (String topic : languages) {
            //for (String topic : codeToLanguage.values()) {
                languageMenu.add(0, R.id.languageMenu, 0, topic);
            }
        }



    }
    //END OF RIGHT HAND SUB MENUS ______________________________________________________________________________________________

    //FILTER CLEAR ALL HERE______________________________________________________________________________________________________
    private void loadClearAll() {
        //TODO:SUBMENU
        topicFilter = null;
        languageFilter = null;
        countryFilter = null;
        articleNames.clear();
        performSourcesDownload();
    }
    //END OF CLEAR ALL FOR FILTERS________________________________________________________________________________________________________


    public void acceptResults(ArrayList<News> newsList) {
        if (newsList == null) {
            Toast.makeText(this, "Data loader failed", Toast.LENGTH_LONG).show();
        } else {
            this.newsList.clear();
            this.newsList.addAll(newsList);
            newsAdapter.notifyItemRangeChanged(0, newsList.size());
        }
    }


public void filterSources () {
        //use articleNames

    if (topicFilter != null) {
        for (String Key: topicToName.keySet()) {
            if (Key != topicFilter) {
                for (Object value : topicToName.get(Key)) {
                    articleNames.remove(value);
                }
                //articleNames.remove(topicToName.get(Key));
            }
        }
    }

    //TODO: FIX THIS REMOVES EVERYTHING

    if (languageFilter != null) {
        for (String Key: fullLanguageToName.keySet()) {
            if (Key != languageFilter) {
                for (Object value : fullLanguageToName.get(Key)) {
                    articleNames.remove(value);
                }
                //articleNames.remove(topicToName.get(Key));
            }
        }
    }


    System.out.println(articleNames);
    System.out.println(fulllCountryToName);

    //TODO: FIX THIS REMOVES EVERYTHING
    if (countryFilter != null) {
        //countryToName
        for (String Key: fulllCountryToName.keySet()) {
            if (Key != countryFilter) {
                for (Object value : fulllCountryToName.get(Key)) {
                    articleNames.remove(value);
                }
                //articleNames.remove(topicToName.get(Key));
            }
        }

    }


    setTitle("News Gateway (" + articleNames.size() + ")");
    mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, articleNames.toArray()));



}

public boolean CreateLanguageHashMap () {
    System.out.println(languageToName);
  for (String cCode : codeToLanguage.keySet()) {
        for (String lCode : languageToName.keySet()) {
            System.out.println(cCode);
            System.out.println(lCode);
            if (cCode.equals(lCode)) {
                fullLanguageToName.put(codeToLanguage.get(cCode), languageToName.get(lCode));
            }
        }
    }
System.out.println(fullLanguageToName);
  return true;

}

public boolean CreateCountryHashMap () {
    for (String cCode : codeToCountry.keySet()) {
        for (String lCode : countryToName.keySet()) {
            System.out.println(cCode);
            System.out.println(lCode);
            if (cCode.equals(lCode)) {
                fulllCountryToName.put(codeToCountry.get(cCode), countryToName.get(lCode));
            }
        }
    }
    return true;
}


//LAST PUNCTUATION
}