package com.example.projecta.homeScreen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.projecta.MainActivity;
import com.example.projecta.R;
import com.example.projecta.adapter.Adapter_Reward;
import com.example.projecta.databinding.ActivityRewardBinding;
import com.example.projecta.homeScreen.rewards.QuizResultActivity;
import com.example.projecta.model.QuizCategory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Reward extends AppCompatActivity {

    private ActivityRewardBinding binding;
    private FirebaseFirestore firebaseFirestore;
    RecyclerView recyclerView;
    Adapter_Reward adapter;
    List<Integer> image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRewardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseFirestore = FirebaseFirestore.getInstance();

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Reward.this, MainActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.rewardRecycleView);

        ArrayList<QuizCategory> categories = new ArrayList<>();

        adapter = new Adapter_Reward(this, categories);

        firebaseFirestore.collection("QuizCategories")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException error) {
                        categories.clear();
                        for (DocumentSnapshot documentSnapshot : value.getDocuments()){
                            QuizCategory quizCategory = documentSnapshot.toObject(QuizCategory.class);
                            quizCategory.setQuizCategoryID(documentSnapshot.getId());
                            categories.add(quizCategory);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,
                2, GridLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        getPointsFromDatabase();
    }

    private void getPointsFromDatabase() {
        firebaseFirestore.collection("Users")
                .document(FirebaseAuth.getInstance().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful())
                        {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()){
                                int dbValue = documentSnapshot.getLong("earnedPoints").intValue();
                                binding.earnedPoints.setText(String.valueOf(dbValue));
                            }
                        }
                    }
                });
    }

    @Override
    protected void onRestart() {
        this.recreate();  // refresh activity
        super.onRestart();
    }
}