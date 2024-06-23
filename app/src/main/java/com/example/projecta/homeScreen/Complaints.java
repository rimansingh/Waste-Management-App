package com.example.projecta.homeScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.projecta.MainActivity;
import com.example.projecta.R;
import com.example.projecta.databinding.ActivityComplaintsBinding;
import com.example.projecta.databinding.ActivityFeedbackBinding;
import com.example.projecta.databinding.ActivityWasteBinLocatorBinding;
import com.example.projecta.homeScreen.complaints.ComplaintFile;

public class Complaints extends AppCompatActivity {

    private ActivityComplaintsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComplaintsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Complaints.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        binding.fileComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Complaints.this, ComplaintFile.class);
                startActivity(intent);
            }
        });
    }
}