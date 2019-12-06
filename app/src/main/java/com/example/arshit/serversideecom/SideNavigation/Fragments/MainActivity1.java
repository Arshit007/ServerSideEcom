package com.example.arshit.serversideecom.SideNavigation.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.arshit.serversideecom.LogoutFragment;
import com.example.arshit.serversideecom.MainActivity;
import com.example.arshit.serversideecom.Model.Category;
import com.example.arshit.serversideecom.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.arshit.serversideecom.SideNavigation.Fragments.Fragment.AllOrderFragment;
import com.example.arshit.serversideecom.SideNavigation.Fragments.Fragment.AllUserFragment;
import com.example.arshit.serversideecom.SideNavigation.Fragments.Fragment.HomeFragment;
import com.example.arshit.serversideecom.SideNavigation.Fragments.Notification.Token;
import com.example.arshit.serversideecom.SignIn;
import com.example.arshit.serversideecom.SubCategoryActivity;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
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


public class MainActivity1 extends AppCompatActivity {



    private DatabaseReference Rootref;
    FirebaseAuth mAuth;
    private static final int GALLERY_PICK = 1;
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



    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    String catTxt;



    public static int navItemIndex = 0;

    private static final String TAG_HOME = "home";
    private static final String TAG_PHOTOS = "ALL USER";
    private static final String TAG_MOVIES = "ALL ORDERS";
    private static final String TAG_Logout = "Logout";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_HOME;
    HashMap<String,String> hashMap;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;
    String id;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

hashMap = new HashMap<>();
//        intialise();


        id = UUID.randomUUID().toString();
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        final DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference("Token");

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String deviceToken = instanceIdResult.getToken();
                  Token data = new Token(deviceToken,true);
                dbrf.child(currentUserId).setValue(data);

            }
        });







        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);

        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();



            }
        });

        loadNavHeader();

        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    private void showDialog() {


        AlertDialog.Builder  builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Category Name : ");
        builder.setMessage("Please fill full information");

        LayoutInflater  inflater = this.getLayoutInflater();

        View  custom_layout = inflater.inflate(R.layout.custom_dialog_category,null);


        txt_category = custom_layout.findViewById(R.id.edit_text_cat);


        btnSelect = custom_layout.findViewById(R.id.btnSelect);

        btnUpload = custom_layout.findViewById(R.id.btnUpload);




        builder.setView(custom_layout);

        builder.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        Rootref = FirebaseDatabase.getInstance().getReference("Category");


        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                catTxt = txt_category.getText().toString();
                chooseImage();



    }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK){

            Uri imageURI = data.getData();
            CropImage.activity(imageURI).setAspectRatio(1,1).start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {


                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setTitle("Saving Changes");
                mProgressDialog.setMessage("Wait untill changes are saved");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();


                Uri resultUri = result.getUri();



                StorageReference filepath = imgStorageReference.child("Profile_images").child(id+".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()){


                            imgStorageReference.child("Profile_images").child(id+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrl = uri.toString();

//                                    txt =  view.findViewById(R.id.txt);
//                                    String cat = txt.getText().toString();

                                    hashMap = new HashMap<>();
                                    hashMap.put("CategoryName",catTxt);
                                    hashMap.put("imageURL",downloadUrl);


                                    Rootref.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {

                                                mProgressDialog.dismiss();
//

                                                Toast.makeText(MainActivity1.this, " Image Updated", Toast.LENGTH_SHORT).show();

                                                onResume();
                                            }


                                        }
                                    });

                                }
                            });
                        }

//                           Toast.makeText(SettingsActivity.this, "Working", Toast.LENGTH_SHORT).show();
//                     }
                        else {

                            Toast.makeText(MainActivity1.this, "Not Working", Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                        }

                    }
                });
            }

            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }

        }
    }

    private void chooseImage(){

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);


    }


    private void loadNavHeader() {
        // name, website
        txtName.setText("Arshit Jain");


        // loading header background image
//        Glide.with(this).load(urlNavHeaderBg)
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imgNavHeaderBg);
//
//        // Loading profile image
//        Glide.with(this).load(urlProfileImg)
//                .crossFade()
//                .thumbnail(0.5f)
//                .bitmapTransform(new CircleTransform(this))
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imgProfile);

        // showing dot next to notifications label
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    private void loadHomeFragment() {
        selectNavMenu();

        setToolbarTitle();
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

              toggleFab();
            return;
        }

       Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:

                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                // photos
                AllUserFragment allUserFragment = new AllUserFragment();
                return allUserFragment;
            case 2:
                // movies fragment
                AllOrderFragment moviesFragment = new AllOrderFragment();
                return moviesFragment;
            case 3:
                // notifications fragment
                LogoutFragment notificationsFragment = new LogoutFragment();
                return notificationsFragment;
//
//            case 4:
//                // settings fragment
//                SettingsFragment settingsFragment = new SettingsFragment();
//                return settingsFragment;
            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_photos:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_PHOTOS;
                        break;
                    case R.id.nav_movies:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_MOVIES;
                        break;
                    case R.id.nav_logout:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_Logout;
                        break;
//                    case R.id.nav_settings:
//                        navItemIndex = 4;
//                        CURRENT_TAG = TAG_SETTINGS;
//                        break;
//                    case R.id.nav_about_us:
//                        // launch new intent instead of loading fragment
////                        startActivity(new Intent(MainActivity1.this, AboutUsActivity.class));
//                        drawer.closeDrawers();
//                        return true;
//                    case R.id.nav_privacy_policy:
//                        // launch new intent instead of loading fragment
////                        startActivity(new Intent(MainActivity1.this, PrivacyPolicyActivity.class));
//                        drawer.closeDrawers();
//                        return true;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }

        // when fragment is notifications, load the menu created for notifications
        if (navItemIndex == 3) {
//            getMenuInflater().inflate(R.menu.notifications, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Intent intent = new Intent(getApplicationContext(), SignIn.class);
            startActivity(intent);
            finish();
            return true;
        }

        // user is in notifications fragment
        // and selected 'Mark all as Read'
//        if (id == R.id.action_mark_all_read) {
//            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
//        }
//
//        // user is in notifications fragment
//        // and selected 'Clear All'
//        if (id == R.id.action_clear_notifications) {
//            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
//        }

        return super.onOptionsItemSelected(item);
    }

    // show or hide the fab
    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
    }



}
