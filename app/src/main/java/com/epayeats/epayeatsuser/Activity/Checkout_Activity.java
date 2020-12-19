package com.epayeats.epayeatsuser.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.epayeats.epayeatsuser.Database.MobUser;
import com.epayeats.epayeatsuser.Model.orderModel;
import com.epayeats.epayeatsuser.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Checkout_Activity extends AppCompatActivity
{
    TextView checkout_activity_your_location;
    TextView checkout_activity_address1;
    TextView checkout_activity_address2;
    TextView checkout_activity_city;
    TextView checkout_activity_pincode;
    TextView checkout_activity_contact_name;
    TextView checkout_activity_phone_number;
    TextView checkout_activity_final_price;
    TextView checkout_activity_alt_phone_number;

    Button checkout_activity_change_address_btn;;
    Button checkout_activity_place_order_btn;

    SharedPreferences sharedPreferences;
    String user_ID;
    String user_location;
    String user_lat, user_long;

    int tempId,tempQty;
    float tempPrice,total;

    orderModel orderModel;
    List<MobUser> UserList;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_);

        sharedPreferences = getSharedPreferences("data", 0);
        user_ID = sharedPreferences.getString("userid","");
        user_location = sharedPreferences.getString("location_name", "");
        user_lat = sharedPreferences.getString("lat","");
        user_long = sharedPreferences.getString("lot","");


        checkout_activity_your_location = findViewById(R.id.checkout_activity_your_location);
        checkout_activity_address1 = findViewById(R.id.checkout_activity_address1);
        checkout_activity_address2 = findViewById(R.id.checkout_activity_address2);
        checkout_activity_city = findViewById(R.id.checkout_activity_city);
        checkout_activity_pincode = findViewById(R.id.checkout_activity_pincode);
        checkout_activity_contact_name = findViewById(R.id.checkout_activity_contact_name);
        checkout_activity_phone_number = findViewById(R.id.checkout_activity_phone_number);
        checkout_activity_final_price = findViewById(R.id.checkout_activity_final_price);
        checkout_activity_change_address_btn = findViewById(R.id.checkout_activity_change_address_btn);
        checkout_activity_place_order_btn = findViewById(R.id.checkout_activity_place_order_btn);
        checkout_activity_alt_phone_number = findViewById(R.id.checkout_activity_alt_phone_number);

        checkout_activity_your_location.setText(user_location);
        
        checkout_activity_change_address_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) 
            {
                addAddress();
            }
        });
        
        checkout_activity_place_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = checkout_activity_address1.getText().toString();
                if(temp.isEmpty())
                {
                    Toast.makeText(Checkout_Activity.this, "Please add a delivery Address", Toast.LENGTH_SHORT).show();
                }
                else 
                {
                    finalCheckout();
                }
            }
        });

        loadTotalCoast();

    }

    private void loadTotalCoast()
    {
        List<MobUser> UserCheckout = SQLite.select().from(MobUser.class).queryList();
        for (MobUser mu : UserCheckout)
        {
            tempQty = 0;
            tempPrice = 0;
            try {
                tempId = mu.getId();
                tempQty = tempQty + mu.getQty();
                tempPrice = tempPrice + Float.parseFloat(mu.getMenuOfferPrice());
                float p = tempPrice * tempQty;
                total = total + p;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        checkout_activity_final_price.setText(total + "");
    }

    private void finalCheckout()
    {
        reference = FirebaseDatabase.getInstance().getReference("order_data");


        orderModel = new orderModel();

        UserList = SQLite.select().from(MobUser.class).queryList();
        List<MobUser> UserCheckout = SQLite.select().from(MobUser.class).queryList();
        for (MobUser mu : UserCheckout)
        {
            orderModel.setMenuID(mu.getMenuID());
            orderModel.setMenuName(mu.getMenuName());
            orderModel.setMenuImage(mu.getImage1());

            orderModel.setMainCatagoryID(mu.getMenuMainCatagoreyID());
            orderModel.setMainCatagoryName(mu.getMenuMainCatagoreyName());

            orderModel.setSubCatagoryID(mu.getMenuSubCatagoreyID());
            orderModel.setSubCatagoryName(mu.getMenuSubCatagoreyName());

            orderModel.setLocalAdminID(mu.getMenuLocalAdminID());

            orderModel.setOfferPrice(mu.getMenuOfferPrice());
            orderModel.setSellingPrice(mu.getMenuSellingPrice());
            orderModel.setActualPrice(mu.getMenuActualPrice());

            orderModel.setRestID(mu.getRestID());
            orderModel.setRestName(mu.getRestName());

            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            orderModel.setOrderDate(date);

            String time = new SimpleDateFormat("hh-mm-a", Locale.getDefault()).format(new Date());
            orderModel.setOrderTime(time);

            orderModel.setQty(String.valueOf(mu.getQty()));

            orderModel.setTotalPrice(total + "");

            orderModel.setHouse(checkout_activity_address1.getText().toString());
            orderModel.setArea(checkout_activity_address2.getText().toString());
            orderModel.setCity(checkout_activity_city.getText().toString());
            orderModel.setPincode(checkout_activity_pincode.getText().toString());
            orderModel.setcName(checkout_activity_contact_name.getText().toString());
            orderModel.setcPhone(checkout_activity_phone_number.getText().toString());
            orderModel.setcAltPhone(checkout_activity_alt_phone_number.getText().toString());

            orderModel.setOrderStatus("0");
            orderModel.setDeliveryBodID("");
            orderModel.setDeliveryBoyName("");
            orderModel.setDeliveryDate("");
            orderModel.setDeliveryTime("");

            orderModel.setUserID(user_ID);
            orderModel.setUserLocation(user_location);
            orderModel.setUserLatitude(user_lat);
            orderModel.setUserLongitude(user_long);

            orderModel.setTemp1("");
            orderModel.setTemp2("");
            orderModel.setTemp3("");

            String pushkey = reference.push().getKey();
            orderModel.setOrderID(pushkey);
            reference.child(pushkey).setValue(orderModel);
        }

        Intent intent = new Intent(Checkout_Activity.this, OrderPlaced_Activity.class);
        startActivity(intent);
        finish();


    }

    private void addAddress()
    {
        LayoutInflater li = LayoutInflater.from(Checkout_Activity.this);
        View addAddress = li.inflate(R.layout.prompt, null);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setView(addAddress);

        EditText newaddline1;
        EditText newaddline2;
        EditText newaddcity;
        EditText newaddpincode;
        EditText newaddname;
        EditText newaddnumber;
        EditText newaddalternatenumber;
        Button newaddcancelbtn;
        Button newaddsavebtn;
        CheckBox usePrevious_address_checkbox;

        newaddline1 = addAddress.findViewById(R.id.newaddline1);
        newaddline2 = addAddress.findViewById(R.id.newaddline2);
        newaddcity = addAddress.findViewById(R.id.newaddcity);
        newaddpincode = addAddress.findViewById(R.id.newaddpincode);
        newaddname = addAddress.findViewById(R.id.newaddname);
        newaddnumber = addAddress.findViewById(R.id.newaddnumber);
        newaddalternatenumber = addAddress.findViewById(R.id.newaddalternatenumber);
        newaddcancelbtn = addAddress.findViewById(R.id.newaddcancelbtn);
        newaddsavebtn = addAddress.findViewById(R.id.newaddsavebtn);
        usePrevious_address_checkbox = addAddress.findViewById(R.id.usePrevious_address_checkbox);

        final AlertDialog alertDialog = alertBuilder.create();

        newaddsavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String a1, a2, ac, ap, ana, anu, aanum;
                a1 = newaddline1.getText().toString();
                a2 = newaddline2.getText().toString();
                ac = newaddcity.getText().toString();
                ap = newaddpincode.getText().toString();
                ana = newaddname.getText().toString();
                anu = newaddnumber.getText().toString();
                aanum = newaddalternatenumber.getText().toString();

                if (a1.isEmpty() || a2.isEmpty() || ac.isEmpty()  || ap.isEmpty() || ana.isEmpty() || anu.isEmpty()) {
                    if (a1.isEmpty()) {
                        newaddline1.setError("House No or Building Name Required");
                    }

                    if (a2.isEmpty()) {
                        newaddline2.setError("Road Name, Area, Colony Required");
                    }

                    if (ac.isEmpty()) {
                        newaddcity.setError("City Required");
                    }

                    if (ap.isEmpty()) {
                        newaddpincode.setError("Pincode Required");
                    }

                    if (ana.isEmpty()) {
                        newaddname.setError("Name Required");
                    }

                    if (anu.isEmpty()) {
                        newaddnumber.setError("Number Required");
                    }

                }
                else
                {
                    checkout_activity_address1.setText(a1);
                    checkout_activity_address2.setText(a2);
                    checkout_activity_city.setText(ac);
                    checkout_activity_pincode.setText(ap);
                    checkout_activity_contact_name.setText(ana);
                    checkout_activity_phone_number.setText(anu);
                    checkout_activity_alt_phone_number.setText(aanum);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("house_number", a1);
                    editor.putString("area", a2);
                    editor.putString("city", ac);
                    editor.putString("pincode", ap);
                    editor.putString("cName", ana);
                    editor.putString("cNumber", anu);
                    editor.putString("cAltNumber", aanum);
                    editor.apply();

                    alertDialog.dismiss();
                }
            }
        });

        newaddcancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        usePrevious_address_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    String temp1;
                    String temp2;
                    String temp3;
                    String temp4;
                    String temp5;
                    String temp6;
                    String temp7;

                    temp1 = sharedPreferences.getString("house_number", "");
                    temp2 = sharedPreferences.getString("area", "");
                    temp3 = sharedPreferences.getString("city", "");
                    temp4 = sharedPreferences.getString("pincode", "");
                    temp5 = sharedPreferences.getString("cName", "");
                    temp6 = sharedPreferences.getString("cNumber", "");
                    temp7 = sharedPreferences.getString("cAltNumber", "");

                    if(temp1.equals(""))
                    {
                        Toast.makeText(Checkout_Activity.this, "No previous records", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        newaddline1.setText(temp1);
                        newaddline2.setText(temp2);
                        newaddcity.setText(temp3);
                        newaddpincode.setText(temp4);
                        newaddname.setText(temp5);
                        newaddnumber.setText(temp6);
                        newaddalternatenumber.setText(temp7);

                    }
                }
                else
                {
                    newaddline1.setText("");
                    newaddline2.setText("");
                    newaddcity.setText("");
                    newaddpincode.setText("");
                    newaddname.setText("");
                    newaddnumber.setText("");
                    newaddalternatenumber.setText("");
                }
            }
        });

        alertDialog.show();
    }
}