package com.epayeats.epayeatsuser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.epayeats.epayeatsuser.Fragment.Dashboard_Fragment;
import com.epayeats.epayeatsuser.Model.FoodCatagoryModel;
import com.epayeats.epayeatsuser.R;

import java.util.List;

public class FoodCatagoryHomeAdapter extends RecyclerView.Adapter<FoodCatagoryHomeAdapter.ImageHolder>
{
    Context context;
    List<FoodCatagoryModel> foodCatagoryModel;

    public FoodCatagoryHomeAdapter(Context context, List<FoodCatagoryModel> foodCatagoryModel) {
        this.context = context;
        this.foodCatagoryModel = foodCatagoryModel;
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.food_catagorey_recyclerview_items, parent, false);
        return new ImageHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position)
    {
        FoodCatagoryModel currentUpload = foodCatagoryModel.get(position);

        holder.foodcatagory_editText.setText(currentUpload.getFoodCatagoreyType());

        holder.foodcatagory_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Dashboard_Fragment.mainCatagoryInterface.maincatgory(position, currentUpload.getFoodCatagoreyType());
            }
        });

    }

    @Override
    public int getItemCount() {
        return foodCatagoryModel.size();
    }


    public class ImageHolder extends RecyclerView.ViewHolder
    {
        TextView foodcatagory_editText;
        CardView main_card;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);

            foodcatagory_editText = itemView.findViewById(R.id.foodcatagory_recyclerview_textview);
            main_card = itemView.findViewById(R.id.main_card);

        }

    }

}
