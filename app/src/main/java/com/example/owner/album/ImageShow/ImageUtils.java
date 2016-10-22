package com.example.owner.album.ImageShow;

/**
 * Created by Owner on 2016/10/22.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class ImageUtils {

    private static final String TAG = "ImageUtils";

    private int dp_w;
    private int dp_h;
    private int drow_h;
    private int drow_s;

    public  Bitmap resizeBitmapToDisplaySize(Activity activity, Bitmap src){
        WindowManager wm = (WindowManager)activity.getSystemService(Context.WINDOW_SERVICE);
        // Displayインスタンス生成
        Display dp = wm.getDefaultDisplay();
        // ディスプレイサイズ取得
        dp_w = dp.getWidth();
        dp_h = dp.getHeight();
        // リサイズ画像の高さ
        drow_h = (dp_w / 2) * 3;
        // 描画始点の高さ
        drow_s = (dp_h - drow_h) / 2;

        src = Bitmap.createScaledBitmap(src, dp_w, drow_h , true);
        return src;
    }
}
