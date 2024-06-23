package com.example.projecta.homeScreen.rewards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.projecta.R;
import com.example.projecta.databinding.ActivityQuizResultBinding;
import com.example.projecta.homeScreen.Reward;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class QuizResultActivity extends AppCompatActivity {

    ActivityQuizResultBinding binding;
    int Points = 10;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseFirestore = FirebaseFirestore.getInstance();

        int rightAnswer = getIntent().getIntExtra("rightAnswer", 0);
        int totalQuestions = getIntent().getIntExtra("totalQuestions", 0);

        int points = rightAnswer * Points;

        binding.correctAnswer.setText(String.format("%d/%d", rightAnswer, totalQuestions));
        binding.earnedPoints.setText(String.valueOf(points));

        firebaseFirestore.collection("Users")
                .document(FirebaseAuth.getInstance().getUid())
                .update("earnedPoints", FieldValue.increment(points));

        buttonNewQuiz();
    }

    private void buttonNewQuiz() {
        binding.buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizResultActivity.this, Reward.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        return;
    }
}