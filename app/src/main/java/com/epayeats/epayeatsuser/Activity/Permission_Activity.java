package com.epayeats.epayeatsuser.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.epayeats.epayeatsuser.Maps.ChangeLocation_MapsActivity;
import com.epayeats.epayeatsuser.Maps.NewLocation_MapsActivity;
import com.epayeats.epayeatsuser.R;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Permission_Activity extends AppCompatActivity implements LocationListener
{

    TextView text_location;
    Button button_location, activity_go_btn;
    LocationManager locationManager;
    boolean GpsStatus;
    String lang;
    String lot;

    Button permission_activity_manual_location_btn;

    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching you current location...");

        text_location = findViewById(R.id.text_location);
        button_location = findViewById(R.id.button_location);
        activity_go_btn = findViewById(R.id.activity_go_btn);
        permission_activity_manual_location_btn = findViewById(R.id.permission_activity_manual_location_btn);

        checkPermission();

        button_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                getCurrentLocation();
            }
        });

        activity_go_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SharedPreferences sharedPreferences;
                sharedPreferences = getSharedPreferences("data", 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("location_name", text_location.getText().toString());
                editor.putString("lat", lang);
                editor.putString("lot", lot);
                editor.apply();

                Intent intent = new Intent(Permission_Activity.this, Login_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        permission_activity_manual_location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Permission_Activity.this, NewLocation_MapsActivity.class);
                startActivity(intent);
            }
        });

    }

    private void checkPermission()
    {
        Permissions.check(Permission_Activity.this, Manifest.permission.ACCESS_FINE_LOCATION, null, new PermissionHandler() {
            @Override
            public void onGranted()
            {
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions)
            {
                Toast.makeText(Permission_Activity.this, "Pemission Denied", Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onBlocked(Context context, ArrayList<String> blockedList)
            {
                Toast.makeText(Permission_Activity.this, "Permission Blocked", Toast.LENGTH_SHORT).show();
                return super.onBlocked(context, blockedList);

            }
        });
    }

    private void getCurrentLocation()
    {

        if(CheckGpsStatus())
        {
            try {

                locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(Permission_Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, Permission_Activity.this);

                text_location.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            SweetAlertDialog dialog1 = new SweetAlertDialog(Permission_Activity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops....")
                    .setContentText("Location permission not granted, please turn on location")
                    .showCancelButton(false)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog)
                        {
                            Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent1);
                            sweetAlertDialog.dismiss();
                            text_location.setVisibility(View.VISIBLE);
                        }
                    });

            dialog1.show();

        }

    }

    public boolean CheckGpsStatus()
    {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        return GpsStatus;
    }

    @Override
    public void onLocationChanged(Location location)
    {
        lang = String.valueOf(location.getLatitude());
        lot = String.valueOf(location.getLongitude());

        try {
            Geocoder geocoder = new Geocoder(Permission_Activity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
            String address = addresses.get(0).getAddressLine(0);
            text_location.setText(address);
            activity_go_btn.setVisibility(View.VISIBLE);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            SharedPreferences sharedPreferences = getSharedPreferences("data", 0);

            boolean logg = sharedPreferences.getBoolean("login_status",false);
            if(logg == true)
            {
                Intent intent = new Intent(Permission_Activity.this, MainActivity.class);
                startActivity(intent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure want to Exit?");


        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

    }
}