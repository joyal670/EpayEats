package com.epayeats.epayeatsuser.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.epayeats.epayeatsuser.Adapter.CartAdapterMain;
import com.epayeats.epayeatsuser.Database.MobUser;
import com.epayeats.epayeatsuser.Database.MobUser_Table;
import com.epayeats.epayeatsuser.Interface.CartInterface;
import com.epayeats.epayeatsuser.R;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

public class CartMainCatagory_Activity extends AppCompatActivity implements CartInterface
{
    ListView listView;
    List<MobUser> UserList;
    Button cartCheckout;
    CartAdapterMain cartAdapter;

    public static CartInterface cartInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_);

        cartInterface = this;

        cartCheckout = findViewById(R.id.cartCheckOutMain);
        listView = findViewById(R.id.cartListViewMain);

        UserList = SQLite.select().from(MobUser.class).queryList();
        cartAdapter = new CartAdapterMain(CartMainCatagory_Activity.this, UserList);
        listView.setAdapter(cartAdapter);

        cartCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserList.isEmpty()) {
                    Toast.makeText(CartMainCatagory_Activity.this, "Add Some Products", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(CartMainCatagory_Activity.this, Checkout_Activity.class);
                    startActivity(intent);
                }

            }
        });


    }

    @Override
    public void add(int position)
    {
        try {

            int qq = UserList.get(position).getQty();
            qq = qq + 1;

            SQLite.update(MobUser.class).set(MobUser_Table.qty.eq(qq)).where(MobUser_Table.menuID.eq(UserList.get(position).getMenuID())).execute();
            UserList.get(position).setQty(qq);
            cartAdapter.notifyDataSetChanged();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(int position)
    {
        try {
            int qq = UserList.get(position).getQty();
            qq = qq - 1;
            if (qq <= 1) {
                qq = 1;
                Toast.makeText(CartMainCatagory_Activity.this, "Qty less then zero", Toast.LENGTH_SHORT).show();
            }
            SQLite.update(MobUser.class).set(MobUser_Table.qty.eq(qq)).where(MobUser_Table.menuID.eq(UserList.get(position).getMenuID())).execute();
            UserList.get(position).setQty(qq);
            cartAdapter.notifyDataSetChanged();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeitem(int position)
    {
        try {
            UserList.get(position).delete();
            UserList.remove(position);
            Toast.makeText(CartMainCatagory_Activity.this, "Item Removed", Toast.LENGTH_SHORT).show();
            cartAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}