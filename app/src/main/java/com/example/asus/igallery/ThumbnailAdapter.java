package com.example.asus.igallery;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by ASUS on 2016/7/10.
 */
public class ThumbnailAdapter extends BaseAdapter {
    private List<Bitmap> imgData = null;
    private Context context = null;

    public ThumbnailAdapter(Context context, List<Bitmap> imgData) {
        this.context = context;
        this.imgData = imgData;
    }

    @Override
    public int getCount() {
        return imgData.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_thumbnail, viewGroup, false);
            holder = new ViewHolder();
            holder.imgFile = (ImageView) view.findViewById(R.id.imageView_imageFile);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.imgFile.setImageBitmap(imgData.get(i));
        return view;
    }

    static class ViewHolder {
        ImageView imgFile;
    }
}
