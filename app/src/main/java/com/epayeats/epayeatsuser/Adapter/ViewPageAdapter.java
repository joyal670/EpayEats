package com.epayeats.epayeatsuser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.epayeats.epayeatsuser.R;
import com.squareup.picasso.Picasso;

public class ViewPageAdapter extends PagerAdapter
{
    private Context context;
    private LayoutInflater layoutInflater;
    private Integer[] images = {R.drawable.welcome1, R.drawable.welcome2, R.drawable.welcome3, R.drawable.welcome4};

    public ViewPageAdapter(Context context)
    {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.welcome, null);

        ImageView imageView = view.findViewById(R.id.welcome_imageview);
        Picasso.get().load(images[position]).into(imageView);

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view =  (View) object;
        viewPager.removeView(view);
    }
}
