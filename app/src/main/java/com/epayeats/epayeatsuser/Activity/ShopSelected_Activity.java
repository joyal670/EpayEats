package com.epayeats.epayeatsuser.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.epayeats.epayeatsuser.Adapter.MenuAdapter;
import com.epayeats.epayeatsuser.Adapter.SubCatagoryHomeAdapter;
import com.epayeats.epayeatsuser.Adapter.SubCatagoryShopAdapter;
import com.epayeats.epayeatsuser.Database.MobUser;

import com.epayeats.epayeatsuser.Database.MobUser_Table;
import com.epayeats.epayeatsuser.Interface.SubCatagoryInterface;
import com.epayeats.epayeatsuser.Model.MenuModel;
import com.epayeats.epayeatsuser.Model.SubCatagoryModel;
import com.epayeats.epayeatsuser.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShopSelected_Activity extends AppCompatActivity implements SubCatagoryInterface
{
    String resID;
    String resName;
    String resPhone;
    String resPhoto;

    String resLocation;
    String lat;
    String lon;

    String resLocalAdminID;
    String resLocalAdminName;
    String resOpenTime;
    String resCloseTime;

    String isShopClosed;
    String resLicenceNo;

    TextView shop_selected_name, shop_selected_phone, shop_selected_location, shop_selected_status, shop_selected_licence;
    ImageView shop_selected_image;
    CircleImageView shop_selected_status_red, shop_selected_status_green;

    ListView shop_selected_menu_listview;
    List<MenuModel> mMenuModel;
    MenuAdapter mMenuAdapter;

    RecyclerView shop_selected_sub_catagorey_recyclerview;
    List<SubCatagoryModel> subCatagoryModel;
    SubCatagoryShopAdapter subCatagoryHomeAdapter;
    public static SubCatagoryInterface subCatagoryInterface;

    public ProgressDialog progressDialog;

    BottomSheetBehavior behavior;
    ImageView sheet_cart_image;
    TextView sheet_cart_item_name, sheet_cart_item_description, sheet_cart_item_offer_price, sheet_cart_item_selling_price;
    Button sheet_cart_cartbtn;

    ImageButton sheet_cart_addqty, sheet_cart_removeqty;
    TextView sheet_cart_crntqty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_selected_);

        subCatagoryInterface = this;

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        resID = getIntent().getExtras().getString("resID");
        resName = getIntent().getExtras().getString("resName");
        resPhone = getIntent().getExtras().getString("resPhone");
        resPhoto = getIntent().getExtras().getString("resPhoto");

        resLocation = getIntent().getExtras().getString("resLocation");
        lat = getIntent().getExtras().getString("lat");
        lon = getIntent().getExtras().getString("lon");

        resLocalAdminID = getIntent().getExtras().getString("resLocalAdminID");
        resOpenTime = getIntent().getExtras().getString("resOpenTime");
        resCloseTime = getIntent().getExtras().getString("resCloseTime");
        resLocalAdminName = getIntent().getExtras().getString("resLocalAdminName");

        isShopClosed = getIntent().getExtras().getString("isShopClosed");
        resLicenceNo = getIntent().getExtras().getString("resLicenceNo");

        shop_selected_name = findViewById(R.id.shop_selected_name);
        shop_selected_phone = findViewById(R.id.shop_selected_phone);
        shop_selected_location = findViewById(R.id.shop_selected_location);
//        shop_selected_open = findViewById(R.id.shop_selected_open);
//        shop_selected_close = findViewById(R.id.shop_selected_close);
        shop_selected_status = findViewById(R.id.shop_selected_status);
        shop_selected_image = findViewById(R.id.shop_selected_image);
        shop_selected_licence = findViewById(R.id.shop_selected_licence);
        shop_selected_menu_listview = findViewById(R.id.shop_selected_menu_listview);
        sheet_cart_cartbtn = findViewById(R.id.sheet_cart_cartbtn);
        sheet_cart_item_offer_price = findViewById(R.id.sheet_cart_item_offer_price);
        sheet_cart_item_selling_price = findViewById(R.id.sheet_cart_item_selling_price);
        shop_selected_sub_catagorey_recyclerview = findViewById(R.id.shop_selected_sub_catagorey_recyclerview);
        shop_selected_status_green = findViewById(R.id.shop_selected_status_green);
        shop_selected_status_red = findViewById(R.id.shop_selected_status_red);

        sheet_cart_addqty = findViewById(R.id.sheet_cart_addqty);
        sheet_cart_removeqty = findViewById(R.id.sheet_cart_removeqty);
        sheet_cart_crntqty = findViewById(R.id.sheet_cart_crntqty);

        Toolbar toolbar = findViewById(R.id.toolbarshop);
        setSupportActionBar(toolbar);


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
                    Toast.makeText(ShopSelected_Activity.this, "Qty less than zero", Toast.LENGTH_SHORT).show();
                    sheet_cart_crntqty.setText(temp1 + "");
                }
                sheet_cart_crntqty.setText(temp1 + "");
            }
        });

        shop_selected_name.setText(resName);
        shop_selected_phone.setText(resPhone);
        shop_selected_location.setText(resLocation);
//        shop_selected_open.setText(resOpenTime);
//        shop_selected_close.setText(resCloseTime);
        shop_selected_status.setText(isShopClosed);
        shop_selected_licence.setText(resLicenceNo);

        if(isShopClosed.equals("open"))
        {
            shop_selected_status_green.setVisibility(View.VISIBLE);
            shop_selected_status_red.setVisibility(View.GONE);

        }
        if(isShopClosed.equals("closed"))
        {
            shop_selected_status_red.setVisibility(View.VISIBLE);
            shop_selected_status_green.setVisibility(View.GONE);
        }

        Picasso.get().load(resPhoto).into(shop_selected_image);

        View bootom = findViewById(R.id.bottom_sheet_cart);
        sheet_cart_image = findViewById(R.id.sheet_cart_image);
        sheet_cart_item_name = findViewById(R.id.sheet_cart_item_name);
        sheet_cart_item_description = findViewById(R.id.sheet_cart_item_description);

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

        sheet_cart_cartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String name = sheet_cart_item_name.getText().toString();
                if(name.isEmpty())
                {
                    Toast.makeText(ShopSelected_Activity.this, "Please select a menu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadMenu();
        loadSubCatagory();

    }

    // load all menu
    private void loadMenu()
    {
        progressDialog.show();
        Query query = FirebaseDatabase.getInstance().getReference("menu");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                mMenuModel.clear();
                progressDialog.dismiss();
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    if(resID.equals(snapshot1.child("restID").getValue().toString()))
                    {
                        if("Approved".equals(snapshot1.child("menuApprovel").getValue().toString()))
                        {
                            if("on".equals(snapshot1.child("menuOnorOff").getValue().toString()))
                            {
                                String date = new SimpleDateFormat("H", Locale.getDefault()).format(new Date());
                                String mOpen = snapshot1.child("menuOpenTime").getValue().toString();
                                String mClose = snapshot1.child("menuCloseTime").getValue().toString();

                                int a = Integer.parseInt(mOpen);
                                int b = Integer.parseInt(mClose);
                                int c = Integer.parseInt(date);

                                if((a <= c ) && (c <= b))
                                {
                                    MenuModel model = snapshot1.getValue(MenuModel.class);
                                    mMenuModel.add(model);
                                }
                            }
                        }
                    }
                }
                mMenuAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                progressDialog.dismiss();
                Toast.makeText(ShopSelected_Activity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        mMenuModel = new ArrayList<>();
        mMenuAdapter = new MenuAdapter(this, mMenuModel);
        shop_selected_menu_listview.setAdapter(mMenuAdapter);
        shop_selected_menu_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bottomSetup(position);
            }
        });

    }

    // load sub category
    private void loadSubCatagory()
    {
        progressDialog.show();
        DatabaseReference subMenuReference;
        subMenuReference = FirebaseDatabase.getInstance().getReference("sub_category");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        shop_selected_sub_catagorey_recyclerview.setLayoutManager(layoutManager);
        shop_selected_sub_catagorey_recyclerview.setHasFixedSize(true);

        subMenuReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                progressDialog.dismiss();
                subCatagoryModel.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    SubCatagoryModel model = snapshot1.getValue(SubCatagoryModel.class);
                    subCatagoryModel.add(model);
                }
                subCatagoryHomeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                progressDialog.dismiss();
                Toast.makeText(ShopSelected_Activity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        subCatagoryModel = new ArrayList<>();
        subCatagoryHomeAdapter = new SubCatagoryShopAdapter(ShopSelected_Activity.this, subCatagoryModel);
        shop_selected_sub_catagorey_recyclerview.setAdapter(subCatagoryHomeAdapter);

    }

    // click on sub category
    @Override
    public void subcatagory(int position, String name)
    {
        progressDialog.show();
        Query query = FirebaseDatabase.getInstance().getReference("menu");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                mMenuModel.clear();
                progressDialog.dismiss();
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    if(resID.equals(snapshot1.child("restID").getValue().toString()))
                    {
                        if(name.equals(snapshot1.child("menuSubCatagorey").getValue().toString()))
                        {
                            if("Approved".equals(snapshot1.child("menuApprovel").getValue().toString()))
                            {
                                if("on".equals(snapshot1.child("menuOnorOff").getValue().toString()))
                                {
                                    String date = new SimpleDateFormat("H", Locale.getDefault()).format(new Date());
                                    String mOpen = snapshot1.child("menuOpenTime").getValue().toString();
                                    String mClose = snapshot1.child("menuCloseTime").getValue().toString();

                                    int a = Integer.parseInt(mOpen);
                                    int b = Integer.parseInt(mClose);
                                    int c = Integer.parseInt(date);

                                    if((a <= c ) && (c <= b))
                                    {
                                        MenuModel model = snapshot1.getValue(MenuModel.class);
                                        mMenuModel.add(model);
                                    }
                                }
                            }
                        }
                    }
                }
                mMenuAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                progressDialog.dismiss();
                Toast.makeText(ShopSelected_Activity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        mMenuModel = new ArrayList<>();
        mMenuAdapter = new MenuAdapter(this, mMenuModel);
        shop_selected_menu_listview.setAdapter(mMenuAdapter);
        shop_selected_menu_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bottomSetup(position);
            }
        });

    }

    private void bottomSetup(int position)
    {
        if(behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
        {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        else
        {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        Picasso.get().load(mMenuModel.get(position).getImage1()).into(sheet_cart_image);
        sheet_cart_item_name.setText(mMenuModel.get(position).getMenuName());
        sheet_cart_item_description.setText(mMenuModel.get(position).getMenuDescription());
        sheet_cart_item_selling_price.setPaintFlags(sheet_cart_item_selling_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        sheet_cart_item_selling_price.setText(mMenuModel.get(position).getMenuSellingPrice());
        sheet_cart_item_offer_price.setText(mMenuModel.get(position).getMenuOfferPrice());

        sheet_cart_cartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String name = sheet_cart_item_name.getText().toString();
                if(name.isEmpty())
                {
                    Toast.makeText(ShopSelected_Activity.this, "Please select a menu", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                        if("closed".equals(isShopClosed))
                        {
                            Toast.makeText(ShopSelected_Activity.this, "The shop is currently closed", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String date = new SimpleDateFormat("H", Locale.getDefault()).format(new Date());
                            String mOpen = resOpenTime;
                            String mClose = resCloseTime;

                            int a = Integer.parseInt(mOpen);
                            int b = Integer.parseInt(mClose);
                            int c = Integer.parseInt(date);

                            if((a <= c ) && (c <= b) )
                            {
                                String qty = sheet_cart_crntqty.getText().toString();

                                List<MobUser> UserList;
                                String id = mMenuModel.get(position).getMenuID();
                                UserList = SQLite.select().from(MobUser.class).where(MobUser_Table.menuID.eq(id)).queryList();
                                if(UserList.size() == 0)
                                {
                                    MobUser user = new MobUser();
                                    user.insertData(id, mMenuModel.get(position).getMenuName(), mMenuModel.get(position).getImage1(), mMenuModel.get(position).getMenuMainCatagorey(), mMenuModel.get(position).getMenuMainCatagoryID(), mMenuModel.get(position).getMenuSubCatagorey(), mMenuModel.get(position).getMenuSubCatagoreyID(), mMenuModel.get(position).getMenuLocalAdminID(), mMenuModel.get(position).getMenuDescription(), mMenuModel.get(position).getMenuOfferPrice(), mMenuModel.get(position).getMenuSellingPrice(), mMenuModel.get(position).getMenuActualPrice(), mMenuModel.get(position).getMenuOpenTime(), mMenuModel.get(position).getMenuCloseTime(), mMenuModel.get(position).getMenuOnorOff(), mMenuModel.get(position).getMenuUnit(), mMenuModel.get(position).getMenuApprovel(), mMenuModel.get(position).getRestName(), mMenuModel.get(position).getRestID(), Integer.parseInt(qty));

                                    boolean checkSave = user.save();

                                    if(checkSave)
                                    {
                                        Toast.makeText(ShopSelected_Activity.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(ShopSelected_Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    int q = UserList.get(0).getQty();
                                    q = q + Integer.parseInt(qty);
                                    SQLite.update(MobUser.class).set(MobUser_Table.qty.eq(q)).where(MobUser_Table.menuID.eq(id)).execute();
                                    Toast.makeText(ShopSelected_Activity.this, "Already exists in cart, Added one more Item !!!", Toast.LENGTH_SHORT).show();

                                }
                            }
                            else
                            {
                                Toast.makeText(ShopSelected_Activity.this, "Shop is currently closed", Toast.LENGTH_SHORT).show();
                            }
                        }


                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.cart_option_menu_item:

                Intent intent = new Intent(ShopSelected_Activity.this, CartMainCatagory_Activity.class);
                startActivity(intent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}