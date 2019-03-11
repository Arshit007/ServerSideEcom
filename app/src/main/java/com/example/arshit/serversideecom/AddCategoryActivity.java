package com.example.arshit.serversideecom;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.arshit.serversideecom.SideNavigation.Fragments.Fragment.HomeFragment;
import com.example.arshit.serversideecom.SideNavigation.Fragments.MainActivity1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.UUID;

public class AddCategoryActivity extends AppCompatActivity {




    private DatabaseReference Rootref;
    ImageView profilePic;

    HashMap<String,String> hashMap;
    ProgressDialog mProgressDialog;

    Button btnprofile;
    SharedPreferences myprefs;
    private static final int GALLERY_PICK = 1;
    String currentUserId;
    StorageReference imgStorageReference;
    EditText name,email,number;
//    private ProgressDialog mProgressDialog;
    String oldName,oldEmail,oldNumber,newName,newEmail,newNumber;
    View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        intialise();
    }



    private void intialise() {



        profilePic = (ImageView)findViewById(R.id.profilePic);

        imgStorageReference =  FirebaseStorage.getInstance().getReference();

        name = findViewById(R.id.profilename);

//        profilePic.setVisibility(View.INVISIBLE);
//
            profilePic.setVisibility(View.VISIBLE);

            profilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String nameText = name.getText().toString().trim();

                    if (TextUtils.isEmpty(nameText)) {
                        Toast.makeText(getApplicationContext(), "Enter Category Name First!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    else {

                          changeProfilePic();
                    }
                }
            });


    }

    private void changeProfilePic() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(AddCategoryActivity.this);


    }






    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        currentUserId = UUID.randomUUID().toString();

        Rootref = FirebaseDatabase.getInstance().getReference("Category");

        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK){

            Uri imageURI = data.getData();
            CropImage.activity(imageURI).setAspectRatio(1,1).start(AddCategoryActivity.this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {




                Uri resultUri = result.getUri();



                StorageReference filepath = imgStorageReference.child("Profile_images").child(currentUserId+".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()){


                            imgStorageReference.child("Profile_images").child(currentUserId+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    final String downloadUrl = uri.toString();


                                    hashMap = new HashMap<>();

                                    hashMap.put("CategoryId",currentUserId);
                                    hashMap.put("Category",name.getText().toString());
                                    hashMap.put("imageURL",downloadUrl);

                                    Rootref.child(currentUserId).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {


                                                Toast.makeText(getApplicationContext(), " Category Updated", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(getApplicationContext(),MainActivity1.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);


//
//                                                Toast.makeText(getApplicationContext(), ""+number.getEditText().getText().toString(), Toast.LENGTH_SHORT).show();
//                                                onResume();
                                            }


                                        }
                                    });

                                }
                            });
                        }

//                           Toast.makeText(SettingsActivity.this, "Working", Toast.LENGTH_SHORT).show();
//                     }
                        else {

                            Toast.makeText(getApplicationContext(), "Not Working", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }

        }
    }




}
