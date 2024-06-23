package com.example.projecta.homeScreen.complaints;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projecta.R;
import com.example.projecta.databinding.ActivityComplaintFileBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ComplaintFile extends AppCompatActivity {

    private ActivityComplaintFileBinding binding;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private TextView textView;
    private static final int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComplaintFileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        textView = findViewById(R.id.your_location);

        binding.location.setVisibility(View.GONE);

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ComplaintFile.this);
        binding.fileComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentLocation();
            }
        });
    }

    private void getCurrentLocation() {
        if(ContextCompat.checkSelfPermission(ComplaintFile.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>()
                    {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null){
                                try {
                                    Geocoder geocoder = new Geocoder
                                            (ComplaintFile.this, Locale.getDefault());
                                    List<Address> addresses = geocoder.getFromLocation(
                                            location.getLatitude(),
                                            location.getLongitude(),
                                            1
                                    );
                                    textView.setText(addresses.get(0).getAddressLine(0));
                                    binding.location.setVisibility(View.VISIBLE);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });
        }
        else{
            askPermission();
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(ComplaintFile.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                getCurrentLocation();
            }
            else{
                Toast.makeText(ComplaintFile.this,
                        "Please provide the required permission",
                        Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }
}