package com.example.hp.rxjavatext;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hp.rxjavatext.model.Image;

import java.util.List;

/**
 * Created by hp on 2016/3/30.
 */
public class MyAdapter extends BaseAdapter {

    private List<String> list;
    private Context mContext;
    List<Image> images;

    public MyAdapter(List<Image> images, List<String> list, Context context) {
       this.list=list;
        this.mContext=context;
        this.images=images;
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder=null;
        if(convertView==null){

            holder=new ViewHolder();
            convertView=View.inflate(mContext,R.layout.simplelayout,null);
            holder.textView=(TextView)convertView.findViewById(R.id.item_textview);
            holder.imageView=(ImageView)convertView.findViewById(R.id.item_image);
            convertView.setTag(holder);

        }else {

            holder=(ViewHolder)convertView.getTag();
        }


        holder.textView.setText(list.get(position));
        Glide.with(mContext).load(images.get(position).getUrl()).into(holder.imageView);
        return convertView;
    }

    class  ViewHolder{


        TextView textView;
        ImageView imageView;
    }
}
