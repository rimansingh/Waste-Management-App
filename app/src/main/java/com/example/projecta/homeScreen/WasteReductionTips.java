package com.example.projecta.homeScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.projecta.MainActivity;
import com.example.projecta.R;
import com.example.projecta.databinding.ActivityWasteBinLocatorBinding;
import com.example.projecta.databinding.ActivityWasteReductionTipsBinding;

import java.util.ArrayList;

public class WasteReductionTips extends AppCompatActivity {

    private ActivityWasteReductionTipsBinding binding;
    private ImageSlider imageSlider;
    private ArrayList<SlideModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWasteReductionTipsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WasteReductionTips.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        imageSlider = findViewById(R.id.imageSlider);
        list = new ArrayList<>();

        list.add(new SlideModel(R.drawable.reduce_waste, ScaleTypes.FIT));
        list.add(new SlideModel(R.drawable.reuse, ScaleTypes.FIT));
        list.add(new SlideModel(R.drawable.recycle, ScaleTypes.FIT));

        imageSlider.setImageList(list, ScaleTypes.FIT);

    }
}