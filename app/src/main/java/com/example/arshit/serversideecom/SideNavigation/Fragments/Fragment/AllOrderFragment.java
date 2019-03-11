package com.example.arshit.serversideecom.SideNavigation.Fragments.Fragment;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arshit.serversideecom.Model.Category;
import com.example.arshit.serversideecom.Model.Order;
import com.example.arshit.serversideecom.Model.User;
import com.example.arshit.serversideecom.R;
import com.example.arshit.serversideecom.SideNavigation.Fragments.ListenOrder;
import com.example.arshit.serversideecom.SideNavigation.Fragments.OrderDetail;
import com.example.arshit.serversideecom.SubCategoryActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.arshit.serversideecom.SideNavigation.Fragments.Fragment.HomeFragment.UPDATE;

public class AllOrderFragment extends Fragment {

    View view;
    RecyclerView order_rv;
    String orderContact,orderCity,orderCountry,orderAddress,orderName,orderAmount,orderId;
     String selected_user_id;
     LinearLayout cart_product_ll;

    RecyclerView uRecyclerView;
    FirebaseRecyclerAdapter adapter;

    public AllOrderFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_all_order, container, false);

        intialise();
        return view;


    }

    private void intialise(){
        cart_product_ll = view.findViewById(R.id.cart_product_ll);
        cart_product_ll.setVisibility(View.VISIBLE);

        order_rv = view.findViewById(R.id.see_order_recycler);

        GridLayoutManager glm = new GridLayoutManager(getActivity(), 1);

        order_rv.setLayoutManager(glm);

        Intent service = new Intent(getActivity(),ListenOrder.class);
        getActivity().startService(service);
        }

    @Override
    public void onResume() {
        super.onResume();

        cart_product_ll.setVisibility(View.VISIBLE);

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("Order");
        database.keepSynced(true);

        FirebaseRecyclerOptions<Order> options = new FirebaseRecyclerOptions.Builder<Order>()
                .setQuery(database, Order.class).build();

        adapter = new FirebaseRecyclerAdapter<Order, AllOrderFragment.UsersViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final AllOrderFragment.UsersViewHolder holder, int position, @NonNull final Order order) {

                selected_user_id = getRef(position).getKey();


                    database.child(selected_user_id).addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            cart_product_ll.setVisibility(View.INVISIBLE);

                      String id =  dataSnapshot.child("CurrentId").getValue().toString();

                        final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Users").child(id);

                        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                             orderName = String.valueOf(dataSnapshot.child("username").getValue());


                                holder.order_product_name.setText(orderName);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
//


                            orderId = String.valueOf(dataSnapshot.getKey());
                            orderAmount = dataSnapshot.child("TotalAmount").getValue().toString();
                            orderAddress = dataSnapshot.child("Address").getValue().toString();
                            orderCity = dataSnapshot.child("City").getValue().toString();
                            orderCountry = dataSnapshot.child("Country").getValue().toString();
                            orderContact = dataSnapshot.child("Contact").getValue().toString();

                            holder.order_product_id.setText(orderId);
                            holder.order_product_amount.setText(orderAmount);
                            holder.order_product_address.setText(orderAddress);
                            holder.order_product_city.setText(orderCity);
                            holder.order_product_country.setText(orderCountry);
                            holder.order_product_contact.setText(orderContact);


                        }
//                    }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    holder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent profileIntent = new Intent(getContext(), OrderDetail.class);
                            profileIntent.putExtra("selected_user_id", String.valueOf(holder.order_product_id.getText().toString()));

                            startActivity(profileIntent);
                        }
                    });

                }

            @NonNull
            @Override
            public AllOrderFragment.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.see_all_order_item, parent, false);

                return new AllOrderFragment.UsersViewHolder(view);

            }
        } ;


       order_rv.setAdapter(adapter);
        adapter.startListening();
    }


    public static class UsersViewHolder extends RecyclerView.ViewHolder  implements View.OnCreateContextMenuListener {

        TextView order_product_contact, order_product_city, order_product_country, order_product_address, order_product_name, order_product_amount, order_product_id;
        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            mView.setOnCreateContextMenuListener(this);

            order_product_contact = mView.findViewById(R.id.order_product_id);
            order_product_name = mView.findViewById(R.id.order_product_name);
            order_product_id = mView.findViewById(R.id.order_product_contact);
            order_product_address = mView.findViewById(R.id.order_product_address);
            order_product_city = mView.findViewById(R.id.order_product_city);
            order_product_country = mView.findViewById(R.id.order_product_country);
            order_product_amount = mView.findViewById(R.id.order_product_amount);


        }


        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select The Action");
            contextMenu.add(0, 0, getAdapterPosition(), "UPDATE");
            contextMenu.add(0, 0, getAdapterPosition(), "DELETE");

        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getTitle().equals("DELETE"))
        {

            DeleteDialog(adapter.getRef(item.getOrder()).getKey());
        }
//        else if (item.getTitle().equals(UPDATE)){
//
////            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(), (Category) adapter.getItem(item.getOrder()));
//
//        }

        return super.onContextItemSelected(item);


    }

    private void DeleteDialog(String ref) {


            DatabaseReference order = FirebaseDatabase.getInstance().getReference("Order");

        order.child(ref).removeValue();

        onStart();
        onResume();


        //            cart_product_ll.setVisibility(View.VISIBLE);

        }

    @Override
    public void onStart() {
        super.onStart();
    }


}




