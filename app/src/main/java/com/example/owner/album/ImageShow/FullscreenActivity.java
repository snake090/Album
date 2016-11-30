package com.example.owner.album.ImageShow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.owner.album.Album.AlbumList_Activity;
import com.example.owner.album.Component.Orientation;
import com.example.owner.album.Map.AlbumMapsActivity;
import com.example.owner.album.R;
import com.example.owner.album.query.Picture_Query;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    @BindView(R.id.imageView2)
    ImageView imageView2;
    private View mContentView;
    private MyApplication application;
    private ArrayList<String> bitmaps;
    private int position;
    float lastTouchX = 0;
    // float lastTouchY = 0;
    float currentX = 0;
    //float currentY = 0;
    private Button button;

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements

            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);
        ButterKnife.bind(this);
        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        button = (Button) findViewById(R.id.dummy_button);
        application = (MyApplication) this.getApplication();
        bitmaps = application.getBitmaps();
        position = application.getPosition();
        Image_show();

        View view = findViewById(R.id.imageView2);
        view.setOnTouchListener(new FlickTouchListener());


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

        button.setOnClickListener(v -> {

            String Bitmap_path=bitmaps.get(position);
            Intent intent = new Intent(FullscreenActivity.this, AlbumMapsActivity.class);
            int id=new Picture_Query().Get_ID(Bitmap_path);
            intent.putExtra("id", id);
            intent.putExtra("kind",1);
            startActivity(intent);

        });

    }

    private class FlickTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN://画面に触れたら
                    lastTouchX = event.getX();

                    // lastTouchY = event.getY();
                    break;

                case MotionEvent.ACTION_UP://画面を離したら
                    currentX = event.getX();
                    // currentY = event.getY();
                    /*
                    AlertDialog.Builder alertDlgBld = new AlertDialog.Builder(FullscreenActivity.this);
                    alertDlgBld.setMessage("(" + lastTouchX +")から(" + currentX +  ")へフリックした");

                    alertDlgBld.setPositiveButton(
                            "",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    alertDlgBld.show();
                    */
                    float distance = currentX - lastTouchX;

                    //右
                    if ((distance > 0) && (Math.abs(distance) > 100)) {
                        if (position - 1 >= 0) {
                            position--;
                            Image_show();
                        }
                        //左
                    } else if ((distance < 0) && (Math.abs(distance)) > 100) {
                        if (position + 1 < bitmaps.size()) {
                            position++;
                            Image_show();
                        }

                    }
                    break;
            }
            return true;
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first

        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_PORTRAIT:  // 縦長
                Image_show();
                // 処理
                break;
            case Configuration.ORIENTATION_LANDSCAPE:  // 横長
                Image_show();
                // 処理
                break;
            default:
                break;
        }
        super.onConfigurationChanged(newConfig);

    }

    private void Image_show() {
        ImageUtils imageUtils = new ImageUtils();
        Orientation orientation = new Orientation();
        Bitmap bitmap = BitmapFactory.decodeFile(bitmaps.get(position));
        ExifInterface exifInterface = null;
        Orientation orientation_class = new Orientation();
        try {
            exifInterface = new ExifInterface(bitmaps.get(position));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation1 = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        imageView2.setImageBitmap(imageUtils.resizeBitmapToDisplaySize(this, orientation.orientation(bitmap, orientation1)));
    }


}
