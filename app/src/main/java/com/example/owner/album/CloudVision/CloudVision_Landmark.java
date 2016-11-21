package com.example.owner.album.CloudVision;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.example.owner.album.Insert.Classification_Info_Eng_Insert;
import com.example.owner.album.Insert.Picture_Insert;
import com.example.owner.album.Translate.TranslateEngToJap;
import com.example.owner.album.Translate.Translate_landmark_EngToJap;
import com.example.owner.album.query.Picture_Query;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Owner on 2016/10/24.
 */

public class CloudVision_Landmark {

    private static final String CLOUD_VISION_API_KEY = "AIzaSyA9MVo_hv-TskogWCsuiLscq5c1pNufKDo";
    public void callCloudVision(final Bitmap bitmap) throws IOException {

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
                            labelDetection.setType("LANDMARK_DETECTION");
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


                if(result.size()!=0){
                    Picture_Insert picture_insert=new Picture_Insert();
                    picture_insert.Insert_Landmark_Eng(result.get(0));
                    Translate_landmark_EngToJap translate_landmark_engtojap=new Translate_landmark_EngToJap(result.get(0));
                    translate_landmark_engtojap.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                }


            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private ArrayList<String> convertResponse(BatchAnnotateImagesResponse response) {

        ArrayList<String> message = new ArrayList<>();
        List<EntityAnnotation> Landmarks = response.getResponses().get(0).getLandmarkAnnotations();
        if (Landmarks != null) {
            for (EntityAnnotation label : Landmarks) {
                message.add(label.getDescription());
            }
        }

        return message;
    }
}
