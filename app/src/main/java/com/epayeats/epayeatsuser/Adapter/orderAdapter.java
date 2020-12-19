package com.epayeats.epayeatsuser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.epayeats.epayeatsuser.Model.orderModel;
import com.epayeats.epayeatsuser.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class orderAdapter extends RecyclerView.Adapter<orderAdapter.ImageHolder>
{
    Context context;
    List<orderModel> mCheckout;
    OnitemClickListener mlistener;

    public orderAdapter(Context context, List<orderModel> mCheckout) {
        this.context = context;
        this.mCheckout = mCheckout;
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.orderd_items, parent, false);
        return new ImageHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position)
    {
        orderModel currentUpload = mCheckout.get(position);

        holder.ordered_itemname.setText(currentUpload.getMenuName());
        holder.ordered_itemprice.setText(currentUpload.getOfferPrice());
        holder.ordered_itemqty.setText(currentUpload.getQty());
        holder.ordered_date.setText(currentUpload.getOrderDate());

        if(currentUpload.getOrderStatus().equalsIgnoreCase("0"))
        {
            holder.ordered_itemstatus.setText("Pending, Not yet Delivered");
            holder.ordered_deliverddate.setText("Pending");
        }
        else if(currentUpload.getOrderStatus().equalsIgnoreCase("1"))
        {
            holder.ordered_itemstatus.setText("Order is Picked up by the Delivery Agent");
            holder.ordered_deliverddate.setText("Out of Delivery");

        }
        else if(currentUpload.getOrderStatus().equalsIgnoreCase("2"))
        {

            holder.ordered_itemstatus.setText("Delivered");
            holder.ordered_deliverddate.setText(currentUpload.getDeliveryDate());
        }
        else
        {
            holder.ordered_itemstatus.setText("Cancelled");
        }

        Picasso.get()
                .load(currentUpload.getMenuImage())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.ordered_itemimage);

        int price = Integer.parseInt(currentUpload.getOfferPrice());
        int qty = Integer.parseInt(currentUpload.getQty());
        int total = qty * price;
        String tot = String.valueOf(total);

        holder.ordered_itemtotal.setText(tot);
    }


    @Override
    public int getItemCount() {
        return mCheckout.size();
    }

    public class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView ordered_itemname, ordered_itemqty, ordered_itemprice, ordered_itemstatus, ordered_date, ordered_deliverddate, ordered_itemtotal;
        ImageView ordered_itemimage;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);

            ordered_itemimage = itemView.findViewById(R.id.ordered_itemimage);
            ordered_itemqty = itemView.findViewById(R.id.ordered_itemqty);
            ordered_itemprice = itemView.findViewById(R.id.ordered_itemprice);
            ordered_itemstatus = itemView.findViewById(R.id.ordered_itemstatus);
            ordered_itemname = itemView.findViewById(R.id.ordered_itemname);
            ordered_date = itemView.findViewById(R.id.ordered_date);
            ordered_deliverddate = itemView.findViewById(R.id.ordered_deliverddate);
            ordered_itemtotal = itemView.findViewById(R.id.ordered_itemtotal);

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

    public interface OnitemClickListener{
        void onItemClick(int position);
    }
    public void setOnClickListener(OnitemClickListener listener)
    {
        mlistener = listener;
    }
}
