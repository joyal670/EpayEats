package com.epayeats.epayeatsuser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.epayeats.epayeatsuser.Activity.CartMainCatagory_Activity;
import com.epayeats.epayeatsuser.Database.MobUser;
import com.epayeats.epayeatsuser.R;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapterMain extends BaseAdapter
{
    private Context context;
    List<MobUser> count;

    public CartAdapterMain(Context context, List<MobUser> count) {
        this.context = context;
        this.count = count;
    }
    @Override
    public int getCount() {
        return count.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.cart_listview_items, null);

        ImageButton add, remove;
        ImageView image;
        TextView name, price, qty, type;
        Button itembtn;

        add = convertView.findViewById(R.id.addqty);
        remove = convertView.findViewById(R.id.removeqty);
        qty = convertView.findViewById(R.id.cartqty);
        image = convertView.findViewById(R.id.cartitemImage);
        name = convertView.findViewById(R.id.cartname);
        type = convertView.findViewById(R.id.carttype);
        price = convertView.findViewById(R.id.mycartprice);
        itembtn = convertView.findViewById(R.id.cartitemremovebtn);

        List<MobUser> UserList = SQLite.select().from(MobUser.class).queryList();

        String DBimage,DBname,DBprice,DBqty,DBtype;

        DBimage = count.get(position).getImage1();
        DBname = count.get(position).getMenuName();
        DBprice = count.get(position).getMenuOfferPrice();
        DBqty = String.valueOf(count.get(position).getQty());
        DBtype = count.get(position).getMenuSubCatagoreyName();

        Picasso.get().load(DBimage).into(image);
        name.setText(DBname);
        type.setText(DBtype);
        qty.setText(DBqty);
        price.setText(DBprice);
        type.setText(DBtype);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartMainCatagory_Activity.cartInterface.add(position);

            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartMainCatagory_Activity.cartInterface.remove(position);
            }
        });


        itembtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartMainCatagory_Activity.cartInterface.removeitem(position);
            }
        });

        return convertView;
    }


}
