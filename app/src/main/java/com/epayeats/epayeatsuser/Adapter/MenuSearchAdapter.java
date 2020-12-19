package com.epayeats.epayeatsuser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.epayeats.epayeatsuser.Activity.HomeMainCatagory_Activity;
import com.epayeats.epayeatsuser.Fragment.Search_Fragment;
import com.epayeats.epayeatsuser.Model.MenuModel;
import com.epayeats.epayeatsuser.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MenuSearchAdapter extends RecyclerView.Adapter<MenuSearchAdapter.ImageHolder>
{
    Context context;
    List<MenuModel> menuModel;

    public MenuSearchAdapter(Context context, List<MenuModel> menuModel) {
        this.context = context;
        this.menuModel = menuModel;
    }

    @NonNull
    @Override
    public MenuSearchAdapter.ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.menu_listview_items, parent, false);
        return new MenuSearchAdapter.ImageHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MenuSearchAdapter.ImageHolder holder, int position)
    {
        MenuModel currentUpload = menuModel.get(position);

        holder.listview_menu_name.setText(currentUpload.getMenuName());
        holder.listview_menu_description.setText(currentUpload.getMenuDescription());
        holder.listview_menu_offer_price.setText(currentUpload.getMenuOfferPrice());
        holder.listview_menu_selling_price.setText(currentUpload.getMenuSellingPrice());
        holder.listview_menu_unit.setText(currentUpload.getMenuUnit());

        Picasso.get().load(currentUpload.getImage1()).into(holder.listview_menu_image1);

        holder.searchlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Search_Fragment.searchInterface.addtocart(position, currentUpload.getMenuID(), currentUpload.getMenuName(), currentUpload.getImage1(), currentUpload.getMenuMainCatagorey(), currentUpload.getMenuMainCatagoryID(), currentUpload.getMenuSubCatagorey(), currentUpload.getMenuSubCatagoreyID(), currentUpload.getMenuLocalAdminID(), currentUpload.getMenuDescription(), currentUpload.getMenuOfferPrice(), currentUpload.getMenuSellingPrice(), currentUpload.getMenuActualPrice(), currentUpload.getMenuOpenTime(), currentUpload.getMenuCloseTime(), currentUpload.getMenuOnorOff(), currentUpload.getMenuUnit(), currentUpload.getMenuApprovel(), currentUpload.getRestID(), currentUpload.getRestName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return menuModel.size();
    }

    public class ImageHolder extends RecyclerView.ViewHolder
    {
        TextView listview_menu_name, listview_menu_description;
        TextView listview_menu_offer_price, listview_menu_selling_price;
        TextView listview_menu_unit;
        ImageView listview_menu_image1;
        LinearLayout searchlayout;


        public ImageHolder(@NonNull View itemView) {
            super(itemView);

            listview_menu_name = itemView.findViewById(R.id.listview_menu_name);
            listview_menu_description = itemView.findViewById(R.id.listview_menu_description);

            listview_menu_offer_price = itemView.findViewById(R.id.listview_menu_offer_price);
            listview_menu_selling_price = itemView.findViewById(R.id.listview_menu_selling_price);

            listview_menu_unit = itemView.findViewById(R.id.listview_menu_unit);

            listview_menu_image1 = itemView.findViewById(R.id.listview_menu_image1);

            searchlayout = itemView.findViewById(R.id.searchlayout);

        }

    }
}


