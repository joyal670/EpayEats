package com.epayeats.epayeatsuser.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.epayeats.epayeatsuser.Adapter.MenuRecyclerAdapter;
import com.epayeats.epayeatsuser.Database.MobUser;
import com.epayeats.epayeatsuser.Database.MobUser_Table;
import com.epayeats.epayeatsuser.Interface.MainMenuAddToCartInterface;
import com.epayeats.epayeatsuser.Model.MenuModel;
import com.epayeats.epayeatsuser.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeMainCatagory_Activity extends AppCompatActivity implements MainMenuAddToCartInterface
{
    String position;
    String name;
    String foodCatagoreyID;
    String foodCatagoreyType;

    RecyclerView menu_listing_menu_recyclerview;

    DatabaseReference reference;
    List<MenuModel> model;
    MenuRecyclerAdapter menuAdapter;

    public static MainMenuAddToCartInterface mainMenuAddToCartInterface;

    BottomSheetBehavior behavior;

    ImageView sheet_main_menu_image;
    TextView sheet_main_menu_item_name, sheet_main_menu_item_description, sheet_main_menu_item_offer_price, sheet_main_menu_item_selling_price;
    Button sheet_main_menu_cartbtn;

    public ProgressDialog progressDialog;

    SharedPreferences sharedPreferences;
    String user_lat;
    String user_long;

    ImageButton sheet_cart_addqty, sheet_cart_removeqty;
    TextView sheet_cart_crntqty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main_catagory_);

        mainMenuAddToCartInterface = this;


        sharedPreferences = getSharedPreferences("data", 0);
        user_lat = sharedPreferences.getString("lat","");
        user_long = sharedPreferences.getString("lot","");

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        position = getIntent().getExtras().getString("position");
        name = getIntent().getExtras().getString("name");
        foodCatagoreyID = getIntent().getExtras().getString("foodCatagoreyID");
        foodCatagoreyType = getIntent().getExtras().getString("foodCatagoreyType");

        menu_listing_menu_recyclerview = findViewById(R.id.menu_listing_menu_recyclerview);
        sheet_main_menu_image = findViewById(R.id.sheet_main_menu_image);
        sheet_main_menu_item_name = findViewById(R.id.sheet_main_menu_item_name);
        sheet_main_menu_item_description = findViewById(R.id.sheet_main_menu_item_description);
        sheet_main_menu_cartbtn = findViewById(R.id.sheet_main_menu_cartbtn);
        sheet_main_menu_item_offer_price = findViewById(R.id.sheet_main_menu_item_offer_price);
        sheet_main_menu_item_selling_price = findViewById(R.id.sheet_main_menu_item_selling_price);
        sheet_main_menu_item_selling_price.setPaintFlags(sheet_main_menu_item_selling_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        Toolbar toolbar = findViewById(R.id.toolbarmain);
        toolbar.setTitle(foodCatagoreyType);
        setSupportActionBar(toolbar);


        sheet_cart_addqty = findViewById(R.id.sheet_cart_addqtymain);
        sheet_cart_removeqty = findViewById(R.id.sheet_cart_removeqtymain);
        sheet_cart_crntqty = findViewById(R.id.sheet_cart_crntqtymain);
        sheet_cart_addqty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String temp = sheet_cart_crntqty.getText().toString();
                int temp1 = Integer.parseInt(temp);

                temp1 = temp1 + 1;

                sheet_cart_crntqty.setText(temp1 + "");
            }
        });

        sheet_cart_removeqty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String temp = sheet_cart_crntqty.getText().toString();
                int temp1 = Integer.parseInt(temp);

                temp1 = temp1 - 1;

                if(temp1 <= 1)
                {
                    temp1 = 1;
                    Toast.makeText(HomeMainCatagory_Activity.this, "Qty less than zero", Toast.LENGTH_SHORT).show();
                    sheet_cart_crntqty.setText(temp1 + "");
                }
                sheet_cart_crntqty.setText(temp1 + "");
            }
        });


        reference = FirebaseDatabase.getInstance().getReference("menu");

        View bootom = findViewById(R.id.bottom_sheet_main_menu);
        behavior = BottomSheetBehavior.from(bootom);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState)
            {
                switch (newState)
                {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset)
            {}
        });

        sheet_main_menu_cartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String name = sheet_main_menu_item_name.getText().toString();
                if(name.isEmpty())
                {
                    Toast.makeText(HomeMainCatagory_Activity.this, "Please select a menu", Toast.LENGTH_SHORT).show();
                }
            }
        });


        loadMenu();
    }

    private void loadMenu()
    {
        progressDialog.show();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                model.clear();
                progressDialog.dismiss();
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    if(foodCatagoreyType.equals(snapshot1.child("menuMainCatagorey").getValue().toString()))
                    {
                        if("Approved".equals(snapshot1.child("menuApprovel").getValue().toString()))
                        {
                            if("on".equals(snapshot1.child("menuOnorOff").getValue().toString()))
                            {
                                String restId = snapshot1.child("restID").getValue().toString();

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("restaurants").child(restId);
                                ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot)
                                    {
                                        if("UnBlocked".equals(snapshot.child("isBlocked").getValue().toString()))
                                        {
                                            String rest_lat;
                                            String rest_long;

                                            rest_lat = snapshot.child("lat").getValue().toString();
                                            rest_long = snapshot.child("lon").getValue().toString();

                                            Double km = distance(Double.parseDouble(user_lat), Double.parseDouble(user_long), Double.parseDouble(rest_lat), Double.parseDouble(rest_long));

                                            String roKm = String.valueOf(Math.round(km));

                                            int temproKm = Integer.parseInt(roKm);

                                            if(temproKm <= 10)
                                            {
                                                String op = snapshot.child("resOpenTime").getValue().toString();
                                                String cl = snapshot.child("resCloseTime").getValue().toString();

                                                String date = new SimpleDateFormat("H", Locale.getDefault()).format(new Date());

                                                int a = Integer.parseInt(op);
                                                int b = Integer.parseInt(cl);
                                                int c = Integer.parseInt(date);

                                                if((a <= c ) && (c <= b) )
                                                {
                                                    if("open".equals(snapshot.child("isShopClosed").getValue().toString()))
                                                    {
                                                        MenuModel menuModel = snapshot1.getValue(MenuModel.class);
                                                        model.add(menuModel);
                                                    }
                                                }

                                            }
                                        }
                                        menuAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error)
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(HomeMainCatagory_Activity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                }
                menuAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                progressDialog.dismiss();
                Toast.makeText(HomeMainCatagory_Activity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        model = new ArrayList<>();
        menuAdapter = new MenuRecyclerAdapter(HomeMainCatagory_Activity.this, model);
        menu_listing_menu_recyclerview.setAdapter(menuAdapter);
    }



    private void bottomSetup()
    {
        if(behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
        {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        else
        {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

    }

    @Override
    public void addtocartmainmenu(int position, String menuID, String menuName, String image1, String menuMainCatagorey, String menuMainCatagoryID, String menuSubCatagorey, String menuSubCatagoreyID, String menuLocalAdminID, String menuDescription, String menuOfferPrice, String menuSellingPrice, String menuActualPrice, String menuOpenTime, String menuCloseTime, String menuOnorOff, String menuUnit, String menuApprovel, String restID, String restName)
    {
        bottomSetup();

        Picasso.get().load(image1).into(sheet_main_menu_image);
        sheet_main_menu_item_name.setText(menuName);
        sheet_main_menu_item_description.setText(menuDescription);
        sheet_main_menu_item_offer_price.setText(menuOfferPrice);
        sheet_main_menu_item_selling_price.setText(menuSellingPrice);

        sheet_main_menu_cartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                String name = sheet_main_menu_item_name.getText().toString();
                if(name.isEmpty())
                {
                    Toast.makeText(HomeMainCatagory_Activity.this, "Please select an item", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String date = new SimpleDateFormat("H", Locale.getDefault()).format(new Date());
                    String mOpen = menuOpenTime;
                    String mClose = menuCloseTime;

                    int a = Integer.parseInt(mOpen);
                    int b = Integer.parseInt(mClose);
                    int c = Integer.parseInt(date);

                    if((a <= c ) && (c <= b) )
                    {
                        String qty = sheet_cart_crntqty.getText().toString();
                        List<MobUser> UserList;
                        UserList = SQLite.select().from(MobUser.class).where(MobUser_Table.menuID.eq(menuID)).queryList();
                        if(UserList.size() == 0)
                        {
                            MobUser user = new MobUser();
                            user.insertData(menuID, menuName, image1, menuMainCatagorey, menuMainCatagoryID, menuSubCatagorey, menuSubCatagoreyID, menuLocalAdminID, menuDescription, menuOfferPrice, menuSellingPrice, menuActualPrice, menuOpenTime, menuCloseTime, menuOnorOff, menuUnit, menuApprovel, restName, restID, Integer.parseInt(qty));

                            boolean checkSave = user.save();

                            if(checkSave)
                            {
                                Toast.makeText(HomeMainCatagory_Activity.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(HomeMainCatagory_Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else
                        {
                            int q = UserList.get(0).getQty();
                            q = q + Integer.parseInt(qty);
                            SQLite.update(MobUser.class).set(MobUser_Table.qty.eq(q)).where(MobUser_Table.menuID.eq(menuID)).execute();
                            Toast.makeText(HomeMainCatagory_Activity.this, "Already exists in cart, Added one more Item !!!", Toast.LENGTH_SHORT).show();

                        }
                    }
                    else
                    {
                        Toast.makeText(HomeMainCatagory_Activity.this, "Sorry, this menu is currently unavailable ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.cart_option_menu_item:

                Intent intent = new Intent(HomeMainCatagory_Activity.this, CartMainCatagory_Activity.class);
                startActivity(intent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}