package com.example.owner.album.Map;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.owner.album.Album.PictureListActivity;
import com.example.owner.album.Component.GalleryAdapter;
import com.example.owner.album.Component.Orientation;
import com.example.owner.album.ImageShow.FullscreenActivity;
import com.example.owner.album.ImageShow.MyApplication;
import com.example.owner.album.R;
import com.example.owner.album.model.Album;
import com.example.owner.album.model.Picture_Info;
import com.example.owner.album.query.Album_Query;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.face.Landmark;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import io.realm.RealmResults;

public class AlbumMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Double> longitude;
    private ArrayList<Double> latitude;
    private ArrayList<Bitmap> bitmaps;
    private ArrayList<String> bitmap_path;
    private ArrayList<String> landmark;
    private int index = 0;
    private MyApplication app;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_maps);
        app = (MyApplication) this.getApplication();
        Intent i = getIntent();
        int id = i.getIntExtra("id", -1);
        if (id != -1) {
            RealmResults<Album> album = new Album_Query().Get_Album(id);
            latitude = new ArrayList<>();
            longitude = new ArrayList<>();
            landmark=new ArrayList<>();
            ArrayList<String> path;
            for (Picture_Info pictureInfo : album.get(0).getPicture_infos()) {
                String Longitude=pictureInfo.getLongitude();
                String Latitude=pictureInfo.getLatitude();
                if(!"".equals(longitude)&&!"".equals(latitude)) {
                    longitude.add(Double.parseDouble(Longitude));
                    latitude.add(Double.parseDouble(Latitude));
                    String Landmark=pictureInfo.getLandmark_jap();
                    if(!"".equals(Landmark)){
                        landmark.add(Landmark);
                    }else{
                        landmark.add("");
                    }
                }
                path = new Album_Query().Get_Path(id);
                GetBitmap(path);
            }
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoContents(Marker marker) {

                View view = getLayoutInflater().inflate(R.layout.info_window, null);
                // タイトル設定
                TextView title = (TextView)view.findViewById(R.id.info_title);
                title.setText(marker.getTitle());
                // 画像設定
                ImageView imageview = (ImageView) view.findViewById(R.id.imageView3);
                imageview.setImageBitmap(bitmaps.get(index));
                return view;

            }

            @Override
            public View getInfoWindow(Marker marker) {

                return null;
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String id = marker.getId();
                id=id.replaceAll("m","");
                index=Integer.parseInt(id);
                LatLng location = new LatLng(latitude.get(index), longitude.get(index));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17));
                return false;
            }
        });


        if(longitude.size()==1){
            LatLng location = new LatLng(latitude.get(index), longitude.get(index));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17));
        }else {
            LatLngBounds JAPAN = new LatLngBounds(
                    new LatLng(20, 122), new LatLng(45, 153));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(JAPAN.getCenter(), 4));
        }
        for (int i = 0; i < longitude.size(); i++) {
            LatLng location = new LatLng(latitude.get(i), longitude.get(i));
            MarkerOptions options = new MarkerOptions();
            options.position(location);
            String Landmark=landmark.get(i);
            if(!"".equals(Landmark)) {
                options.title(Landmark);
            }
            Marker marker = mMap.addMarker(options);

        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent;
                String id = marker.getId();
                id=id.replaceAll("m","");
                intent = new Intent(AlbumMapsActivity.this, FullscreenActivity.class);
                app.setBitmaps(bitmap_path);
                app.setPosition(Integer.parseInt(id));
                startActivity(intent);
            }
        });
    }

    public void GetBitmap(ArrayList<String> path) {
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
    }

}
