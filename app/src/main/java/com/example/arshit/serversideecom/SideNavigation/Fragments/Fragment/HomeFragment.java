package com.example.arshit.serversideecom.SideNavigation.Fragments.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arshit.serversideecom.AddCategoryActivity;
import com.example.arshit.serversideecom.Model.Category;
import com.example.arshit.serversideecom.R;
import com.example.arshit.serversideecom.SubCategoryActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.UUID;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class HomeFragment extends Fragment {

    private DatabaseReference Rootref;
    String d;
    HashMap<String,String> hashMap,hashMap1;
    FirebaseAuth mAuth;
    FirebaseRecyclerAdapter adapter;
    FirebaseUser firebaseUser;
    String CategoryName;
    private String currentUserId;
    EditText TextCategory;
    Button btnSelect,btnUpload;

    private ProgressDialog mProgressDialog;
    String id;
    private StorageReference imgStorageReference;
    RecyclerView recycler_category;
    Button btn_category,btn_subcat;
    private static final int GALLERY_PICK = 1;
    String catName;
    public static final String UPDATE = "UPDATE";
    public static final String DELETE = "DELETE";
    View view;
    TextView txt;

    public HomeFragment() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      view = inflater.inflate(R.layout.fragment_home, container, false);
        intialise();

        return view;

    }



    private void intialise(){


        imgStorageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();


        recycler_category = view.findViewById(R.id.recycler_category);
        GridLayoutManager glm = new GridLayoutManager(getContext(), 1);

        recycler_category.setLayoutManager(glm);

        id = UUID.randomUUID().toString();

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("username", id);

        btn_category = view.findViewById(R.id.btn_category);

//showDialog();

        btn_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getContext(),AddCategoryActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);



            }
        });


        showCategory();
        onResume();
    }




//     private void uploadImage() {
//
//        if (imageURI!=null){
//
//                mProgressDialog = new ProgressDialog(getActivity());
//                mProgressDialog.setTitle("Saving Changes");
//                mProgressDialog.setMessage("Wait untill changes are saved");
//                mProgressDialog.setCanceledOnTouchOutside(false);
//
//
//                            final String imageName = UUID.randomUUID().toString();
//
//                final StorageReference filepath = imgStorageReference.child("Category_images/"+imageName);
//
//
//                filepath.putFile(imageURI)
//                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                        mProgressDialog.dismiss();
//
//                        Toast.makeText(getContext(), "Uploaded !! ", Toast.LENGTH_SHORT).show();
//
//                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//
//                                newCategory = new Category(txt_category.getText().toString(),uri.toString());
//
//                            }
//                        });
//
//
//                    }
//
//
//
//                })
//
//                       .addOnFailureListener(new OnFailureListener() {
//                           @Override
//                           public void onFailure(@NonNull Exception e) {
//                               mProgressDialog.dismiss();
//
//                               Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                           }
//                       })
//                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//
//                                double progres = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
//
//                                mProgressDialog.setMessage("Uploaded"+progres+"%");
//
//                            }
//                        });
//
//
//
//        }
//    }







//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == Gallery_Pick && resultCode == Activity.RESULT_OK && data!=null && data.getData() != null) {
//
//
//            imageURI = data.getData();
//
////            btnSelect.setText("Image Selected");
//
//
////            CropImage.activity(imageURI).setAspectRatio(1, 1).start(getContext(),HomeFragment.this);
//
//                }
//
//
////        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
////
////            CropImage.ActivityResult result = CropImage.getActivityResult(data);
////
////
////            if (resultCode == Activity.RESULT_OK) {
////
////
////
////
////
////                mProgressDialog.show();
////
////
////                Uri resultUri = result.getUri();
////
////
////
////                final String imageName = UUID.randomUUID().toString();
////
////                StorageReference filepath = imgStorageReference.child("Category_images").child(imageName + ".jpg");
////
////                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
////                    @Override
////                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
////
////                        if (task.isSuccessful()) {
////
//// imgStorageReference.child("Category_images").child(imageName+ ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
////                                @Override
////                                public void onSuccess(Uri uri) {
////
////                                    Toast.makeText(getContext(), "id", Toast.LENGTH_SHORT).show();
////
////
////
////                                    downloadUrl = uri.toString();
//////
////
////
////
//////                                    Toast.makeText(getContext(), "sss"+catTxt, Toast.LENGTH_SHORT).show();
////
//////                                    method1(downloadUrl);
////
//////
//////                                    hashMap.put("Category",catTxt);
//////                                    hashMap.put("imageURL",downloadUrl);
////
////
////
////
////
////
////
////
////
////
////
//////                                    HashMap<String,String> SubcatHashMap = new HashMap<>();
//////
//////                                    hashMap.put("Food Name",subcatName);
//////                                    hashMap.put("Description",subcatdescription);
//////                                    hashMap.put("Price",subcatprice);
//////                                    hashMap.put("Discount",subcatdiscount);
//////                                    hashMap.put("imageURL",downloadUrl);
//////                                    mRootRef.child("Category").push().setValue(SubcatHashMap);
////
////
////
////                                    hashMap.put("Image",downloadUrl);
////
////
////onResume();
////                                    method();
////
////                                    mProgressDialog.dismiss();
////
//////                                    Toast.makeText(getContext(), txt_category. getText().toString()+" Category Created ", Toast.LENGTH_SHORT).show();
////
////                                }
////
////                            });
////
////                        }
////
////                        else {
////
////                            Toast.makeText(getContext(), "Not Working", Toast.LENGTH_SHORT).show();
////                            mProgressDialog.dismiss();
////                        }
////
////                    }
////                });
////            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
////
////                Exception error = result.getError();
////            }
////
////        }
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//
//
//
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (mProgressDialog != null) {
//            mProgressDialog.dismiss();
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if ( mProgressDialog != null) {
//            mProgressDialog.dismiss();
//        }
//    }



    private void showCategory() {

            final DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Category");
        database.keepSynced(true);

        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(database, Category.class).build();

        adapter = new FirebaseRecyclerAdapter<Category, HomeFragment.UserViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull final HomeFragment.UserViewHolder holder, int position, @NonNull final Category category) {

                final String selected_user_id = getRef(position).getKey();


                database.child(selected_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChild("imageURL")){


//                            Category category1 = dataSnapshot.getChildren().getValue(Category.class);
//                           catName = category.getName();

                            final String userImage = dataSnapshot.child("imageURL").getValue().toString();
                            CategoryName = dataSnapshot.child("Category").getValue().toString();
//

//   String status = dataSnapshot.child("status").getValue().toString();



                            holder.uName.setText(CategoryName);

                            Picasso.get().load(userImage).placeholder(R.drawable.profile_avatar_small).into(holder.profileImg);

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
                                Intent profileIntent = new Intent(getContext(), SubCategoryActivity.class);
                                profileIntent.putExtra("selected_user_id",selected_user_id);
                                profileIntent.putExtra("CategoryName", String.valueOf(holder.uName.getText().toString()));

                                startActivity(profileIntent);
                            }
                        });



                    }



                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });




            }




            @NonNull
            @Override
            public HomeFragment.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);

                return new HomeFragment.UserViewHolder(view);

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
        else if (item.getTitle().equals(UPDATE)){

//            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(), (Category) adapter.getItem(item.getOrder()));

        }

        return super.onContextItemSelected(item);


    }

    private void DeleteDialog(String ref) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Category");

        database.child(ref).removeValue();


    }
//
//
//
//    private void showUpdateDialog(String ref, Category item) {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setTitle("Update Category Name : ");
//        builder.setMessage("Please fill full information");
//
//        LayoutInflater inflater = this.getLayoutInflater();
//        View custom_layout = inflater.inflate(R.layout.custom_dialog_category,null);
//
//        txt_category = custom_layout.findViewById(R.id.edit_text_cat);
//
//        txt_category.setText(item.getName());
//        builder.setView(custom_layout);
//
//        builder.setIcon(R.drawable.ic_shopping_cart_black_24dp);
//
//
//        builder.setPositiveButton("Select Image ", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//
//                String groupName = txt_category.getText().toString();
//
//                if (TextUtils.isEmpty(groupName)){
//
//                    Toast.makeText(getContext(), "Please Write Category Name", Toast.LENGTH_SHORT).show();
//                }
//
//                else {
//
//                    chooseImage();
//
//                }
//
//            }
//        });
//
//
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//                dialogInterface.cancel();
//            }
//        });
//
//
//
//        builder.show();
//
//
//    }


}
