package com.parhamcodeappsgmail.phoneplus;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.parhamcodeappsgmail.phoneplus.Tools.TinyDB;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

public class ErrorCatch extends AppCompatActivity {
    Button back,send;
    TextView text;
    ConstraintLayout layout;
    TinyDB tinyDB;
    String error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_catch);
        tinyDB=new TinyDB(this);
        layout=findViewById(R.id.errorcontainer);
        text=findViewById(R.id.to);
        send=findViewById(R.id.erroremail);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                senbymail();
            }


        });
        back=findViewById(R.id.ret);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentData();

            }
        });

        if(tinyDB.getBoolean("lighttheme")){
            text.setTextColor(getResources().getColor(R.color.darktgray));
            layout.setBackgroundColor(getResources().getColor(R.color.lightgray));
        }
        error=Objects.requireNonNull(getIntent().getExtras()).getString("error");
        Uri uri=generatetxt(this,Calendar.getInstance().getTimeInMillis()+"",error);
        uploadFile(uri);
    }

    public void intentData() {

        // Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(ErrorCatch.this, MainActivity.class);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
        finish();
    }

    public Uri generatetxt(Context context, String sFileName, String sBody) {
        Uri txtURI=null;
        try {
            File file = new File(context.getFilesDir(), "txtfile");

            if (!file.exists()) {
                file.mkdirs();
            }
            File gpxfile = new File(file, sFileName+".txt");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            txtURI=Uri.fromFile(gpxfile);

            // Message.message(this,txtURI.toString(),1);



        } catch (IOException e) {
            e.printStackTrace();
        }
        return txtURI;
    }


    private void uploadFile(Uri path ) {
        //checking if file is available
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        if (path != null) {
            //displaying progress dialog while image is uploading


            //getting the storage reference
            String ext="txt";
            final StorageReference sRef = storageReference.child("/ERRORREPORT/" +Calendar.getInstance().getTimeInMillis()+ "." + ext);

            //adding the file to reference
            sRef.putFile(path)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //dismissing the progress dialog
                            sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //Uri download_uri = uri;
                                    // upload[0] = new Upload(editTextName.getText().toString().trim(), download_uri.toString());
                                    //Toast.makeText(ErrorCatch.this, "فایل خطا ارسال شد", Toast.LENGTH_LONG).show();

                                }
                            });




                            //creating the upload object to store uploaded image details
                            // Upload upload = new Upload(editTextName.getText().toString().trim(), taskSnapshot.getDownloadUrl().toString());
                            //adding an upload to firebase da
                            // tabase
                            // String uploadId = mDatabase.push().getKey();
                            // mDatabase.child(uploadId).setValue(upload[0]);


                            // btn.revertAnimation();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Toast.makeText(ErrorCatch.this, "فایل خطا ارسال نشد\n" +
                            // "چنانچه باز این خطا را دریافت کردید برای ارسال فایل خطا دستگاه را به اینترنت متصل کنید", Toast.LENGTH_LONG).show();

                        }
                    });

        } else {
            //display an error if no file is selected
        }
    }

    private void senbymail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","ParhamCodeApps@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "خطا در برنامه تلفن پلاس");
        emailIntent.putExtra(Intent.EXTRA_TEXT, error);
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

}
