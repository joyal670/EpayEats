package com.epayeats.epayeatsuser.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.epayeats.epayeatsuser.Activity.Checkout_Activity;
import com.epayeats.epayeatsuser.Adapter.CartAdapter;
import com.epayeats.epayeatsuser.Database.MobUser;
import com.epayeats.epayeatsuser.Database.MobUser_Table;
import com.epayeats.epayeatsuser.Interface.CartInterface;
import com.epayeats.epayeatsuser.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;


public class Cart_Fragment extends Fragment implements CartInterface
{
    ListView listView;
    List<MobUser> UserList;
    Button cartCheckout;
    CartAdapter cartAdapter;

    public static CartInterface cartInterface;

    public Cart_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart_, container, false);

        cartInterface = this;

        cartCheckout = view.findViewById(R.id.cartCheckOut);
        listView = view.findViewById(R.id.cartListView);

        UserList = SQLite.select().from(MobUser.class).queryList();
        cartAdapter = new CartAdapter(getActivity(), UserList);
        listView.setAdapter(cartAdapter);

        cartCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserList.isEmpty()) {
                    Toast.makeText(getContext(), "Add Some Products", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getContext(), Checkout_Activity.class);
                    startActivity(intent);
                }

            }
        });

        return view;
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
                Toast.makeText(getActivity(), "Qty less then zero", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Item Removed", Toast.LENGTH_SHORT).show();
            cartAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}