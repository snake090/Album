package com.example.owner.album.ImageShow;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Owner on 2016/10/22.
 */

public class MyApplication extends Application {
    private final String TAG = "APPLICATION";
    private ArrayList<String> bitmaps;
    private int position;

    @Override
    public void onCreate() {
        //Application作成時
        Log.v(TAG,"--- onCreate() in ---");
    }

    @Override
    public void onTerminate() {
        //Application終了時
        Log.v(TAG,"--- onTerminate() in ---");
    }

    public ArrayList<String> getBitmaps() {
        return bitmaps;
    }

    public void setBitmaps(ArrayList<String> bitmaps) {
        this.bitmaps = bitmaps;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
