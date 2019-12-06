package com.example.arshit.serversideecom;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arshit.serversideecom.Model.Category;
import com.example.arshit.serversideecom.Model.SubCategory;
import com.example.arshit.serversideecom.SideNavigation.Fragments.MainActivity1;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class SubCategoryActivity extends AppCompatActivity {


    FirebaseRecyclerAdapter adapter;
    private DatabaseReference Rootref;
    String selected_user_id;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    private String currentUserId;
    EditText txt_category,edit_text_sub_cat_description, edit_text_price_discount, edit_food_cat, edit_text_price_subcat;
    Button btnSelect, btnUpload;
    private static int Gallery_Pick = 1;
    private ProgressDialog mProgressDialog;
    private StorageReference imgStorageReference;
    Uri imageURI;
    RecyclerView recycler_sub_category;
    String subcatdiscount, subcatName, subcatdescription, subcatprice,CategoryName;

    Button btn_category, btn_subcat;
    Intent intent;
    String CategoryId;
    String FoodName;
    FloatingActionButton addSubbtn;
    View custom_layout;

    TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);


        intialise();
        toolbar();
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

    private void intialise() {


        addSubbtn = findViewById(R.id.btnAddFloat);


        intent= getIntent();
        CategoryId =  intent.getStringExtra("selected_user_id");

        CategoryName = intent.getStringExtra("CategoryName");



        addSubbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getApplicationContext(),AddSubCategoryActivity.class);
                intent.putExtra("selected_user_id",CategoryId);
                intent.putExtra("CategoryName",CategoryName);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });


        imgStorageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        Rootref = FirebaseDatabase.getInstance().getReference();


        recycler_sub_category = findViewById(R.id.recycler_sub_category);
        GridLayoutManager glm = new GridLayoutManager(getApplicationContext(), 1);
        recycler_sub_category.setLayoutManager(glm);

        showSubCategory();

   }



//        private void new_subCategory(){
//
//        {
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(SubCategoryActivity.this);
//            builder.setTitle("Enter Subcategory Detail : ");
//            builder.setMessage("Please fill full information");
//
//            LayoutInflater inflater = this.getLayoutInflater();
//             custom_layout = inflater.inflate(R.layout.custom_dialog_subcategory,null);
//
//
//            builder.setView(custom_layout);
//
//            builder.setIcon(R.drawable.ic_shopping_cart_black_24dp);
//
//
//            builder.setPositiveButton("Select Image ", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//
//                    edit_food_cat = custom_layout.findViewById(R.id.edit_food_cat);
//                    edit_text_sub_cat_description = custom_layout.findViewById(R.id.edit_text_sub_cat);
//                    edit_text_price_subcat = custom_layout.findViewById(R.id.edit_text_price_subcat);
//                    edit_text_price_discount = custom_layout.findViewById(R.id.edit_text_price_discount);
//
//
//
//                    subcatName = edit_food_cat.getText().toString();
//
//                    subcatdescription = edit_text_sub_cat_description.getText().toString();
//
//                    subcatprice = edit_text_price_subcat.getText().toString();
//
//                    subcatdiscount = edit_text_price_discount.getText().toString();
//
//                    if (TextUtils.isEmpty(subcatName) && TextUtils.isEmpty(subcatdescription)
//                            && TextUtils.isEmpty(subcatprice) && TextUtils.isEmpty(subcatdescription)){
//
//                        Toast.makeText(SubCategoryActivity.this, "Enter All the Detail", Toast.LENGTH_SHORT).show();
//                    }
//
//                    else {
//
//                        chooseImage();
//
//                    }
//
//                }
//            });
//
//
//            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//
//                    dialogInterface.cancel();
//                }
//            });
//
//
//
//            builder.show();
//        }
//
//
//
//    }




private  void  showSubCategory(){

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("SubCategory").child(CategoryId);
        database.keepSynced(true);

        FirebaseRecyclerOptions<SubCategory> options = new FirebaseRecyclerOptions.Builder<SubCategory>()
                .setQuery(database, SubCategory.class).build();

adapter = new FirebaseRecyclerAdapter<SubCategory, SubCategoryActivity.UsersViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final SubCategoryActivity.UsersViewHolder holder, int position, @NonNull final SubCategory subCategory) {

                  selected_user_id = getRef(position).getKey();


//                Toast.makeText(SubCategoryActivity.this, "selected id "+ selected_user_id, Toast.LENGTH_SHORT).show();
//                Toast.makeText(SubCategoryActivity.this, "Category Id " + CategoryId, Toast.LENGTH_SHORT).show();
//
//                Toast.makeText(SubCategoryActivity.this," Value - "+(CharSequence) database.child(CategoryId).child(selected_user_id), Toast.LENGTH_SHORT).show();

//                database.child(selected_user_id).orderByChild("MenuId").equalTo(0);

                database.child(selected_user_id).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                        if (dataSnapshot.hasChild("imageSubCat")){
//
                            FoodName = dataSnapshot.child("Food Name").getValue().toString();
//
                            holder.uName.setText(FoodName);


                            final String userImage = dataSnapshot.child("imageSubCat").getValue().toString();

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

//                            if (CategoryId.equals(dataSnapshot.hasChild("MenuId")))
//                                   {
//
////                            SubCategory subCategory1 = dataSnapshot.getValue(SubCategory.class);
////                            holder.uName.setText(subCategory1.getDescription());
//
//                            Toast.makeText(SubCategoryActivity.this, "working inside ", Toast.LENGTH_SHORT).show();
//
////                            final String userImage = dataSnapshot.child("imageSubCat").getValue().toString();
//                             FoodName = dataSnapshot.child("Food Name").getValue().toString();
////
//                             holder.uName.setText(FoodName);
//
//                           toolbar_title.setText(CategoryName);
//
//                            Picasso.get().load(userImage).networkPolicy(NetworkPolicy.OFFLINE)
//                                    .placeholder(R.drawable.profile_avatar_small).into(holder.profileImg, new Callback() {
//                                @Override
//                                public void onSuccess() {
//
//                                }
//
//                                @Override
//                                public void onError(Exception e) {
//
//                                    Picasso.get().load(userImage).placeholder(R.drawable.profile_avatar_small).into(holder.profileImg);
//                                }
//                            });
//
//
//                        }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




//                holder.mView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent profileIntent = new Intent(MainActivity.this, SubCategoryActivity.class);
//                        profileIntent.putExtra("selected_user_id", selected_user_id);
//                        startActivity(profileIntent);
//                    }
//                });

            }




            @NonNull
            @Override
            public SubCategoryActivity.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);

                return new SubCategoryActivity.UsersViewHolder(view);

            }

        } ;


   recycler_sub_category.setAdapter(adapter);
        adapter.startListening();
    }


    public static class UsersViewHolder extends RecyclerView.ViewHolder  implements View.OnCreateContextMenuListener{
        public CircleImageView profileImg;

        TextView uName,uStatus;
        View  mView;

        public UsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mView.setOnCreateContextMenuListener(this);

            uName = mView.findViewById(R.id.category_name);
            profileImg = mView.findViewById(R.id.category_image);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select The Action");
            contextMenu.add(0,0,getAdapterPosition(),"DELETE");
        }






    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals("DELETE"))
        {

            DeleteDialog(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void DeleteDialog(String ref) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("SubCategory").child(CategoryId);

        database.child(ref).removeValue();

    }



}


