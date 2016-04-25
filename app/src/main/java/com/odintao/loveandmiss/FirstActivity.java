package com.odintao.loveandmiss;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.odintao.adapter.FirstAdapter;
import com.odintao.java.MySingleton;
import com.odintao.model.ObjectPlaylist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Odin on 12/3/2015.
 */
public class FirstActivity extends  Activity {

    SharedPreferences sharePref;
    SharedPreferences.Editor editor;
    GridView gridView;
    String jsonString;
    private ArrayList<ObjectPlaylist> ObjectPlaylist;// เกมส์ทั้งหมดที่เคยอยู่ใน share pref
    private ArrayList<ObjectPlaylist> allObjectPlaylist = new ArrayList<ObjectPlaylist>();;
    private ArrayList<ObjectPlaylist> newObjectPlaylist;
    ObjectPlaylist[] spinGame;
    ObjectPlaylist gpl;
    ObjectPlaylist spinnerGpl; // for spinner
    ProgressDialog mProgressDialog;
    private static final String GETDATAURL = "http://www.odintao.com/android/loveandmiss/filesphp/php_love_get_menu.php";

    String[] gameName;
    String[] allGameName;
    String[] allGameId;
    String[] allGameImg;
    Boolean[] allGameSelectedShef;
    boolean[] allGamePickShef; // game ที่เคยถูกเลือกแล้ว เพื่อใช้ตอน show dialog ตอนแรก
    ArrayList<String> lstGameNmInPref ;
    String[] allGameNmInPref; // เกมส์ทั้งหมดที่เคยอยู่ใน share pref
    // Spinner spinner;
    Button imgBtn;
    private Boolean bFirstTime = false;
    int textlength = 0;
    private AlertDialog myalertDialog = null;
    InterstitialAd interstitial;
    ProgressDialog pDialog;
    private FirstAdapter adapter;
    // Log tag
    private static final String TAG = FirstActivity.class.getSimpleName();

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // spinner = (Spinner) findViewById(R.id.spinner1);
        ObjectPlaylist = new ArrayList<ObjectPlaylist>();
        newObjectPlaylist= new ArrayList<ObjectPlaylist>();
        gridView = (GridView) findViewById(R.id.gridView1);
        adapter = new FirstAdapter(this, allObjectPlaylist);
        gridView.setAdapter(adapter);
//        new RemoteDataTask().execute(allObjectPlaylist);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage(getResources().getString(R.string.load_data));
        pDialog.show();
        JsonArrayRequest movieReq = new JsonArrayRequest(GETDATAURL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString()); hidePDialog();
//                        allObjectPlaylist = new ArrayList<ObjectPlaylist>();
                        for (int i = 0; i < response.length(); i++) {                    // Parsing json
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                gpl = new ObjectPlaylist();
                                String gameNm = obj.getString("love_name");
                                String gameImgUrl = obj.getString("love_img_url");
                                String gameId = obj.getString("love_id");
                                gpl.setobjId(gameId);
                                gpl.setobjImgUrl(gameImgUrl);
                                gpl.setobjName(gameNm);
                                // ObjectPlaylist.add(gpl);
                                allObjectPlaylist.add(gpl);

                            } catch (JSONException e) {e.printStackTrace(); }
                        }
//                        adapter = new FirstAdapter(FirstActivity.this, allObjectPlaylist);
                        adapter.notifyDataSetChanged();
//                        gridView.setAdapter(adapter);
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

                TextView c = (TextView) v.findViewById(R.id.txtListGame);
                String cateId = c.getText().toString();
                Intent intent;
                intent = new Intent(getApplicationContext(),
                        ShowListImgActivity.class);
                intent.putExtra("CATEID", cateId);
                System.out.println("----------------------------------- FirstAct cateid:" + cateId);
                // intent.putExtra("TOTALRESULTS", 22);
                startActivity(intent);

            }
        });
        // check network
        if (!isOnline()) {
//            btnFavorite.setVisibility(View.GONE);
        }
        //TODO load admob
        showAd();
        showAdPopup();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        // MenuInflater inflater = getMenuInflater();
        // inflater.inflate(R.menu.main, menu);
//        return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    private void showToast(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast.show();
    }



    private boolean isOnline() {
        boolean bResult = false;
        try {
            ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo m3g = connManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            boolean bWifi = false;
            boolean b3g = false;
            if (mWifi != null) {
                bWifi = mWifi.isConnected();
            }
            if (m3g != null) {
                b3g = m3g.isConnected();
            }

            if (bWifi || b3g) {
                // Do whatever
                // showToast("Network Connected");
                bResult = true;
            } else {
                // showToast("Please make sure your Network Connection is ON");
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        FirstActivity.this);

                builder.setTitle(R.string.network_title);
                builder.setMessage(R.string.network_not_con);
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // showToast("Oh yeah!");
                            }
                        });

                builder.show();
            }
        } catch (Exception e) {
            showToast("Error" + e.getMessage());
        }
        return bResult;
    }



    private void showAdTest(){
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("B695E9C704D69B46BDDB734DDA56673B").build();
        adView.loadAd(adRequest);
    }

    private void showAd() {
        if (getResources().getString(R.string.production).equalsIgnoreCase("Y")) {
            AdView adView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        } else {
			showAdTest();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.app_name);

        dialog.setCancelable(true);
        dialog.setMessage(R.string.action_quite);
        dialog.setPositiveButton(R.string.bt_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//      showAdPopup();
//        ****** del of policy
//        displayInterstitial();

            }

        });

        dialog.setNegativeButton(R.string.bt_no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.setNeutralButton(R.string.btn_rate,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
//							dialog.cancel();
                        Intent vwIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.share_url_app)));
                        startActivity(vwIntent);
//							finish();
                    }
                });

        dialog.show();

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
                closeApplication();
            }

        });
    }

    public  void closeApplication(){
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
//        startActivity(intent);
        System.out.println("--------------- Quit------------------");
        finish();
        System.exit(0);
    }

    public void displayInterstitial() {
        // Show the interstitial if it is ready. Otherwise, proceed to the next
        // level
        // without ever showing it.
        if (interstitial != null) {
            if(interstitial.isLoaded()){
                interstitial.show();
            }else{
               closeApplication();
            }
        } else {
            closeApplication();

        }
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
    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}
