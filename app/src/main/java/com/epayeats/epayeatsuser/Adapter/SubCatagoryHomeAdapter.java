package com.epayeats.epayeatsuser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.epayeats.epayeatsuser.Fragment.Dashboard_Fragment;
import com.epayeats.epayeatsuser.Model.SubCatagoryModel;
import com.epayeats.epayeatsuser.R;

import java.util.List;


public class SubCatagoryHomeAdapter extends RecyclerView.Adapter<SubCatagoryHomeAdapter.ImageHolder>
{
    Context context;
    List<SubCatagoryModel> menuModel;

    public SubCatagoryHomeAdapter(Context context, List<SubCatagoryModel> menuModel) {
        this.context = context;
        this.menuModel = menuModel;
    }

    @NonNull
    @Override
    public SubCatagoryHomeAdapter.ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.sub_menu, parent, false);
        return new SubCatagoryHomeAdapter.ImageHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull SubCatagoryHomeAdapter.ImageHolder holder, int position)
    {
        SubCatagoryModel currentUpload = menuModel.get(position);

        holder.sub_menu_adapter_textview.setText(currentUpload.getSubCatagoryName());

        holder.sub_menu_adapter_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Dashboard_Fragment.subCatagoryInterface.subcatagory(position, currentUpload.getSubCatagoryName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return menuModel.size();
    }

    public class ImageHolder extends RecyclerView.ViewHolder
    {
        TextView sub_menu_adapter_textview;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);

            sub_menu_adapter_textview = itemView.findViewById(R.id.sub_menu_adapter_textview);

        }

    }
}

