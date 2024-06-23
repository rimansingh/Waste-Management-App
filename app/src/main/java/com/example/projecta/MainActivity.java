package com.example.projecta;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import com.example.projecta.databinding.ActivityMainBinding;
import com.example.projecta.fragments.HomeFragment;
import com.example.projecta.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private BottomNavigationView bottomNavigationView;
    private FirebaseFirestore firebaseFirestore;

    HomeFragment home = new HomeFragment();
    ProfileFragment profile = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseFirestore = FirebaseFirestore.getInstance();

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, home).commit();

        bottomNavigationView.setOnItemSelectedListener
                (new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){

                    case R.id.home:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, home)
                                .commit();
                        binding.topMenu
                                .setVisibility(View.VISIBLE);
                        return true;

                    case R.id.profile:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, profile)
                                .commit();
                        binding.topMenu
                                .setVisibility(View.GONE);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.recreate();
    }

    @Override
    protected void onStart() {

        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);

        int hours = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);

        if(hours>=0 && hours<=11){
            binding.greeting.setText("Good Morning!");
        }else if(hours>=12 && (hours<=15)){
            binding.greeting.setText("Good Afternoon!");
        }else if(hours>=16 && hours<=20){
            binding.greeting.setText("Good Evening!");
        }else if(hours>=21 && hours<=23){
            binding.greeting.setText("Good Night!");
        }

        super.onStart();
    }
}