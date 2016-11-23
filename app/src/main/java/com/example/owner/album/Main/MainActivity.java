package com.example.owner.album.Main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.owner.album.Album.AlbumList_Activity;
import com.example.owner.album.Album.Album_Create_Activity;
import com.example.owner.album.CloudVision.CloudVision_Landmark;
import com.example.owner.album.Exif.Exif;
import com.example.owner.album.ImageShow.FullscreenActivity;
import com.example.owner.album.ImageShow.MyApplication;
import com.example.owner.album.Insert.Classification_Info_Eng_Insert;
import com.example.owner.album.Insert.Picture_Insert;
import com.example.owner.album.R;
import com.example.owner.album.Translate.TranslateEngToJap;
import com.example.owner.album.query.Picture_Query;
import com.facebook.stetho.Stetho;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

import static com.example.owner.album.R.id.gridview;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;


    private static final String CLOUD_VISION_API_KEY = "AIzaSyA9MVo_hv-TskogWCsuiLscq5c1pNufKDo";

    private static final String TAG = "Main";
    private static final int GALLERY_IMAGE_REQUEST = 1;
    private ArrayList<String> classification_info_eng = new ArrayList<>();

    private SearchView searchView;
    private GridView gridView;
    private GalleryAdapter galleryAdapter;
    private ArrayList<Bitmap> bitmaps;
    private MyApplication app;
    private ArrayList<String> bitmap_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ButterKnife.bind(this);

/*
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
*/
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);


        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());


        Realm.init(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            startGalleryChooser();
        });

        gridView = (GridView) findViewById(gridview);
        galleryAdapter = new GalleryAdapter(this);
        app = (MyApplication) this.getApplication();
        Picture_Query picture_query = new Picture_Query();
        ArrayList<String> path = picture_query.Query();
        grid(path);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // galleryAdapter.remove(position);
                //   Bitmap bitmap=galleryAdapter.getBitmap(position);

                Intent intent;

                intent = new Intent(MainActivity.this, FullscreenActivity.class);
                // intent.setClassName("Main.MainActivity","Image_Show.FullscreenActivity");

                //  intent.putExtra("bitmap",bitmaps);
                app.setBitmaps(bitmap_path);
                app.setPosition(position);

                startActivity(intent);


            }
        });


    }


    public void startGalleryChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select a photo"),
                GALLERY_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            getExif(data.getData());
            uploadImage(data.getData());

        }
    }

    public void getExif(Uri uri) {
        // ContentResolverを用いてURIが示す画像ファイルの情報を取得
        Cursor c = getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
        String filename = null;
        if (c.moveToFirst()) {
            int index = c.getColumnIndex(MediaStore.Images.Media.DATA);
            // 画像ファイルのディレクトリを取得
            filename = c.getString(index);
        }

        File file = new File(filename);
        if (file.exists()) {


            ExifInterface exifInterface = null;

            try {
                // ExifInterfaceインスタンスを生成
                exifInterface = new ExifInterface(file.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }

            String latitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            String longitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            String dateTime = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);

            //Log.d("longitude", hour);
            Exif exif = new Exif();

            if (latitude != null) {
                latitude = exif.ExifHourMinSecToDegrees(latitude);
            }
            if (longitude != null) {
                longitude = exif.ExifHourMinSecToDegrees(longitude);
            }

            Picture_Insert.Insert_Picture(this,file.getPath(), dateTime, latitude, longitude);

        }
    }



    //cloudvision
    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                Bitmap bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                                1200);

                CloudVision_Landmark cloudVision_landmark=new CloudVision_Landmark();
                cloudVision_landmark.callCloudVision(bitmap);
                callCloudVision(bitmap);

            } catch (IOException e) {
                Log.d(TAG, "Image picking failed because " + e.getMessage());

            }
        } else {
            Log.d(TAG, "Image picker gave us a null image.");
        }

    }

    public Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    private void callCloudVision(final Bitmap bitmap) throws IOException {

        ArrayList<String> info = new ArrayList<>();
        ProgressDialog dialog;
        // Do the real work in an async task, because we need to use the network anyway
        new AsyncTask<Object, Void, ArrayList<String>>() {
            @Override
            protected ArrayList<String> doInBackground(Object... params) {
                try {
                    Log.d(TAG, "strat");
                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                    builder.setVisionRequestInitializer(new
                            VisionRequestInitializer(CLOUD_VISION_API_KEY));
                    Vision vision = builder.build();

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                            new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
                        AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

                        // Add the image
                        Image base64EncodedImage = new Image();
                        // Convert the bitmap to a JPEG
                        // Just in case it's a format that Android understands but Cloud Vision
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                        byte[] imageBytes = byteArrayOutputStream.toByteArray();

                        // Base64 encode the JPEG
                        base64EncodedImage.encodeContent(imageBytes);
                        annotateImageRequest.setImage(base64EncodedImage);

                        // add the features we want
                        annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                            Feature labelDetection = new Feature();
                            labelDetection.setType("LABEL_DETECTION");
                            labelDetection.setMaxResults(10);
                            add(labelDetection);
                        }});

                        // Add the list of one thing to the request
                        add(annotateImageRequest);
                    }});

                    Vision.Images.Annotate annotateRequest =
                            vision.images().annotate(batchAnnotateImagesRequest);
                    // Due to a bug: requests to Vision API containing large images fail when GZipped.
                    annotateRequest.setDisableGZipContent(true);
                    Log.d(TAG, "created Cloud Vision request object, sending request");

                    BatchAnnotateImagesResponse response = annotateRequest.execute();

                    Log.d(TAG, "created Cloud Vision request object, sending request");
                    return convertResponse(response);

                } catch (GoogleJsonResponseException e) {
                    Log.d(TAG, "failed to make API request because " + e.getContent());
                } catch (IOException e) {
                    Log.d(TAG, "failed to make API request because of other IOException " +
                            e.getMessage());
                }
                ArrayList<String> message = new ArrayList<String>();
                message.add("Cloud Vision API request failed. Check logs for details.");
                return message;
            }

            protected void onPostExecute(ArrayList<String> result) {

                Classification_Info_Eng_Insert classification_Info_Eng_Insert = new Classification_Info_Eng_Insert();
                classification_Info_Eng_Insert.Insert_Classification_Info(result);


                new TranslateEngToJap(result).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                Picture_Query picture_query = new Picture_Query();
                ArrayList<String> path = picture_query.Query();

                grid(path);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private ArrayList<String> convertResponse(BatchAnnotateImagesResponse response) {

        ArrayList<String> message = new ArrayList<>();
        List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
        if (labels != null) {
            for (EntityAnnotation label : labels) {
                message.add(label.getDescription());

            }
        }

        return message;
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
        getMenuInflater().inflate(R.menu.main, menu);

        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);

        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("text", newText);
                Picture_Query picture_query = new Picture_Query();
                ArrayList<String> path = picture_query.Query(newText);
                Log.d("path", "");
                if (path.size() != 0) {
                    grid(path);
                } else {
                    galleryAdapter.removeAll();
                }
                if (newText.equals("")) {
                    path = picture_query.Query();
                    grid(path);
                }
                return false;
            }

        });
        return true;
    }

    public void grid(ArrayList<String> path) {
        bitmaps = new ArrayList<>();
        bitmap_path = new ArrayList<>();
        galleryAdapter.removeAll();

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

        Log.d("grid", "grid");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            deleteBookShelf();
        }else if(id==R.id.action_create_album) {

            Intent intent;

            intent = new Intent(MainActivity.this, Album_Create_Activity.class);


            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent intent;
            intent = new Intent(MainActivity.this, AlbumList_Activity.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void deleteBookShelf() {
        Realm r = Realm.getDefaultInstance();

        r.executeTransactionAsync(realm -> {
            realm.deleteAll();
        });

    }


}
