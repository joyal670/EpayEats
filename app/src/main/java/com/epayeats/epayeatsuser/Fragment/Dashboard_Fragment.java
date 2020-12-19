package com.epayeats.epayeatsuser.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.epayeats.epayeatsuser.Maps.ChangeLocation_MapsActivity;
import com.epayeats.epayeatsuser.Activity.HomeMainCatagory_Activity;
import com.epayeats.epayeatsuser.Activity.HomeSubMenuCatagory_Activity;
import com.epayeats.epayeatsuser.Activity.ShopSelected_Activity;
import com.epayeats.epayeatsuser.Adapter.FoodCatagoryHomeAdapter;
import com.epayeats.epayeatsuser.Adapter.RestaurantHomeAdapter;
import com.epayeats.epayeatsuser.Adapter.SubCatagoryHomeAdapter;
import com.epayeats.epayeatsuser.Adapter.imageSliderAdapter;
import com.epayeats.epayeatsuser.Interface.MainCatagoryInterface;
import com.epayeats.epayeatsuser.Interface.SubCatagoryInterface;
import com.epayeats.epayeatsuser.Model.FoodCatagoryModel;
import com.epayeats.epayeatsuser.Model.RestaurantModel;
import com.epayeats.epayeatsuser.Model.SubCatagoryModel;
import com.epayeats.epayeatsuser.Model.imageSliderModel;
import com.epayeats.epayeatsuser.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;


public class Dashboard_Fragment extends Fragment implements RestaurantHomeAdapter.OnitemClickListener, MainCatagoryInterface, SubCatagoryInterface
{
    SliderView sliderView;
    List<imageSliderModel> imageSliderModelList;
    TextView fragment_dashboard_location;
    imageSliderAdapter imageSliderAdapter;

    String user_location;
    String user_lat;
    String user_long;
    String user_KM;

    String rest_KM;

    SharedPreferences sharedPreferences;

    RecyclerView fragment_dashboard_recyclerview;
    DatabaseReference mRestaurantReference;
    List<RestaurantModel> mRestuarantModel;
    RestaurantHomeAdapter mRestaurantAdapter;



    RecyclerView fragment_dashboard_main_menu_recyclerview;
    DatabaseReference mMainCatagoreyReference;
    List<FoodCatagoryModel> mMainCatagoryModel;
    FoodCatagoryHomeAdapter mMainCatagoryAdapter;
    public static MainCatagoryInterface mainCatagoryInterface;


    RecyclerView fragment_dashboard_sub_menu_recyclerview;
    DatabaseReference mSubCatagoryReference;
    List<SubCatagoryModel> mSubCatagoryModel;
    SubCatagoryHomeAdapter mSubCatagoryAdapter;
    public static SubCatagoryInterface subCatagoryInterface;

    public ProgressDialog progressDialog;



    public Dashboard_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard_, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        sharedPreferences = getContext().getSharedPreferences("data", 0);
        user_location = sharedPreferences.getString("location_name", "");
        user_lat = sharedPreferences.getString("lat","");
        user_long = sharedPreferences.getString("lot","");


        // main catagory recyclerview
        fragment_dashboard_main_menu_recyclerview = view.findViewById(R.id.fragment_dashboard_main_menu_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        fragment_dashboard_main_menu_recyclerview.setLayoutManager(layoutManager);
        fragment_dashboard_main_menu_recyclerview.setHasFixedSize(true);
        mainCatagoryInterface = this;

        // sub catagory recycerview
        fragment_dashboard_sub_menu_recyclerview = view.findViewById(R.id.fragment_dashboard_sub_menu_recyclerview);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        fragment_dashboard_sub_menu_recyclerview.setLayoutManager(layoutManager1);
        fragment_dashboard_sub_menu_recyclerview.setHasFixedSize(true);
        subCatagoryInterface = this;

        // toolbar setup
        Toolbar toolbar = view.findViewById(R.id.toolbarfrg);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_baseline_location_on_24);
        toolbar.setTitle(user_location);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(), ChangeLocation_MapsActivity.class);
                startActivity(intent);
            }
        });

//        // collasping toolbar setup
//        CollapsingToolbarLayout toolbarLayout = view.findViewById(R.id.toolbar_layout);
//        toolbarLayout.setTitle(user_location);
//        toolbarLayout.setCollapsedTitleTextColor(Color.BLACK);
//        toolbarLayout.setExpandedTitleColor(Color.BLACK);
//        toolbarLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), ChangeLocation_MapsActivity.class);
//                startActivity(intent);
//            }
//        });


        // image slider setup
//        imageSliderModelList = new ArrayList<>();
        sliderView = view.findViewById(R.id.imageSlider);
//        imageSliderModelList.add(new imageSliderModel(R.drawable.img1));
//        imageSliderModelList.add(new imageSliderModel(R.drawable.img2));
//        imageSliderModelList.add(new imageSliderModel(R.drawable.img3));

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("banner_images");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                imageSliderModelList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    String temp1 = snapshot1.child("lat").getValue().toString();
                    String temp2 = snapshot1.child("lon").getValue().toString();

                    Double km = distance(Double.parseDouble(user_lat), Double.parseDouble(user_long), Double.parseDouble(temp1), Double.parseDouble(temp2));

                    String roKm = String.valueOf(Math.round(km));

                    int temproKm = Integer.parseInt(roKm);

                    if(temproKm <= 10)
                    {
                        imageSliderModel model = snapshot1.getValue(imageSliderModel.class);
                        imageSliderModelList.add(model);
                    }

                }
                imageSliderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        imageSliderModelList = new ArrayList<>();
        imageSliderAdapter = new imageSliderAdapter(getContext(), imageSliderModelList);
        sliderView.setSliderAdapter(imageSliderAdapter);
        sliderView.startAutoCycle();

//        sliderView.setSliderAdapter(new imageSliderAdapter(getContext(),imageSliderModelList));
//        sliderView.startAutoCycle();


        fragment_dashboard_recyclerview = view.findViewById(R.id.fragment_dashboard_recyclerview);
        fragment_dashboard_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        fragment_dashboard_recyclerview.setHasFixedSize(true);


        loadNearByRestaurants();
        loadMainMenu();
        loadSubMenu();

        return view;
    }


    private void loadSubMenu()
    {
        progressDialog.show();
        mSubCatagoryReference = FirebaseDatabase.getInstance().getReference("sub_category");
        mSubCatagoryReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mSubCatagoryModel.clear();
                progressDialog.dismiss();
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    SubCatagoryModel model = snapshot1.getValue(SubCatagoryModel.class);
                    mSubCatagoryModel.add(model);
                }
                mSubCatagoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                progressDialog.dismiss();
                Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        mSubCatagoryModel = new ArrayList<>();
        mSubCatagoryAdapter = new SubCatagoryHomeAdapter(getContext(), mSubCatagoryModel);
        fragment_dashboard_sub_menu_recyclerview.setAdapter(mSubCatagoryAdapter);
    }

    private void loadMainMenu()
    {
        progressDialog.show();
        mMainCatagoreyReference = FirebaseDatabase.getInstance().getReference("main_catagory");
        mMainCatagoreyReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                progressDialog.dismiss();
                mMainCatagoryModel.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    FoodCatagoryModel model = snapshot1.getValue(FoodCatagoryModel.class);
                    mMainCatagoryModel.add(model);
                }
                mMainCatagoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                progressDialog.dismiss();
                Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        mMainCatagoryModel = new ArrayList<>();
        mMainCatagoryAdapter = new FoodCatagoryHomeAdapter(getContext(), mMainCatagoryModel);
        fragment_dashboard_main_menu_recyclerview.setAdapter(mMainCatagoryAdapter);

    }

    private void loadNearByRestaurants()
    {
        progressDialog.show();
        mRestaurantReference = FirebaseDatabase.getInstance().getReference("restaurants");
        mRestaurantReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                mRestuarantModel.clear();
                progressDialog.dismiss();
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren())
                {
                    String rest_lat;
                    String rest_long;

                    rest_lat = dataSnapshot1.child("lat").getValue().toString();
                    rest_long = dataSnapshot1.child("lon").getValue().toString();

                    String status = dataSnapshot1.child("isBlocked").getValue().toString();

                    Double km = distance(Double.parseDouble(user_lat), Double.parseDouble(user_long), Double.parseDouble(rest_lat), Double.parseDouble(rest_long));

                    String roKm = String.valueOf(Math.round(km));

                    int temproKm = Integer.parseInt(roKm);

                    if(temproKm <= 10)
                    {
                        if("UnBlocked".equals(status))
                        {
                            RestaurantModel model = dataSnapshot1.getValue(RestaurantModel.class);
                            mRestuarantModel.add(model);
                        }

                    }
                }
                mRestaurantAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                progressDialog.dismiss();
                Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        mRestuarantModel = new ArrayList<>();
        mRestaurantAdapter = new RestaurantHomeAdapter(getContext(), mRestuarantModel);
        fragment_dashboard_recyclerview.setAdapter(mRestaurantAdapter);
        mRestaurantAdapter.setOnClickListener(Dashboard_Fragment.this);

    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        double km = dist / 0.62137;

//       String s = String.format("%.2f", km);
//        String a = String.valueOf(Math.round(km));

        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public void onItemClick(int position)
    {
        Intent intent = new Intent(getContext(), ShopSelected_Activity.class);
        intent.putExtra("resID", mRestuarantModel.get(position).getResID());
        intent.putExtra("resName", mRestuarantModel.get(position).getResName());
        intent.putExtra("resPhone", mRestuarantModel.get(position).getResPhone());
        intent.putExtra("resPhoto", mRestuarantModel.get(position).getResPhoto());

        intent.putExtra("resLocation", mRestuarantModel.get(position).getResLocation());
        intent.putExtra("lat", mRestuarantModel.get(position).getLat());
        intent.putExtra("lon", mRestuarantModel.get(position).getLon());

        intent.putExtra("resLocalAdminID", mRestuarantModel.get(position).getResLocalAdminID());
        intent.putExtra("resLocalAdminName", mRestuarantModel.get(position).getResLocalAdminName());
        intent.putExtra("resOpenTime", mRestuarantModel.get(position).getResOpenTime());
        intent.putExtra("resCloseTime", mRestuarantModel.get(position).getResCloseTime());

        intent.putExtra("isShopClosed", mRestuarantModel.get(position).getIsShopClosed());
        intent.putExtra("resLicenceNo", mRestuarantModel.get(position).getResLicenceNo());

        startActivity(intent);

    }

    @Override
    public void maincatgory(int position, String name)
    {
        Intent intent = new Intent(getContext(), HomeMainCatagory_Activity.class);
        intent.putExtra("position", position);
        intent.putExtra("name", name);
        intent.putExtra("foodCatagoreyID", mMainCatagoryModel.get(position).getFoodCatagoreyID());
        intent.putExtra("foodCatagoreyType", mMainCatagoryModel.get(position).getFoodCatagoreyType());
        startActivity(intent);
    }

    @Override
    public void subcatagory(int position, String name)
    {
        Intent intent = new Intent(getContext(), HomeSubMenuCatagory_Activity.class);
        intent.putExtra("position", position);
        intent.putExtra("name", name);
        intent.putExtra("subCatagoryID", mSubCatagoryModel.get(position).getSubCatagoryID());
        intent.putExtra("subCatagoryName", mSubCatagoryModel.get(position).getSubCatagoryName());
        intent.putExtra("mainCatagoryName", mSubCatagoryModel.get(position).getMainCategoryName());
        intent.putExtra("mainCatagoryID", mSubCatagoryModel.get(position).getMainCategoryID());
        startActivity(intent);
    }
}