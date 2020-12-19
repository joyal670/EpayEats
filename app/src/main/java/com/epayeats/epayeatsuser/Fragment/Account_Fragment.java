package com.epayeats.epayeatsuser.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.epayeats.epayeatsuser.Model.orderModel;
import com.epayeats.epayeatsuser.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class Account_Fragment extends Fragment
{
    TextView fragment_account_orders, fragment_account_orders_deliverd, fragment_account_orders_cancelled, fragment_account_orders_pending, fragment_account_orders_ongoing;
    SharedPreferences sharedPreferences;
    String a1, a2, a3;
    DatabaseReference databaseReference;
    List<orderModel> mCheckoutmodel;
    
    int count0 = 0;
    int count1 = 0;
    int count2 = 0;
    int count3 = 0;
    int cnt = 0;
    
    String status0 = "0";
    String status1 = "1";
    String status2 = "2";
    String status3 = "3";

    TextView fragment_account_name;
    CircleImageView fragment_account_image;

    public Account_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_, container, false);

        fragment_account_orders = view.findViewById(R.id.fragment_account_orders);
        fragment_account_orders_deliverd = view.findViewById(R.id.fragment_account_orders_deliverd);
        fragment_account_orders_cancelled = view.findViewById(R.id.fragment_account_orders_cancelled);
        fragment_account_orders_pending = view.findViewById(R.id.fragment_account_orders_pending);
        fragment_account_orders_ongoing = view.findViewById(R.id.fragment_account_orders_ongoing);
        fragment_account_name = view.findViewById(R.id.fragment_account_name);
        fragment_account_image = view.findViewById(R.id.fragment_account_image);

        sharedPreferences = getActivity().getSharedPreferences("data", 0);
        a1 = sharedPreferences.getString("userid", "");
        a2 = sharedPreferences.getString("useremail","");
        a3 = sharedPreferences.getString("displayimg", "");

        if(a3.equals(""))
        {
            fragment_account_image.setImageResource(R.drawable.ic_baseline_account_circle_24);
        }
        else
        {
            Picasso.get().load(a3).into(fragment_account_image);
        }


        fragment_account_name.setText(a2);

        databaseReference = FirebaseDatabase.getInstance().getReference("order_data");

        loadAllOrders();
        loadDeliverdOrders();
        loadCancelledOrders();
        loadPendingOrders();
        loadOnGoingOrders();

        return view;
    }

    private void loadOnGoingOrders()
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    if (a1.equals(dataSnapshot1.child("userID").getValue().toString()))
                    {
                        if(status0.equals(dataSnapshot1.child("orderStatus").getValue().toString()))
                        {
                            try {
                                cnt = cnt + 1;
                                fragment_account_orders_ongoing.setText(cnt + "");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadPendingOrders()
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    if (a1.equals(dataSnapshot1.child("userID").getValue().toString()))
                    {
                        if(status1.equals(dataSnapshot1.child("orderStatus").getValue().toString()))
                        {
                            try {
                                count1 = count1 + 1;
                                fragment_account_orders_pending.setText(count1 + "");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadCancelledOrders()
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    if (a1.equals(dataSnapshot1.child("userID").getValue().toString()))
                    {
                        if(status3.equals(dataSnapshot1.child("orderStatus").getValue().toString()))
                        {
                            try {
                                count3 = count3 + 1;
                                fragment_account_orders_cancelled.setText(count3 + "");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadDeliverdOrders()
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    if (a1.equals(dataSnapshot1.child("userID").getValue().toString()))
                    {
                        if(status2.equals(dataSnapshot1.child("orderStatus").getValue().toString()))
                        {
                            try {
                                count2 = count2 + 1;
                                fragment_account_orders_deliverd.setText(count2 + "");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadAllOrders()
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    if (a1.equals(dataSnapshot1.child("userID").getValue().toString()))
                    {
                        try {
                            count0 = count0 + 1;
                            fragment_account_orders.setText(count0 + "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}