package com.epayeats.epayeatsuser.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.epayeats.epayeatsuser.Adapter.orderAdapter;
import com.epayeats.epayeatsuser.Model.orderModel;
import com.epayeats.epayeatsuser.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.epayeats.epayeatsuser.Adapter.orderAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MyOrders_Fragment extends Fragment implements orderAdapter.OnitemClickListener
{
    RecyclerView AllOrdersRecyclerView;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    List<orderModel> mCheckoutmodel;
    orderAdapter orderAdapter;
    public ProgressDialog progressDialog;
    SwipeRefreshLayout refresh_my_orders;
    String a1;


    public MyOrders_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_orders_, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Your Orders...");

        AllOrdersRecyclerView = view.findViewById(R.id.AllOrdersRecyclerView);
        refresh_my_orders = view.findViewById(R.id.refresh_my_orders);


        sharedPreferences = this.getActivity().getSharedPreferences("data", 0);
        a1 = sharedPreferences.getString("userid", "");

        AllOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        AllOrdersRecyclerView.setHasFixedSize(true);

        refresh_my_orders.setRefreshing(true);
        refresh_my_orders.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadOrders();
            }
        });

        loadOrders();

        return view;
    }

    private void loadOrders()
    {
        refresh_my_orders.setRefreshing(false);
        databaseReference = FirebaseDatabase.getInstance().getReference("order_data");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mCheckoutmodel.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    if (a1.equals(dataSnapshot1.child("userID").getValue().toString())) {
                        orderModel model = dataSnapshot1.getValue(orderModel.class);
                        mCheckoutmodel.add(model);
                    }

                }
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        mCheckoutmodel = new ArrayList<>();
        orderAdapter = new orderAdapter(getContext(), mCheckoutmodel);
        AllOrdersRecyclerView.setAdapter(orderAdapter);
        orderAdapter.setOnClickListener(MyOrders_Fragment.this);

    }

    @Override
    public void onItemClick(int position)
    {
        String[] items = {"Cancel order"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Select Options");
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(which == 0)
                {
                    String sta = "0";
                    String sta1 = "1";
                    String sta2 = "2";
                    String sta3 = "3";

                    if(sta.equals(mCheckoutmodel.get(position).getOrderStatus()))
                    {

                        SweetAlertDialog dialog1 = new SweetAlertDialog(Objects.requireNonNull(getContext()), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Are you sure?")
                                .setContentText("Won't be able to undone!")
                                .setConfirmText("Yes, cancel order!")
                                .setCancelText("No, cancel please")
                                .showCancelButton(true)
                                .setCancelClickListener(sweetAlertDialog -> sweetAlertDialog.cancel())
                                .setConfirmClickListener(sweetAlertDialog -> {
                                    sweetAlertDialog.setTitleText("Cancelled")
                                            .setContentText("Your order has been cancelled")
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(null)
                                            .showCancelButton(false)
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);


                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("order_data").child(mCheckoutmodel.get(position).getOrderID());
                                    reference.child("orderStatus").setValue("3");
                                });
                        dialog1.show();



                    }
                    else if(sta1.equals(mCheckoutmodel.get(position).getOrderStatus()))
                    {
                        Toast.makeText(getContext(), "The Delivery is on the way", Toast.LENGTH_SHORT).show();
                    }
                    else if(sta2.equals(mCheckoutmodel.get(position).getOrderStatus()))
                    {
                        Toast.makeText(getContext(), "Order is already delivered", Toast.LENGTH_SHORT).show();
                    }
                    else if(sta3.equals(mCheckoutmodel.get(position).getOrderStatus()))
                    {
                        Toast.makeText(getContext(), "The order is already cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        dialog.create().show();
    }
}