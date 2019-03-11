package com.example.arshit.serversideecom.SideNavigation.Fragments;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arshit.serversideecom.Model.Order;
import com.example.arshit.serversideecom.Model.User;
import com.example.arshit.serversideecom.R;
import com.example.arshit.serversideecom.SideNavigation.Fragments.Fragment.AllOrderFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderDetail extends AppCompatActivity {

    RecyclerView order_detail_rv;
    String orderName,orderQty,orderPrice,OrderId;

Intent intent;
    RecyclerView uRecyclerView;
    FirebaseRecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        intialise();
    }

    private void intialise(){

        order_detail_rv = findViewById(R.id.see_order_detail);
        GridLayoutManager glm = new GridLayoutManager(OrderDetail.this, 1);
        order_detail_rv.setLayoutManager(glm);

        intent= getIntent();
        OrderId =  intent.getStringExtra("selected_user_id");

//        Toast.makeText(this, OrderId, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResume() {
        super.onResume();

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("Order").child(OrderId).child("products");

        database.keepSynced(true);

        FirebaseRecyclerOptions<Order> options = new FirebaseRecyclerOptions.Builder<Order>()
                .setQuery(database, Order.class).build();

        adapter = new FirebaseRecyclerAdapter<Order, OrderDetail.UsersViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final OrderDetail.UsersViewHolder holder, int position, @NonNull final Order order) {

                final String  selected_user_id = getRef(position).getKey();


                database.child(selected_user_id).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        orderPrice = dataSnapshot.child("Price").getValue().toString();
                        orderName = dataSnapshot.child("ProductName").getValue().toString();
                        orderQty = dataSnapshot.child("ProductQuantity").getValue().toString();



                        holder.order_detail_name.setText(orderName);
                        holder.order_detail_product_price.setText(orderPrice);
                        holder.order_detail_product_qty.setText(orderQty);
                    }
//                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




//                holder.mView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent profileIntent = new Intent(getContext(), OrderDetail.class);
//                        profileIntent.putExtra("selected_user_id", orderId);
//                        startActivity(profileIntent);
//                    }
//                });

            }




            @NonNull
            @Override
            public OrderDetail.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.see_all_order_detail_item, parent, false);

                return new OrderDetail.UsersViewHolder(view);

            }
        } ;


        order_detail_rv.setAdapter(adapter);
        adapter.startListening();
    }


    public static class UsersViewHolder extends RecyclerView.ViewHolder   {

        TextView order_detail_name,order_detail_product_qty,order_detail_product_price;
        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            order_detail_name = mView.findViewById(R.id.order_detail_product_name);
            order_detail_product_qty = mView.findViewById(R.id.order_detail_product_qty);
            order_detail_product_price = mView.findViewById(R.id.order_detail_product_price);



        }

    }







}
