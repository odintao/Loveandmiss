package com.odintao.loveandmiss;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.toolbox.ImageLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.odintao.java.MySingleton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Odin on 11/28/2015.
 */
public class ShowImgActivity extends Activity {

    ViewPager mViewPager;
    String imgUrl;
    String[] allImgUrl;
    int isPosition;
    boolean isFirstTime;
    Button btShare;
    ImageView imageView;


    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_img_show);
        isFirstTime = true;
////        String imgUrl = String.valueOf(savedInstanceState.getInt("","0"));
        Intent intent = getIntent();
        imgUrl = intent.getStringExtra("objImgUrl");
        allImgUrl = intent.getStringArrayExtra("allobjImg");
//        isPosition = allImgUrl.indexOf(imgUrl);
        isPosition = 0;
        for (int i=0;i<allImgUrl.length;i++) {
            if (allImgUrl[i].equals(imgUrl)) {
                isPosition = i;
                break;
            }
        }
        System.out.println("imgUrl:"+imgUrl);
        System.out.println("is imgUrl postion:"+isPosition);
//        NetworkImageView imgVolley = (NetworkImageView) this
//                .findViewById(R.id.imgVolley);
//        imgVolley.setImageUrl(imgUrl, MyVolley.getImageLoader());

        imageView = (ImageView) findViewById(R.id.imageViewHide);
//        imageView = (ImageView) findViewById(R.id.imageView);
//        imageView.setImageResource(R.mipmap.ic_waiting);
//        Picasso.with(getApplicationContext()).load(allImgUrl[isPosition]).into(imageView);
        CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(this);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPagerAdapter);
        this.mViewPager.setCurrentItem(this.isPosition);
        // เพิ่มลูกเล่น viewpager
        mViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            public void transformPage(View view, float position) {
                if (position > 1 || position < -1) {
                    view.setAlpha(0);
                } else {
                    float alpha = 1 - (float) (Math.abs(position));
                    view.setAlpha(alpha);
                }
            }
        });

        try {
            deletefile();
        } catch (IOException e) {
            System.out.println("delete file :" + e.getMessage());
        }
        btShare = (Button) findViewById(R.id.btShare);
        btShare.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_action_share, 0, 0, 0);
        btShare.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "url:" + allImgUrl[mViewPager.getCurrentItem()], Toast.LENGTH_LONG);
//                File f =  new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)).getPath();
                ImageLoader imageLoader = MySingleton.getInstance(getBaseContext()).getImageLoader();
                imageLoader.get(allImgUrl[mViewPager.getCurrentItem()]
                        ,ImageLoader.getImageListener(imageView,R.mipmap.ic_waiting,R.mipmap.ic_img_not_found));
//                Picasso.with(getApplicationContext()).load(allImgUrl[mViewPager.getCurrentItem()]).into(imageView);
                onShareItem(imageView);
            }
        });

       //TODO load admob
        showAd();
    }

    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
//            return mResources.length;
            return allImgUrl.length;
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
            System.out.println("pos:" + position);
            ImageView imageView1 = (ImageView) itemView.findViewById(R.id.imageView);

            ImageLoader imageLoader = MySingleton.getInstance(getBaseContext()).getImageLoader();
            imageLoader.get(allImgUrl[position]
                    ,ImageLoader.getImageListener(imageView1,R.mipmap.ic_waiting,R.mipmap.ic_img_not_found));
//            Picasso.with(getApplicationContext()).load(allImgUrl[position]).error(R.mipmap.ic_img_not_found).placeholder(R.mipmap.ic_waiting).into(imageView1);
//
            container.addView(itemView);
            System.out.println("mViewPager.getCurrentItem():" + mViewPager.getCurrentItem());
            System.out.println("****************** allImgUrl[position] :" + allImgUrl[position]);


            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
    private int gotoPosition(int p){
        int result = p;
        if(isFirstTime) {
            result = isPosition;
            isFirstTime = false;
            System.out.println("isFirstTime is pos result:"+result);
        }
        return  result;
    }

    // Can be triggered by a view event such as a button press
    public void onShareItem(ImageView imgv) {
        // Get access to bitmap image from view
//       final NetworkImageView ivImage = (NetworkImageView) v.findViewById(R.id.imageView);
        // Get access to the URI for the bitmap

        Uri bmpUri = getLocalBitmapUri(imgv);

        if (bmpUri != null) {
            // Construct a ShareIntent with link to image
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.setType("image/*");
            // Launch sharing dialog for image
            startActivity(Intent.createChooser(shareIntent,getResources().getString(R.string.title_share_program)));
        } else {
            // ...sharing failed, handle error
        }

        System.out.println("url:" + allImgUrl[mViewPager.getCurrentItem()]);
    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
//            File file =  new File(Environment.getExternalStoragePublicDirectory(
//                    Environment.DIRECTORY_DOWNLOADS), "congate_share_image_" + System.currentTimeMillis() + ".png");
            File folder = Environment.getExternalStorageDirectory();
            folder = new File(folder.getAbsolutePath() + "/"+getString(R.string.share_img_folder)+"/");
            folder.mkdirs();

           File file = new File(folder,"congate_share_image_" + System.currentTimeMillis() + ".png");


            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    void deletefile() throws IOException {
//        File f =  new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
//        if (f.isDirectory()) {
//            for (File c : f.listFiles()) {
//                delete(c);
//            }
//        } else


//        if (f.getAbsolutePath().contains("congate_share_image_")) {
//            if (!f.delete()) {
//                new FileNotFoundException("Failed to delete file: " + f);
//            }
//        }

        String sdcard = Environment.getExternalStorageDirectory() +   "/"+getString(R.string.share_img_folder)+"/";
        // go to your directory
        File fileList = new File( sdcard );

        //check if dir is not null
        if (fileList != null){

            // so we can list all files
            File[] filenames = fileList.listFiles();

            // loop through each file and delete
            if (filenames != null) {
                for (File tmpf : filenames) {
                    tmpf.delete();
                }
            }
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

}
