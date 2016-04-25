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
import com.odintao.java.MySingleton;
import com.odintao.model.Movie;

import java.util.List;

/**
 * Created by Odin on 1/1/2016.
 */
public class ShowListImgAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Movie> movieItems;
//    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    ImageLoader imageLoader ;
    public ShowListImgAdapter(Activity activity, List<Movie> movieItems) {
        this.activity = activity;
        this.movieItems = movieItems;
        imageLoader = MySingleton.getInstance(activity.getBaseContext()).getImageLoader();

    }
    @Override
    public int getCount() {return movieItems.size();}
    @Override
    public Object getItem(int location) {return movieItems.get(location);}
    @Override
    public long getItemId(int position) {return position;}
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder gridViewImgHolder;
        if (view == null) {
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.obj_list_dtl, parent,false);
            gridViewImgHolder = new ViewHolder();
            gridViewImgHolder.textView = (TextView) view
                    .findViewById(R.id.grid_item_label_dtl);
            gridViewImgHolder.tvListGame = (TextView) view
                    .findViewById(R.id.txtListGame_dtl);
            gridViewImgHolder.tvUrl = (TextView) view
                    .findViewById(R.id.txtUrl_dtl);
            gridViewImgHolder.thumbNail = (NetworkImageView) view
                    .findViewById(R.id.grid_item_image_dtl);
            view.setTag(gridViewImgHolder);
        }else{
            gridViewImgHolder = (ViewHolder) view.getTag();
        }

        // getting movie data for the row
        Movie m = movieItems.get(position);
        if (m != null) {
            gridViewImgHolder.tvListGame.setText(m.getObjId());
            gridViewImgHolder.tvUrl.setText(m.getThumbnailUrl());
            // thumbnail image
            gridViewImgHolder.thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
            gridViewImgHolder.thumbNail.setDefaultImageResId(R.mipmap.ic_waiting);
            gridViewImgHolder.thumbNail.setErrorImageResId(R.mipmap.ic_img_not_found);
        }
        return view;
    }

    static class ViewHolder {
        TextView textView, tvListGame, tvUrl;
        NetworkImageView thumbNail;
    }
}
