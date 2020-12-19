package com.epayeats.epayeatsuser.Maps;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.epayeats.epayeatsuser.Activity.MainActivity;
import com.epayeats.epayeatsuser.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ChangeLocation_MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SharedPreferences sharedPreferences;
    String user_location;
    String user_lat;
    String user_long;

    BottomSheetBehavior behavior;
    TextView map_selectedloc;
    Button map_save;
    Double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_location__maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        sharedPreferences = getSharedPreferences("data", 0);
        user_location = sharedPreferences.getString("location_name", "");
        user_lat = sharedPreferences.getString("lat","");
        user_long = sharedPreferences.getString("lot","");

        map_selectedloc = findViewById(R.id.map_selectedloc);
        map_save = findViewById(R.id.map_save);

        View bootom = findViewById(R.id.bottom_sheet_map);
        behavior = BottomSheetBehavior.from(bootom);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState)
            {
                switch (newState)
                {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;

                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset)
            {}
        });

        map_save.setOnClickListener(v -> changeLoc());


    }

    private void changeLoc()
    {
        String txt = map_selectedloc.getText().toString();
        if(txt.isEmpty())
        {
            Toast.makeText(this, "Please Select a Location", Toast.LENGTH_SHORT).show();
        }
        else
            {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("location_name", txt);
                editor.putString("lat", String.valueOf(lat));
                editor.putString("lot", String.valueOf(lon));
                editor.apply();

                Intent intent = new Intent(ChangeLocation_MapsActivity.this, MainActivity.class);
                startActivity(intent);

                Toast.makeText(this, "Your location is changed", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(Double.parseDouble(user_lat), Double.parseDouble(user_long));
        mMap.addMarker(new MarkerOptions().position(sydney).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng)
            {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("New Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                String s = latLng.toString();
                String s2 = s.replaceAll("[()lat/lng:]","");
                String[] s3 = s2.split(",");
                Double d1 = Double.valueOf(s3[0]);
                Double d2 = Double.valueOf(s3[1]);

                lat = d1;
                lon = d2;

                try {
                    Geocoder geocoder = new Geocoder(ChangeLocation_MapsActivity.this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(d1, d2, 1);
                    String address = addresses.get(0).getAddressLine(0);
                    map_selectedloc.setText(address);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                setBootom();
            }
        });

    }

    private void setBootom()
    {
        if(behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
        {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        else
        {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }
}