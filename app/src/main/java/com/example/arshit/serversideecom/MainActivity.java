package com.example.arshit.serversideecom;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arshit.serversideecom.Model.Category;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import info.hoang8f.widget.FButton;

public class MainActivity extends AppCompatActivity {


    private DatabaseReference Rootref;
    FirebaseAuth mAuth;
    FirebaseRecyclerAdapter adapter;
    FirebaseUser firebaseUser;
    private String currentUserId;
    EditText txt_category,edit_text_price_discount,edit_food_cat,edit_text_sub_cat,edit_text_price_subcat;
    Button btnSelect,btnUpload;
    private static int Gallery_Pick =1;
    private ProgressDialog mProgressDialog;
    private StorageReference imgStorageReference;
    Uri imageURI;
    RecyclerView recycler_category;
    String subcatdiscount,subcatName,subcatdescription,subcatprice,categoryName;
    Button btn_category,btn_subcat;
    String catName;
    public static final String UPDATE = "UPDATE";
    public static final String DELETE = "DELETE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     toolbar();

     intialise();

    }

    private void toolbar(){


        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.user_toolbar);
        TextView title = findViewById(R.id.toolbar_title);
        title.setText("Ecommerce Server Side App");

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });



    }

    private void intialise(){


        imgStorageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        Rootref = FirebaseDatabase.getInstance().getReference();




        recycler_category = findViewById(R.id.recycler_category);
GridLayoutManager glm = new GridLayoutManager(getApplicationContext(), 1);

        recycler_category.setLayoutManager(glm);


        btn_category =findViewById(R.id.btn_category);
//        btn_subcat = findViewById(R.id.btn_sub_category);///


//        btn_subcat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//             //   new_subCategory();
//
//
//            }
//        });
//

        btn_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new_category();

            }
        });
    }





    private void new_category() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Enter Category Name : ");
        builder.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View custom_layout = inflater.inflate(R.layout.custom_dialog_category,null);

        txt_category = custom_layout.findViewById(R.id.edit_text_cat);

        builder.setView(custom_layout);

        builder.setIcon(R.drawable.ic_shopping_cart_black_24dp);


        builder.setPositiveButton("Select Image ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                String groupName = txt_category.getText().toString();

                if (TextUtils.isEmpty(groupName)){

                    Toast.makeText(MainActivity.this, "Please Write Category Name", Toast.LENGTH_SHORT).show();
                }

                else {

                    chooseImage();

                }

            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.cancel();
            }
        });



        builder.show();
    }

//    private void uploadImage() {
//
//        if (imageURI!= null) {
//            mProgressDialog = new ProgressDialog(this);
//            mProgressDialog.setMessage("Uploading");
//            mProgressDialog.show();
//
//            String imageName = UUID.randomUUID().toString();
//
//            final StorageReference imageFolder = imgStorageReference.child("Category_images" + imageName);
//
//            imageFolder.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                    mProgressDialog.dismiss();
//
//                    Toast.makeText(MainActivity.this, "Uploaded !!", Toast.LENGTH_SHORT).show();
//
//                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//
////                            newCategory = new Category(txt_category.getText().toString(),uri.toString());
//
//                        }
//                    });
//                }
//            });
//
//
//
//
//        }
//
//
//
//    }

        private void chooseImage(){

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,Gallery_Pick);
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


                mProgressDialog = new ProgressDialog(MainActivity.this);
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



                                    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();


                                    HashMap<String,String> hashMap = new HashMap<>();

                                    hashMap.put("Category", txt_category.getText().toString());
                                    hashMap.put("imageURL",downloadUrl);
                                    mRootRef.child("Category").push().setValue(hashMap);

//                                    HashMap<String,String> SubcatHashMap = new HashMap<>();
//
//                                    hashMap.put("Food Name",subcatName);
//                                    hashMap.put("Description",subcatdescription);
//                                    hashMap.put("Price",subcatprice);
//                                    hashMap.put("Discount",subcatdiscount);
//                                    hashMap.put("imageURL",downloadUrl);
//                                    mRootRef.child("Category").push().setValue(SubcatHashMap);


                                    mProgressDialog.dismiss();

                                    Toast.makeText(MainActivity.this, txt_category. getText().toString()+" Category Created ", Toast.LENGTH_SHORT).show();

                                }

                            });

                        }

              else {

                            Toast.makeText(MainActivity.this, "Not Working", Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                        }

                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }

        }
    }




    @Override
    protected void onStart() {
        super.onStart();

     final DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Category");
        database.keepSynced(true);

        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(database, Category.class).build();

         adapter = new FirebaseRecyclerAdapter<Category, UserViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull final UserViewHolder holder, int position, @NonNull final Category category) {

                final String selected_user_id = getRef(position).getKey();

                database.child(selected_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChild("imageURL")){


//                            Category category1 = dataSnapshot.getChildren().getValue(Category.class);
//                           catName = category.getName();

                            final String userImage = dataSnapshot.child("imageURL").getValue().toString();
                           categoryName = dataSnapshot.child("Category").getValue().toString();
//                            String status = dataSnapshot.child("status").getValue().toString();



                            holder.uName.setText(categoryName);

                            Picasso.get().load(userImage).networkPolicy(NetworkPolicy.OFFLINE)
                                    .placeholder(R.drawable.profile_avatar_small).into(holder.profileImg, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(Exception e) {

                                    Picasso.get().load(userImage).placeholder(R.drawable.profile_avatar_small).into(holder.profileImg);
                                }
                            });


                        }
                        holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent profileIntent = new Intent(MainActivity.this, SubCategoryActivity.class);
                                profileIntent.putExtra("selected_user_id",selected_user_id);
                                profileIntent.putExtra("CategoryName", String.valueOf(holder.uName.getText().toString()));

                                startActivity(profileIntent);
                            }
                        });



                    }



                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }




            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);

                return new UserViewHolder(view);

            }

        } ;


        recycler_category.setAdapter(adapter);
        adapter.startListening();
    }



    public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public CircleImageView profileImg;

        TextView uName,uStatus;
        View  mView;

        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

           mView.setOnCreateContextMenuListener(this);
            uName = mView.findViewById(R.id.category_name);
            profileImg = mView.findViewById(R.id.category_image);
        }


        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select The Action");
            contextMenu.add(0,0,getAdapterPosition(),UPDATE);
            contextMenu.add(0,0,getAdapterPosition(),DELETE);
        }
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {

         if(item.getTitle().equals(DELETE))
         {

          DeleteDialog(adapter.getRef(item.getOrder()).getKey());


         }

        return super.onContextItemSelected(item);


    }

    private void DeleteDialog(String ref) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Category");

       database.child(ref).removeValue();

    }

    private void showUpdateDialog(DatabaseReference ref, Category item) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Update Category Name : ");
        builder.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View custom_layout = inflater.inflate(R.layout.custom_dialog_category,null);

        txt_category = custom_layout.findViewById(R.id.edit_text_cat);

        txt_category.setText(item.getName());
        builder.setView(custom_layout);

        builder.setIcon(R.drawable.ic_shopping_cart_black_24dp);


        builder.setPositiveButton("Select Image ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                String groupName = txt_category.getText().toString();

                if (TextUtils.isEmpty(groupName)){

                    Toast.makeText(MainActivity.this, "Please Write Category Name", Toast.LENGTH_SHORT).show();
                }

                else {

                    chooseImage();

                }

            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.cancel();
            }
        });



        builder.show();


    }

    private void CreateNewCategory(final String groupName){


        HashMap<String,String> hashMap = new HashMap<>();

        hashMap.put("Category",groupName);
        hashMap.put("imageURL","default");

        Rootref.child("Category").push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                if (task.isSuccessful()){

                    Toast.makeText(MainActivity.this, groupName+"  is created", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}
