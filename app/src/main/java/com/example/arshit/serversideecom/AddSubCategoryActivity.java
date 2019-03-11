package com.example.arshit.serversideecom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arshit.serversideecom.SideNavigation.Fragments.MainActivity1;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.UUID;

public class AddSubCategoryActivity extends AppCompatActivity {


    FirebaseRecyclerAdapter adapter;
    private DatabaseReference Rootref;
    String selected_user_id;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    private String currentUserId;
    EditText txt_category, edit_text_sub_cat_description, edit_text_price_discount, edit_food_cat, edit_text_price_subcat;
    Button btnSelect, btnUpload;
    private static int Gallery_Pick = 1;
    private ProgressDialog mProgressDialog;
    private StorageReference imgStorageReference;
    Uri imageURI;
    RecyclerView recycler_sub_category;
    String subcatdiscount, subcatName, subcatdescription, subcatprice, CategoryName;

    Button btnAddImage;
    Intent intent;
    String CategoryId;
    String FoodName;
    FloatingActionButton addSubbtn;
    View custom_layout;

    TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub_category);

        intialise();
    }

    private void intialise() {


        imgStorageReference = FirebaseStorage.getInstance().getReference();

        intent = getIntent();
        CategoryId = intent.getStringExtra("selected_user_id");

        CategoryName = intent.getStringExtra("CategoryName");

        edit_food_cat = findViewById(R.id.edit_food_cat);
        edit_text_sub_cat_description = findViewById(R.id.edit_text_sub_cat);
        edit_text_price_subcat = findViewById(R.id.edit_text_price_subcat);
        edit_text_price_discount = findViewById(R.id.edit_text_price_discount);

        btnAddImage = (Button) findViewById(R.id.btnAddImage);








            btnAddImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    subcatName = edit_food_cat.getText().toString().trim();

                    subcatdescription = edit_text_sub_cat_description.getText().toString().trim();

                    subcatprice = edit_text_price_subcat.getText().toString().trim();

                    subcatdiscount = edit_text_price_discount.getText().toString().trim();

                    if (TextUtils.isEmpty(subcatName) || TextUtils.isEmpty(subcatdescription)
                            || TextUtils.isEmpty(subcatprice) || TextUtils.isEmpty(subcatdescription)){

                        Toast.makeText(getApplicationContext(), "Enter All the Detail", Toast.LENGTH_SHORT).show();

                         return;
                    }

                    else {

                        addSubcategory();
                    }


                }
            });

    }

    private void addSubcategory() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,Gallery_Pick);

    }


    private void toolbar() {


        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.user_toolbar);
        ImageView imageView = findViewById(R.id.toolbar_img);

        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(CategoryName);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == Gallery_Pick && resultCode == RESULT_OK) {

            Uri imageURI = data.getData();

            CropImage.activity(imageURI).setAspectRatio(1, 1).start(this);
        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {


                mProgressDialog = new ProgressDialog(AddSubCategoryActivity.this);
                mProgressDialog.setTitle("Saving Changes");
                mProgressDialog.setMessage("Wait untill changes are saved");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();


                Uri resultUri = result.getUri();

                final String imageName = UUID.randomUUID().toString();

                StorageReference filepath = imgStorageReference.child("Category_images").child(imageName + ".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {


                            imgStorageReference.child("Category_images").child(imageName+ ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String downloadUrl = uri.toString();



                                    final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference("SubCategory").child(CategoryId);



                                    final HashMap<String,String> hashMap = new HashMap<>();

                                    hashMap.put("MenuId",CategoryId);
                                    hashMap.put("Food Name",edit_food_cat.getText().toString());
                                    hashMap.put("Description",edit_text_sub_cat_description.getText().toString());
                                    hashMap.put("Price",edit_text_price_subcat.getText().toString());
                                    hashMap.put("Discount",edit_text_price_discount.getText().toString());
                                    hashMap.put("imageSubCat",downloadUrl);

//                                            mRootRef.child("SubCategory").push().setValue(hashMap);

                                    mRootRef.push().setValue(hashMap);
                                    mProgressDialog.dismiss();


//

                                    Toast.makeText(getApplicationContext(), " Category Updated", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(getApplicationContext(),MainActivity1.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);


                                    Toast.makeText(AddSubCategoryActivity.this, "Subcategory Updated", Toast.LENGTH_SHORT).show();

                                    mProgressDialog.dismiss();



                                }

                            });

                        }

                        else {

                            Toast.makeText(AddSubCategoryActivity.this, "Not Working", Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                        }

                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }

        }
    }

}

