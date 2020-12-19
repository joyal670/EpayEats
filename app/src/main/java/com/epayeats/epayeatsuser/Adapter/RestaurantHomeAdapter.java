package com.epayeats.epayeatsuser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.epayeats.epayeatsuser.Model.RestaurantModel;
import com.epayeats.epayeatsuser.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RestaurantHomeAdapter extends RecyclerView.Adapter<RestaurantHomeAdapter.ImageHolder>
{
    Context context;
    List<RestaurantModel> restaurantModel;
    RestaurantHomeAdapter.OnitemClickListener mlistener;

    public RestaurantHomeAdapter(Context context, List<RestaurantModel> restaurantModel) {
        this.context = context;
        this.restaurantModel = restaurantModel;
    }

    @NonNull
    @Override
    public RestaurantHomeAdapter.ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.restaurant_listview_items, parent, false);
        return new RestaurantHomeAdapter.ImageHolder(view);
    }

    @Override
    public int getItemCount() {
        return restaurantModel.size();
    }

    public class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView restaurant__item_name, restaurant__item_phone, restaurant__item_open_time;
        TextView restaurant__item_close_time, restaurant__item_palce;
        CircleImageView restaurant__item_circular_image_red, restaurant__item_circular_image_green;
        ImageView restaurant__item_image;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);

            restaurant__item_name = itemView.findViewById(R.id.restaurant__item_name);
            restaurant__item_phone = itemView.findViewById(R.id.restaurant__item_phone);

            restaurant__item_open_time = itemView.findViewById(R.id.restaurant__item_open_time);
            restaurant__item_close_time = itemView.findViewById(R.id.restaurant__item_close_time);
            restaurant__item_circular_image_red = itemView.findViewById(R.id.restaurant__item_circular_image_red);
            restaurant__item_circular_image_green = itemView.findViewById(R.id.restaurant__item_circular_image_green);
            restaurant__item_image = itemView.findViewById(R.id.restaurant__item_image);
            restaurant__item_palce = itemView.findViewById(R.id.restaurant__item_palce);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mlistener != null)
            {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION)
                {
                    mlistener.onItemClick(position);
                }
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantHomeAdapter.ImageHolder holder, int position) {

        RestaurantModel currentShopModel = restaurantModel.get(position);

        holder.restaurant__item_name.setText(currentShopModel.getResName());
        holder.restaurant__item_phone.setText(currentShopModel.getResPhone());
        holder.restaurant__item_open_time.setText(currentShopModel.getResOpenTime());
        holder.restaurant__item_close_time.setText(currentShopModel.getResCloseTime());
        holder.restaurant__item_palce.setText(currentShopModel.getResLocation());

        String temp = currentShopModel.getIsShopClosed();
        if(temp.equals("open"))
        {
            holder.restaurant__item_circular_image_red.setVisibility(View.GONE);
            holder.restaurant__item_circular_image_green.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.restaurant__item_circular_image_red.setVisibility(View.VISIBLE);
            holder.restaurant__item_circular_image_green.setVisibility(View.GONE);
        }

        Picasso.get().load(currentShopModel.getResPhoto()).into(holder.restaurant__item_image);

    }

    public interface OnitemClickListener
    {
        void onItemClick(int position);
    }

    public void setOnClickListener(RestaurantHomeAdapter.OnitemClickListener listener)
    {
        mlistener = listener;
    }

}

