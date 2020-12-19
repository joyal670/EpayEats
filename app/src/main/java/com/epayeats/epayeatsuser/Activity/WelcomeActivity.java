package com.epayeats.epayeatsuser.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.epayeats.epayeatsuser.Adapter.ViewPageAdapter;
import com.epayeats.epayeatsuser.R;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class WelcomeActivity extends AppCompatActivity
{
    ViewPager viewPager;
    DotsIndicator dotsIndicator;
    ViewPageAdapter viewPageAdapter;
    Button welcome_activity_skip_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        viewPager = findViewById(R.id.view_pager);
        dotsIndicator = findViewById(R.id.dot1);
        welcome_activity_skip_btn = findViewById(R.id.welcome_activity_skip_btn);

        viewPageAdapter = new ViewPageAdapter(this);
        viewPager.setAdapter(viewPageAdapter);
        dotsIndicator.setViewPager(viewPager);

        welcome_activity_skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}