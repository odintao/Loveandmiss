package com.odintao.loveandmiss;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.odintao.adapter.ShowListImgAdapter;
import com.odintao.java.MySingleton;
import com.odintao.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ShowListImgActivity extends Activity {

    // Log tag
    private static final String TAG = ShowListImgActivity.class.getSimpleName();
    InterstitialAd interstitial;
    boolean isFirstTime = true;
    String objImgUrl;
    // Movies json url
//    private static final String url = "http://lovedesigner.net/feed/json";
    private static final String url = "http://www.odintao.com/android/loveandmiss/filesphp/php_love_get_list_img.php?ilist=";
    private ProgressDialog pDialog;
    private List<Movie> movieList = new ArrayList<Movie>();
    private ListView listView;
//    private CustomListAdapter adapter;
    private ShowListImgAdapter adapter;
    GridView gridView;
    String cateId="";
    String[] allobjImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (GridView) findViewById(R.id.gdVw);
//        listView = (ListView) findViewById(R.id.list);
//        adapter = new CustomListAdapter(this, movieList);
        adapter = new ShowListImgAdapter(this, movieList);
//        listView.setAdapter(adapter);
        gridView.setAdapter(adapter);
        Intent intent = getIntent();
        cateId = intent.getStringExtra("CATEID");
        System.out.println("-----------------------------------MainActi cateid:" + cateId);
        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage(getResources().getString(R.string.load_data));
        pDialog.show();
        JsonArrayRequest movieReq = new JsonArrayRequest(url+cateId,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString()); hidePDialog();
                        for (int i = 0; i < response.length(); i++) {                    // Parsing json
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Movie movie = new Movie();
                                movie.setThumbnailUrl(obj.getString("img_url"));
                                movie.setObjId(obj.getString("cate_id"));

                                movieList.add(movie);

                            } catch (JSONException e) {e.printStackTrace(); }
                        }
                        ArrayList<String> lstobjImg = new ArrayList<String>();
                        for (Movie mv : movieList) {
                            lstobjImg.add(mv.getThumbnailUrl());
                        }
                        allobjImg = lstobjImg.toArray(new String[0]);
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();
            }
        });
//        AppController.getInstance().addToRequestQueue(movieReq);
        MySingleton.getInstance(this).addToRequestQueue(movieReq);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                TextView c = (TextView) v.findViewById(R.id.txtUrl_dtl);
                objImgUrl = c.getText().toString();
                if(isFirstTime){
                    // load first time show popup
                    displayInterstitial();
                }else{
                    nextLevel();
                }

//                TextView c = (TextView) v.findViewById(R.id.txtUrl_dtl);
//                String objImgUrl = c.getText().toString();
//                Intent intent = new Intent(getApplicationContext(),
//                        ShowImgActivity.class);
//                intent.putExtra("objImgUrl", objImgUrl);
//                intent.putExtra("allobjImg", allobjImg);
//                startActivity(intent);
            }
        });

        //Todo load admob
        showAd();
        showAdPopup();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
//		case android.R.id.home: //<-- this will be your left action item
//	        // do something
//	         return true;
            case R.id.action_other_app:
                Intent vwIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Freedom+Time"));
                startActivity(vwIntent);
                return true;
            case R.id.action_share_app:
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getResources()
                        .getString(R.string.share_app_friend_detail));
                sendIntent.setType("text/plain");
                // Always use string resources for UI text.
                // This says something like "Share this photo with"
                String title = getResources().getString(
                        R.string.share_app_friend);
                // Create intent to show the chooser dialog
                Intent chooser = Intent.createChooser(sendIntent, title);
                // Verify the original intent will resolve to at least one
                // activity
                if (sendIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(chooser);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAdTest(){
        AdView adView = (AdView) findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("B695E9C704D69B46BDDB734DDA56673B").build();
        adView.loadAd(adRequest);
    }

    private void showAd() {
        if (getResources().getString(R.string.production).equalsIgnoreCase("Y")) {
            AdView adView = (AdView) findViewById(R.id.adView1);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        } else {
            showAdTest();
        }
    }

    private void showAdPopup()
    {

        // Create the interstitial.
        interstitial = new InterstitialAd(getApplicationContext());
        interstitial.setAdUnitId(getResources().getString(R.string.popup_ad_unit_id));
        // Create an ad request.
        AdRequest.Builder adRequest2 = new AdRequest.Builder();
        if (getResources().getString(R.string.production).equalsIgnoreCase("N")) {
 	/* ************  for test only ************************
 	adRequest2.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
 	****************************************************** */
            adRequest2.addTestDevice("B695E9C704D69B46BDDB734DDA56673B");
        }


        interstitial.loadAd(adRequest2.build());
        // Set an AdListener.
        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
//                closeApplication();
            }

            public void onAdLoaded() {
//				displayInterstitial();
            }

            @Override
            public void onAdClosed() {
                isFirstTime = false;
                nextLevel();
            }

        });
    }
    public void nextLevel(){

        Intent intent = new Intent(getApplicationContext(),
                ShowImgActivity.class);
        intent.putExtra("objImgUrl", objImgUrl);
        intent.putExtra("allobjImg", allobjImg);
        startActivity(intent);
    }

    public void displayInterstitial() {
        // Show the interstitial if it is ready. Otherwise, proceed to the next level
        // without ever showing it.
        if (interstitial != null) {
            if(interstitial.isLoaded()){
                interstitial.show();
            }else{
               nextLevel();
            }
        } else {
           nextLevel();

        }
    }

}