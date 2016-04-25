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
import com.odintao.model.ObjectPlaylist;

import java.util.List;



public class FirstAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
//    private List<Movie> movieItems;
    private List<ObjectPlaylist> objectPlaylists;
//    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    ImageLoader imageLoader ;
    public FirstAdapter(Activity activity, List<ObjectPlaylist> objItems) {
        this.activity = activity;
        this.objectPlaylists = objItems;
        imageLoader = MySingleton.getInstance(activity.getBaseContext()).getImageLoader();
    }
    @Override
    public int getCount() {return objectPlaylists.size();}
    @Override
    public Object getItem(int location) {return objectPlaylists.get(location);}
    @Override
    public long getItemId(int position) {return position;}
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder gridViewImgHolder;
        if (view == null) {
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.obj_list, parent,false);
            gridViewImgHolder = new ViewHolder();
            gridViewImgHolder.textView = (TextView) view
                    .findViewById(R.id.grid_item_label);
            gridViewImgHolder.tvListGame = (TextView) view
                    .findViewById(R.id.txtListGame);
            gridViewImgHolder.tvUrl = (TextView) view
                    .findViewById(R.id.txtUrl);
            gridViewImgHolder.thumbNail = (NetworkImageView) view
                    .findViewById(R.id.grid_item_image);
            view.setTag(gridViewImgHolder);
        }else{
            gridViewImgHolder = (ViewHolder) view.getTag();
        }

        // getting movie data for the row
        ObjectPlaylist m = objectPlaylists.get(position);
        // set value into textview
        if (m != null) {

            gridViewImgHolder.textView.setText(m.getobjName());
            gridViewImgHolder.tvListGame.setText(m.getobjId());
            gridViewImgHolder.tvUrl.setText(m.getobjImgUrl());

            // thumbnail image
            gridViewImgHolder.thumbNail.setImageUrl(m.getobjImgUrl(), imageLoader);
//            thumbNail.setDefaultImageResId(R.mipmap.ic_waiting);
            gridViewImgHolder.thumbNail.setErrorImageResId(R.mipmap.ic_img_not_found);
        }
        return view;
    }

    static class ViewHolder {
        TextView textView, tvListGame, tvUrl;
        NetworkImageView thumbNail;
    }
}