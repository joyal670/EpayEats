package com.epayeats.epayeatsuser.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.epayeats.epayeatsuser.Database.MobUser;
import com.epayeats.epayeatsuser.R;
import com.raizlabs.android.dbflow.sql.language.Delete;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class OrderPlaced_Activity extends AppCompatActivity
{
    TextView placed_order_id;
    Button placed_order_continuebtn;
    SharedPreferences sharedPreferences;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed_);

        sharedPreferences = getSharedPreferences("data", 0);
        String a1 = sharedPreferences.getString("userid", "");

        placed_order_id = findViewById(R.id.placed_order_id);
        placed_order_continuebtn = findViewById(R.id.placed_order_continuebtn);
        placed_order_id.setText(a1);

        placed_order_continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                notification();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void notification()
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setContentTitle("New Order")
                .setContentText("Thank you for your order, we will contact you soon.")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        Uri path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(path);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Your_Channel_ID";
            NotificationChannel channel = new NotificationChannel(channelId, "Chanel Human redable Text", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }
        notificationManager.notify(1, builder.build());

        Delete.table(MobUser.class);

        Intent intent = new Intent(OrderPlaced_Activity.this, MainActivity.class);
        startActivity(intent);
        finish();



    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        notification();
    }

}