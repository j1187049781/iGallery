package com.example.asus.igallery;


import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import android.widget.Toast;
import android.widget.ViewSwitcher;
import java.io.File;
import java.util.List;

public class ImageShowActivity extends AppCompatActivity implements
        ViewSwitcher.ViewFactory, View.OnTouchListener {
    protected List<String> listFilesName = null;
    protected Integer index = 0;
    protected ImageSwitcher imageSwitcher_imgShow = null;

    private float touchDownX;
    private float touchUpX;
    private float touchDownY;
    private float touchUpY;

    protected boolean isPlayPicture = false;
    protected final int MSG_NEXT_PICTURE = 1;
    protected Handler handler = null;
    protected Thread threadTime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ActionBar actBar = getSupportActionBar();
        if (actBar != null) actBar.hide();
/**
 * 为幻灯片放映开一个线程来定时，写一个Handler来处理消息
 */
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == MSG_NEXT_PICTURE && isPlayPicture) {
                    index = (++index) % listFilesName.size();
                    imageSwitcher_imgShow.
                            setImageURI(Uri.fromFile(new File(listFilesName.get(index))));
                }
            }
        };
        threadTime = new Thread() {
            @Override
            public void run() {
                super.run();
                while (true) {
                    try {
                        sleep(3000);
                        handler.sendEmptyMessage(MSG_NEXT_PICTURE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        threadTime.start();

    }

    @Override
    protected void onStart() {
        super.onStart();

        isPlayPicture = false;

        listFilesName = this.getIntent().getExtras().getStringArrayList("imgFilesName");
        index = this.getIntent().getExtras().getInt("index");

        imageSwitcher_imgShow = (ImageSwitcher) findViewById(R.id.imageSwitcher_imgShow);
        imageSwitcher_imgShow.setFactory(this);
        imageSwitcher_imgShow.setInAnimation(AnimationUtils.loadAnimation(this,
               R.anim.fade_in));
        imageSwitcher_imgShow.setOutAnimation(AnimationUtils.loadAnimation(this,
                R.anim.fade_out));
        imageSwitcher_imgShow.
                setImageURI(Uri.fromFile(new File(listFilesName.get(index))));

        imageSwitcher_imgShow.setOnTouchListener(this);
    }

    @Override
    public View makeView() {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundColor(0xFFffffff);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        // 设置ImageView全屏显示,导包：import android.widget.FrameLayout.LayoutParams;
        imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        return imageView;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touchDownX = event.getX();
            touchDownY = event.getY();
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            touchUpX = event.getX();
            touchUpY = event.getY();
           //左右滑动
            if (touchUpX - touchDownX > 100) {// 从左往右,前一张
                index = index == 0 ? listFilesName.size() - 1 : index - 1;
                imageSwitcher_imgShow.
                        setImageURI(Uri.fromFile(new File(listFilesName.get(index))));
            } else if (touchDownX - touchUpX > 200) {// 从右往左，下一张
                index = index == listFilesName.size() - 1 ? 0 : index + 1;
                imageSwitcher_imgShow.
                        setImageURI(Uri.fromFile(new File(listFilesName.get(index))));
            }
            //放映开关
            if (Math.abs(touchDownY - touchUpY) >= 200) {
                isPlayPicture = !isPlayPicture;
                if (isPlayPicture) {
                    Toast.makeText(this, "放映开始", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "放映结束", Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        }
        return false;
    }


}
