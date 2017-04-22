package com.greenthumb.greenthumb.Vision;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.greenthumb.greenthumb.DB.Model.Harvest;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.AnalysisResult;
import com.microsoft.projectoxford.vision.contract.Caption;
import com.microsoft.projectoxford.vision.contract.Category;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;

import com.greenthumb.greenthumb.R;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by madrinathapa on 4/22/17.
 */

public class VisionActivity extends AppCompatActivity {

    private ImageView imgView;
    private Bitmap bitmap;
    private static final int PICK_IMAGE_REQUEST = 1;
    static final int REQUEST_TAKE_PHOTO = 1;

   // FirebaseApp.initializeApp(this);
    /*Image details*/
   // private FirebaseAuth firebaseAuth;
    private FirebaseApp firebaseApp ;
    private DatabaseReference dbreference;
    private FirebaseStorage storage ;
    private StorageReference farmStorageRef ;
    private StorageReference plotStorageRef ;
    private StorageReference plotImagesRef;
    private Uri plantImageUri = null;
    private String mCurrentPhotoPath;

    /*Harvest details*/
    private String caption;
    private ArrayList<String> tags;
    private HashMap<String, String> colors;
    private ArrayList<String> categories;
    private Harvest harvest;
    private VisionServiceClient client;
    private String sectionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vision);
        Log.e("Hey madrina","hjsd");
        imgView = (ImageView) findViewById(R.id.imageView);
        tags = new ArrayList<>();
        colors = new HashMap<>();
        categories  =new ArrayList<>();
        harvest = new Harvest();

        if (client==null){
            client = new VisionServiceRestClient(getString(R.string.subscription_key));
        }


        //Hide keyboard
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        dbreference = FirebaseDatabase.getInstance().getReference();
        View v = findViewById(android.R.id.content);
    }
    public void savePlant(){
        DatabaseReference def = dbreference.child("farms");
        //sectionId = plotStorageRef.getChild();

        if(sectionId == null){
            sectionId = def.push().getKey();
        }
        //String userId = user.getUid();
    //    String plantImagePath = userId+"/"+plantId+"/"+plantName.replaceAll(" ","")+".jpg";
        Date lastModified = new Date();

      // plotImagesRef = plotStorageRef.child(sectionId.replaceAll(" ","")+".jpg");

       // putImagetoFireBase();
    }

    public void onClickCamera(View v){

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.greenthumb.greenthumb.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }

    }

    public void imagePicker(View v){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void setPic() {
		/* Get the size of the ImageView */
        int targetW = imgView.getWidth();
        int targetH = imgView.getHeight();

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

		/* Decode the JPEG file into a Bitmap */
        bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		/* Associate the Bitmap to the ImageView */
        imgView.setImageBitmap(bitmap);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

   /* private void putImagetoFireBase(){
        plotImagesRef = plotStorageRef.child(sectionId.replaceAll(" ","")+".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBdata = baos.toByteArray();

        UploadTask uploadTask = plotImagesRef.putBytes(imageBdata);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }*/

    private void handleBigCameraPhoto() {
        if (mCurrentPhotoPath != null) {
            setPic();
            galleryAddPic();
            mCurrentPhotoPath = null;
        }

    }

    private void galleryAddPic() {

        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        plantImageUri = contentUri;
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                imgView.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else
        if (resultCode == RESULT_OK) {
            handleBigCameraPhoto();
            Log.e("Madrin","first");
            //putImagetoFireBase();
        }

        doDescribe();
        doAnalyse();
    }

    public void doAnalyse() {
        //analyseresult.setTex"Analyzing...");

        try {
            new doAnalyzeRequest().execute();
        } catch (Exception e)
        {
            //res.setText("Error encountered. Exception is: " + e.toString());
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void doDescribe() {
        //mButtonSelectImage.setEnabled(false);
        //res.setText("Describing...");

        try {
            new doRequest().execute();
        } catch (Exception e)
        {
           // res.setText("Error encountered. Exception is: " + e.toString());
        }
    }

    private class doRequest extends AsyncTask<String, String, String> {
        // Store error message
        private Exception e = null;

        public doRequest() {
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                return process();
            } catch (Exception e) {
                e.printStackTrace();
                this.e = e;
            }

            return null;
        }

        private String process() throws VisionServiceException, IOException {
            Gson gson = new Gson();

            // Put the image into an input stream for detection.
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);

            ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());
            AnalysisResult v = client.describe(inputStream, 1);
            String result = gson.toJson(v);

            return result;
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            // Display based on error existence

            //res.setText("");
            if (e != null) {
                Log.e("MADDFG","NUL");
               // res.setText("Error: " + e.getMessage());
                this.e = null;
            } else {
                Gson gson = new Gson();
                AnalysisResult result = gson.fromJson(data, AnalysisResult.class);

                for (Caption cap: result.description.captions) {
                    caption = cap.text;
                }

                harvest.setCaption(caption);
                System.out.println("BEFORE: " +caption+" :after");
                //res.append("\n");

                for (String tag: result.description.tags) {
                    tags.add(tag);
                }
                harvest.setTags(tags);
            }

        }
    }

    private class doAnalyzeRequest extends AsyncTask<String, String, String> {
        // Store error message
        private Exception e = null;

        public doAnalyzeRequest() {
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                return analyzeProcess();
            } catch (Exception e) {
                this.e = e;    // Store error
            }

            return null;
        }

        private String analyzeProcess() throws VisionServiceException, IOException {
            Gson gson = new Gson();
            String[] features = {"ImageType", "Color", "Faces", "Adult", "Categories"};
            String[] details = {};

            // Put the image into an input stream for detection.
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());

            AnalysisResult v = client.analyzeImage(inputStream, features, details);

            String result = gson.toJson(v);
            //Log.e("result", result);

            return result;
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            if (e != null) {
                this.e = null;
            } else {
                Gson gson = new Gson();
                AnalysisResult result = gson.fromJson(data, AnalysisResult.class);

                for (Category category: result.categories) {
                    categories.add(category.name);
                }
                harvest.setCategories(categories);

                colors.put("accentcolor", result.color.accentColor);
                colors.put("foregroundcolor", result.color.dominantColorForeground);
                colors.put("backgroundcolor", result.color.dominantColorBackground);
                harvest.setColors(colors);

            }
            DatabaseReference def = dbreference.child("farms");

            if(sectionId == null){
                sectionId = def.child("plot").push().getKey();
            }
            System.out.println("COlors "+colors + " Caption : "+caption+" tags "+tags + "Cate: "+categories);
            def.child(sectionId).setValue(harvest);

            System.out.println("Harvest Data: "+harvest.toString());
            System.out.println("COlors "+harvest.getColors() + " Caption : "+harvest.getCaption()+" tags "+ harvest.getTags() + "Cate: "+harvest.getCategories());
        }
    }
}
