package com.example.arshit.serversideecom.SideNavigation.Fragments.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arshit.serversideecom.Model.SubCategory;
import com.example.arshit.serversideecom.Model.User;
import com.example.arshit.serversideecom.R;
import com.example.arshit.serversideecom.SubCategoryActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class AllUserFragment extends Fragment {

    RecyclerView uRecyclerView;
    FirebaseRecyclerAdapter adapter;

    private DatabaseReference Rootref;
    String selected_user_id;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;

    private String currentUserId,UserName,UserId;

    private ProgressDialog mProgressDialog;
    private StorageReference imgStorageReference;

    View view;
    public AllUserFragment() {
     }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_all_user, container, false);

    intialise();
     return view;

    }

    private  void intialise(){

        uRecyclerView = view.findViewById(R.id.all_user_rv);
        GridLayoutManager glm = new GridLayoutManager(getActivity(), 1);
        uRecyclerView.setLayoutManager(glm);

    }

    @Override
    public void onResume() {
        super.onResume();

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users");
        database.keepSynced(true);

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(database, User.class).build();

        adapter = new FirebaseRecyclerAdapter<User, AllUserFragment.UsersViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final AllUserFragment.UsersViewHolder holder, int position, @NonNull final User user) {

         final String  selected_user_id = getRef(position).getKey();



                database.child(selected_user_id).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



//                        if (dataSnapshot.hasChild("imageURL")){
//
                            UserName = dataSnapshot.child("username").getValue().toString();
                            UserId = dataSnapshot.child("id").getValue().toString();


//
//
                            holder.uName.setText(UserName);


//                            final String userImage = dataSnapshot.child("imageSubCat").getValue().toString();
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


                        }
//                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent profileIntent = new Intent(getContext(), SubCategoryActivity.class);
                        profileIntent.putExtra("selected_user_id", selected_user_id);
                        startActivity(profileIntent);
                    }
                });

            }




            @NonNull
            @Override
            public AllUserFragment.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.see_all_user_item, parent, false);

                return new AllUserFragment.UsersViewHolder(view);

            }
        } ;


        uRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    public static class UsersViewHolder extends RecyclerView.ViewHolder   {
        public CircleImageView profileImg;
        TextView uName, uStatus;
        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            uName = mView.findViewById(R.id.all_username);
            profileImg = mView.findViewById(R.id.allUser__image);
        }

    }


}
