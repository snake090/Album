package com.example.owner.album.Album;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.owner.album.Component.GalleryAdapter;
import com.example.owner.album.Component.Orientation;
import com.example.owner.album.ImageShow.FullscreenActivity;
import com.example.owner.album.ImageShow.MyApplication;
import com.example.owner.album.Main.MainActivity;
import com.example.owner.album.Map.AlbumMapsActivity;
import com.example.owner.album.R;
import com.example.owner.album.query.Album_Query;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PictureListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private GridView gridView;
    private ArrayList<Bitmap> bitmaps;
    private ArrayList<String> bitmap_path;
    private GalleryAdapter galleryAdapter;
    private MyApplication app;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_list);
        galleryAdapter = new GalleryAdapter(this);
        gridView=(GridView)findViewById(R.id.grid);
        app = (MyApplication) this.getApplication();
        Intent i = getIntent();
        id=i.getIntExtra("id",-1);
        ArrayList<String> path;
        if(id!=-1){
            path=new Album_Query().Get_Path(id);
            setTitle(new Album_Query().Get_AlbumName(id));
            grid(path);
        }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // galleryAdapter.remove(position);
                //   Bitmap bitmap=galleryAdapter.getBitmap(position);

                Intent intent;

                intent = new Intent(PictureListActivity.this, FullscreenActivity.class);
                // intent.setClassName("Main.MainActivity","Image_Show.FullscreenActivity");

                //  intent.putExtra("bitmap",bitmaps);
                app.setBitmaps(bitmap_path);
                app.setPosition(position);

                startActivity(intent);


            }
        });



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.picture_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int Id = item.getItemId();

        if (Id == R.id.nav_gallery) {
            Intent intent;
            intent = new Intent(PictureListActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (Id == R.id.nav_album) {
            Intent intent;
            intent = new Intent(PictureListActivity.this, AlbumList_Activity.class);
            startActivity(intent);
        } else if (Id == R.id.nav_map) {
            Intent intent = new Intent(PictureListActivity.this, AlbumMapsActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("kind",0);
            startActivity(intent);
        } else if (Id == R.id.nav_share) {

        } else if (Id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void grid(ArrayList<String> path) {
        bitmaps = new ArrayList<>();
        bitmap_path = new ArrayList<>();

        ExifInterface exifInterface = null;
        Orientation orientation_class = new Orientation();
        for (String filename : path) {
            File file = new File(filename);
            if (file.exists()) {
                try {
                    exifInterface = new ExifInterface(filename);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int orientation = exifInterface.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);
                Cursor cursor = getContentResolver().query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        null,
                        MediaStore.Images.ImageColumns.DATA + " = ?",
                        new String[]{filename},
                        null);
                ContentResolver cr = getContentResolver();
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
                    Bitmap bmp = MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.MICRO_KIND, null);
                    bmp = orientation_class.orientation(bmp, orientation);
                    bitmaps.add(bmp);
                    cursor.moveToNext();
                }
                bitmap_path.add(file.getPath());

            }
        }
        galleryAdapter.addImageBitmaps(bitmaps);
        galleryAdapter.setProgressBarStyle(GalleryAdapter.PROGRESS_STYLE_MEDIUM);
        gridView.setAdapter(galleryAdapter);
    }
}
