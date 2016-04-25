package com.odintao.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.odintao.loveandmiss.R;
import com.odintao.model.ObjectPlaylist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Odin on 12/3/2015.
 */
public class CustomAlertAdapter extends BaseAdapter {

    Context ctx=null;
    ArrayList<String> listarray=null;
    ArrayList<String> listarrayId=null;
    ArrayList<String> listarrayImg=null;
    private LayoutInflater mInflater=null;
    private List<ObjectPlaylist> gamePlaylist=null;
    private List<ObjectPlaylist> allGameList = null;
    private List<ObjectPlaylist> gameSort = null;
    public CustomAlertAdapter(Activity activty, ArrayList<String> list,List<ObjectPlaylist> gamePlaylist,ArrayList<String> listId
            ,ArrayList<String> listImg,List<ObjectPlaylist> allGameList,List<ObjectPlaylist> gameSort )
    {
        this.ctx=activty;
        mInflater = activty.getLayoutInflater();
        this.listarray=list;
        this.gamePlaylist = gamePlaylist;
        this.listarrayId= listId;
        this.listarrayImg = listImg;
        this.allGameList = allGameList;
        this.gameSort = gameSort;
    }
    @Override
    public int getCount() {

        return listarray.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        final ViewHolder holder;
        if (convertView == null ) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.alertlistrow, null);

            holder.titlename = (TextView) convertView.findViewById(R.id.textView_titllename);
            holder.gameId = (TextView) convertView.findViewById(R.id.txtDialogGID);
            holder.gameImg = (TextView) convertView.findViewById(R.id.txtDialogGImg);
            holder.chk = (CheckBox) convertView.findViewById(R.id.checkBox1);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        String datavalue=listarray.get(position);
        String dataId = listarrayId.get(position);
        String dataImg = listarrayImg.get(position);

        if(gameSort != null){
            dataId = gameSort.get(position).getobjId();
            datavalue=gameSort.get(position).getobjName();
            dataImg = gameSort.get(position).getobjImgUrl();
        }
        holder.titlename.setText(datavalue);
        holder.gameId.setText(dataId);
        holder.gameImg.setText(dataImg);
        holder.chk.setChecked(false);

        //for check box
        if(gamePlaylist !=null){
            for (int i =0;i<gamePlaylist.size();i++){
                if( gamePlaylist.get(i).getobjName().contentEquals(datavalue.trim())){
                    holder.chk.setChecked(true);
                }
            }

//    	   holder.gameId.setText(gamePlaylist.get(position).toString());
        }


        return convertView;
    }

    private static class ViewHolder {
        TextView titlename;
        TextView gameId;
        TextView gameImg;
        CheckBox chk;
    }
}

