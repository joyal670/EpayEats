package com.epayeats.epayeatsuser.Fragment;

import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.epayeats.epayeatsuser.Activity.HomeMainCatagory_Activity;
import com.epayeats.epayeatsuser.Adapter.MenuAdapter;
import com.epayeats.epayeatsuser.Adapter.MenuSearchAdapter;
import com.epayeats.epayeatsuser.Database.MobUser;
import com.epayeats.epayeatsuser.Database.MobUser_Table;
import com.epayeats.epayeatsuser.Interface.searchInterface;
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


public class Search_Fragment extends Fragment implements searchInterface
{

    SearchView search_fragment_searchview;
    RecyclerView search_fragment_listview;

    DatabaseReference reference;
    List<MenuModel> menuModel;
    MenuSearchAdapter menuAdapter;

    SharedPreferences sharedPreferences;
    String user_lat;
    String user_long;

    public static searchInterface searchInterface;

    TextView sheet_search_item_name, sheet_search_item_description, sheet_search_item_offer_price, sheet_search_item_selling_price;
    ImageView sheet_search_image;
    Button sheet_search_cartbtn;

    BottomSheetBehavior behavior;

    ImageButton sheet_cart_addqty, sheet_cart_removeqty;
    TextView sheet_cart_crntqty;

    public Search_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_, container, false);

        searchInterface = this;

        sharedPreferences = getContext().getSharedPreferences("data", 0);
        user_lat = sharedPreferences.getString("lat","");
        user_long = sharedPreferences.getString("lot","");

        search_fragment_listview = view.findViewById(R.id.search_fragment_listview);
        search_fragment_searchview = view.findViewById(R.id.search_fragment_searchview);
        sheet_search_image = view.findViewById(R.id.sheet_search_image);
        sheet_search_item_name = view.findViewById(R.id.sheet_search_item_name);
        sheet_search_item_description = view.findViewById(R.id.sheet_search_item_description);
        sheet_search_item_offer_price = view.findViewById(R.id.sheet_search_item_offer_price);
        sheet_search_item_selling_price = view.findViewById(R.id.sheet_search_item_selling_price);
        sheet_search_cartbtn = view.findViewById(R.id.sheet_search_cartbtn);

        sheet_search_item_selling_price.setPaintFlags(sheet_search_item_selling_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        search_fragment_listview.setVisibility(View.INVISIBLE);

        search_fragment_searchview.setIconifiedByDefault(true);
        search_fragment_searchview.setFocusable(true);
        search_fragment_searchview.setIconified(false);
        search_fragment_searchview.requestFocusFromTouch();


        reference = FirebaseDatabase.getInstance().getReference("menu");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                menuModel.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    if("on".equals(snapshot1.child("menuOnorOff").getValue().toString()))
                    {
                        if("Approved".equals(snapshot1.child("menuApprovel").getValue().toString()))
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
                                                    MenuModel model = snapshot1.getValue(MenuModel.class);
                                                    menuModel.add(model);
                                                }
                                            }

                                        }
                                    }
                                    menuAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error)
                                {
                                    Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
                menuAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        menuModel = new ArrayList<>();
        menuAdapter = new MenuSearchAdapter(getContext(), menuModel);
        search_fragment_listview.setAdapter(menuAdapter);

        search_fragment_searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                searchMenu(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                searchMenu(newText);
                return false;
            }
        });


        View bootom = view.findViewById(R.id.bottom_sheet_search);
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

        sheet_search_cartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String name = sheet_search_item_name.getText().toString();
                if(name.isEmpty())
                {
                    Toast.makeText(getContext(), "Please select a menu", Toast.LENGTH_SHORT).show();
                }
            }
        });


        sheet_cart_addqty = view.findViewById(R.id.sheet_cart_addqtysearch);
        sheet_cart_removeqty = view.findViewById(R.id.sheet_cart_removeqtysearch);
        sheet_cart_crntqty = view.findViewById(R.id.sheet_cart_crntqtysearch);
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
                    Toast.makeText(getContext(), "Qty less than zero", Toast.LENGTH_SHORT).show();
                    sheet_cart_crntqty.setText(temp1 + "");
                }
                sheet_cart_crntqty.setText(temp1 + "");
            }
        });


        return view;
    }

    private void searchMenu(String query)
    {
        ArrayList<MenuModel> myList = new ArrayList<>();
        for (MenuModel obj : menuModel) {
            if (obj.getMenuName().toLowerCase().contains(query.toLowerCase())) {
                myList.add(obj);
            }
        }

        search_fragment_listview.setVisibility(View.VISIBLE);
        MenuSearchAdapter adapter = new MenuSearchAdapter(getContext(), myList);
        search_fragment_listview.setAdapter(adapter);
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
    public void addtocart(int position, String menuID, String menuName, String image1, String menuMainCatagorey, String menuMainCatagoryID, String menuSubCatagorey, String menuSubCatagoreyID, String menuLocalAdminID, String menuDescription, String menuOfferPrice, String menuSellingPrice, String menuActualPrice, String menuOpenTime, String menuCloseTime, String menuOnorOff, String menuUnit, String menuApprovel, String restID, String restName)
    {
        bottomSetup();

        Picasso.get().load(image1).into(sheet_search_image);
        sheet_search_item_name.setText(menuName);
        sheet_search_item_description.setText(menuDescription);
        sheet_search_item_offer_price.setText(menuOfferPrice);
        sheet_search_item_selling_price.setText(menuSellingPrice);


        sheet_search_cartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String name = sheet_search_item_name.getText().toString();
                if(name.isEmpty())
                {
                    Toast.makeText(getContext(), "Please select an item", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getContext(), "Added to Cart", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else
                        {
                            int q = UserList.get(0).getQty();
                            q = q + Integer.parseInt(qty);
                            SQLite.update(MobUser.class).set(MobUser_Table.qty.eq(q)).where(MobUser_Table.menuID.eq(menuID)).execute();
                            Toast.makeText(getContext(), "Already exists in cart, Added one more Item !!!", Toast.LENGTH_SHORT).show();

                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Sorry, this menu is currently unavailable ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


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
}