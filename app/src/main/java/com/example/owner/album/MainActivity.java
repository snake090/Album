package com.example.owner.album;

import android.app.ProgressDialog;
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
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.owner.album.Exif.Exif;
import com.example.owner.album.Insert.Album_Insert;
import com.example.owner.album.Insert.Classification_Info_Eng_Insert;
import com.example.owner.album.Insert.Picture_Insert;
import com.example.owner.album.Translate.TranslateResult;
import com.example.owner.album.model.Picture_Info;
import com.example.owner.album.query.Album_Query;
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
import com.memetix.mst.translate.Translate;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @BindView(R.id.button_create)
    Button buttonCreate;
    @BindView(R.id.button_delete)
    Button buttonDelete;
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
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.imageView2)
    ImageView imageView2;
    private ArrayList<String> classification_info_eng = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);


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
        buttonCreate.setOnClickListener(v -> {
            Log.d("AAA", "Created");
            createBookShelf();
        });


        buttonDelete.setOnClickListener(v -> {
            Log.d("AAA", "Deleted");
            deleteBookShelf();
        });

        Realm.init(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            startGalleryChooser();
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

            Bitmap imgBitmap = BitmapFactory.decodeFile(file.getPath());

            imageView2.setImageBitmap(imgBitmap);

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

            Picture_Insert.Insert_Picture(file.getPath(), dateTime, latitude, longitude);

        }
    }


    private static String basename(String path) {
        File file = new File(path);
        return file.getName();
    }

    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                Bitmap bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                                1200);

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

                Classification_Info_Eng_Insert classification_Info_Eng_Insert=new Classification_Info_Eng_Insert();
                classification_Info_Eng_Insert.Insert_Classification_Info(result);

                TranslateResult translate = new TranslateResult(result);
                translate.execute();
            }
        }.execute();
    }

    private ArrayList<String> convertResponse(BatchAnnotateImagesResponse response) {

        ArrayList<String> message = new ArrayList<>();
        List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
        List<EntityAnnotation> landmarks = response.getResponses().get(0).getLandmarkAnnotations();
        if (labels != null) {
            for (EntityAnnotation label : labels) {
                message.add(label.getDescription());
                classification_info_eng.add(label.getDescription());

            }
        }
        if (landmarks != null) {
            for (EntityAnnotation landmark : landmarks) {
                message.add(landmark.getDescription());
                classification_info_eng.add(landmark.getDescription());
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
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void createBookShelf() {

        Picture_Insert insert1 = new Picture_Insert();
        //    insert1.Insert_DB();

        Album_Insert insert = new Album_Insert();
        insert.Insert_DB();
        Picture_Query query = new Picture_Query();
        RealmResults<Picture_Info> results = query.Query();

        Album_Query query1 = new Album_Query();
        ArrayList<String> path = query1.Path_Query(2);
        Log.d("for", path.get(1));
        // textView2.setText(results.get(0).getPath());


        //       textView2.setText(results1.get(0).getPath());
    }

    private void deleteBookShelf() {
        Realm r = Realm.getDefaultInstance();

        r.executeTransactionAsync(realm -> {
            realm.deleteAll();
        });

    }


}
