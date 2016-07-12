package com.example.asus.igallery;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.asus.utill.FileUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    protected List<String> imgFilesName = null;
    protected GridView gridView_container = null;
    protected List<Bitmap> listBitmap = null;
    final protected int SUCCESS_IMG_LOAD = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView_container = (GridView) findViewById(R.id.gridView_container);
        /**
         * 完成数据与适配器的绑定
         */
        imgFilesName = new ArrayList<String>();
        imgFilesName = FileUtil.getAllPngJpgFiles("iGallery");
        listBitmap = new ArrayList<Bitmap>();
        final ThumbnailAdapter thumbnailAdapter = new ThumbnailAdapter(this, listBitmap);
        gridView_container.setAdapter(thumbnailAdapter);
        /**
         * 处理一张图片加载完成的消息
         */
        final Handler handlerImg = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == SUCCESS_IMG_LOAD) {
                    thumbnailAdapter.notifyDataSetChanged();
                }
            }
        };
        /**
         * 在另一个线程中加载文件
         */
        new Thread() {
            @Override
            public void run() {
                super.run();
                for (String imgFilePath : imgFilesName) {
                    Bitmap rawBitmap = BitmapFactory.decodeFile(imgFilePath, null);
                    listBitmap.add(FileUtil.small((rawBitmap), 300, 300));
                    handlerImg.sendEmptyMessage(SUCCESS_IMG_LOAD);
                }
            }
        }.start();

        gridView_container.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle=new Bundle();
                bundle.putStringArrayList("imgFilesName",(ArrayList<String>) imgFilesName);
                bundle.putInt("index",i);
                Intent intent=new Intent(MainActivity.this,ImageShowActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


    }
}
