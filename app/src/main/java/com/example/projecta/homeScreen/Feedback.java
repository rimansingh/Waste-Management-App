package com.example.projecta.homeScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.projecta.MainActivity;
import com.example.projecta.databinding.ActivityFeedbackBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Feedback extends AppCompatActivity {

    private ActivityFeedbackBinding binding;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedbackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        FirebaseApp.initializeApp(this);

        onButtonClick();
        onButtonBack();
    }

    private void onButtonClick() {
        binding.feedbackSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.feedbackEmail.getText().toString();
                String message = binding.feedbackMessage.getText().toString();

                if (email.isEmpty()){
                    binding.feedbackEmail.setError("Required");
                    binding.feedbackSubmit.setEnabled(false);
                }
                else{
                    binding.feedbackEmail.setError(null);
                    binding.feedbackSubmit.setEnabled(true);
                }

            }
        });
    }

    private void onButtonBack() {
        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Feedback.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}