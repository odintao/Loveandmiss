package com.odintao.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.odintao.loveandmiss.R;
import com.odintao.java.AppController;
import com.odintao.model.Movie;

import java.util.List;



public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Movie> movieItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public CustomListAdapter(Activity activity, List<Movie> movieItems) {
        this.activity = activity;
        this.movieItems = movieItems;
    }
    @Override
    public int getCount() {return movieItems.size();}
    @Override
    public Object getItem(int location) {return movieItems.get(location);}
    @Override
    public long getItemId(int position) {return position;}
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.obj_list_dtl, null);
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.grid_item_image_dtl);


        // getting movie data for the row
        Movie m = movieItems.get(position);
        if (m != null) {
            // set value into textview
            TextView textView = (TextView) convertView
                    .findViewById(R.id.grid_item_label_dtl);
            // set value into textview
            TextView tvListGame = (TextView) convertView
                    .findViewById(R.id.txtListGame_dtl);
            TextView tvUrl = (TextView) convertView
                    .findViewById(R.id.txtUrl_dtl);


            tvListGame.setText(m.getObjId());
            tvUrl.setText(m.getThumbnailUrl());
            // thumbnail image
            thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
            thumbNail.setDefaultImageResId(R.mipmap.ic_waiting);
            thumbNail.setErrorImageResId(R.mipmap.ic_img_not_found);
        }
        return convertView;
    }
}