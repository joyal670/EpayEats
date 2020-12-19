package com.epayeats.epayeatsuser.Maps;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.epayeats.epayeatsuser.Activity.Login_Activity;
import com.epayeats.epayeatsuser.Activity.MainActivity;
import com.epayeats.epayeatsuser.PlacesApi.PlaceAutoSuggestAdapter;
import com.epayeats.epayeatsuser.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NewLocation_MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SharedPreferences sharedPreferences;
    String user_location;
    String user_lat;
    String user_long;

    BottomSheetBehavior behavior;
    TextView map_selectedloc_new;
    Button map_new_save;
    Double lat, lon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_location__maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.newmap);
        mapFragment.getMapAsync(this);

        map_selectedloc_new = findViewById(R.id.map_selectedloc_new);
        map_new_save = findViewById(R.id.map_new_save);

        View bootom = findViewById(R.id.bottom_sheet_map_new);
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

        map_new_save.setOnClickListener(v -> changeLoc());

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

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
                    Geocoder geocoder = new Geocoder(NewLocation_MapsActivity.this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(d1, d2, 1);
                    String address = addresses.get(0).getAddressLine(0);
                    map_selectedloc_new.setText(address);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                setBootom();
            }
        });

    }

    private void changeLoc()
    {
        String txt = map_selectedloc_new.getText().toString();
        if(txt.isEmpty())
        {
            Toast.makeText(this, "Please Select a Location", Toast.LENGTH_SHORT).show();
        }
        else
        {
            sharedPreferences = getSharedPreferences("data", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("location_name", txt);
            editor.putString("lat", String.valueOf(lat));
            editor.putString("lot", String.valueOf(lon));
            editor.apply();

            Intent intent = new Intent(NewLocation_MapsActivity.this, Login_Activity.class);
            startActivity(intent);

            Toast.makeText(this, "Your location is Selected", Toast.LENGTH_SHORT).show();
        }
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