package com.epayeats.epayeatsuser.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.epayeats.epayeatsuser.Activity.HomeMainCatagory_Activity;
import com.epayeats.epayeatsuser.Activity.ShopSelected_Activity;
import com.epayeats.epayeatsuser.Model.MenuModel;
import com.epayeats.epayeatsuser.Model.SubCatagoryModel;
import com.epayeats.epayeatsuser.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MenuRecyclerAdapter extends RecyclerView.Adapter<MenuRecyclerAdapter.ImageHolder>
{
    Context context;
    List<MenuModel> menuModel;

    public MenuRecyclerAdapter(Context context, List<MenuModel> menuModel) {
        this.context = context;
        this.menuModel = menuModel;
    }

    @NonNull
    @Override
    public MenuRecyclerAdapter.ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.sub_menu_recycler, parent, false);
        return new MenuRecyclerAdapter.ImageHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MenuRecyclerAdapter.ImageHolder holder, int position)
    {
        MenuModel currentUpload = menuModel.get(position);

        holder.recycler_menu_name.setText(currentUpload.getMenuName());
        holder.recycler_menu_description.setText(currentUpload.getMenuDescription());
        holder.recycler_menu_offer_price.setText(currentUpload.getMenuOfferPrice());
        holder.recycler_menu_selling_price.setText(currentUpload.getMenuSellingPrice());
        holder.recycler_menu_unit.setText(currentUpload.getMenuUnit());

        Picasso.get().load(currentUpload.getImage1()).into(holder.recycler_menu_image1);

        holder.addtocartlayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                HomeMainCatagory_Activity.mainMenuAddToCartInterface.addtocartmainmenu(position, currentUpload.getMenuID(), currentUpload.getMenuName(), currentUpload.getImage1(), currentUpload.getMenuMainCatagorey(), currentUpload.getMenuMainCatagoryID(), currentUpload.getMenuSubCatagorey(), currentUpload.getMenuSubCatagoreyID(), currentUpload.getMenuLocalAdminID(), currentUpload.getMenuDescription(), currentUpload.getMenuOfferPrice(), currentUpload.getMenuSellingPrice(), currentUpload.getMenuActualPrice(), currentUpload.getMenuOpenTime(), currentUpload.getMenuCloseTime(), currentUpload.getMenuOnorOff(), currentUpload.getMenuUnit(), currentUpload.getMenuApprovel(), currentUpload.getRestID(), currentUpload.getRestName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return menuModel.size();
    }

    public class ImageHolder extends RecyclerView.ViewHolder
    {
        TextView recycler_menu_name, recycler_menu_description, recycler_menu_offer_price, recycler_menu_selling_price, recycler_menu_unit;
        ImageView recycler_menu_image1;
        LinearLayout addtocartlayout1;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);

            recycler_menu_name = itemView.findViewById(R.id.recycler_menu_name);
            recycler_menu_description = itemView.findViewById(R.id.recycler_menu_description);
            recycler_menu_offer_price = itemView.findViewById(R.id.recycler_menu_offer_price);
            recycler_menu_selling_price = itemView.findViewById(R.id.recycler_menu_selling_price);
            recycler_menu_unit = itemView.findViewById(R.id.recycler_menu_unit);
            recycler_menu_image1 = itemView.findViewById(R.id.recycler_menu_image1);
            addtocartlayout1 = itemView.findViewById(R.id.addtocartlayout1);

            recycler_menu_selling_price.setPaintFlags(recycler_menu_selling_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        }

    }
}

