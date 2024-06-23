package com.example.projecta.loginActivities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.projecta.R;
import com.example.projecta.databinding.ActivityOtpVerificationBinding;
import com.example.projecta.model.FirebaseDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class OtpVerification extends AppCompatActivity {

    private ActivityOtpVerificationBinding binding;
    private String getPhoneNumber;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private String verificationID;
    private OtpTextView otpTextView;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        binding = ActivityOtpVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressBar = findViewById(R.id.progressBar);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        otpTextView = findViewById(R.id.otpBox);

        verificationID = getIntent().getStringExtra("id");

        getPhoneNumber = getIntent().getStringExtra("phoneNumber");

        binding.number.setText("Please type the verification code\n sent to " + getPhoneNumber);

        binding.buttonContinue.setTextColor(Color.GRAY);
        binding.buttonContinue.setBackgroundResource(R.drawable.phone_auth_button_disable);

        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                binding.buttonContinue.setEnabled(false);
                binding.buttonContinue.setTextColor(Color.GRAY);
                binding.buttonContinue.setBackgroundResource(R.drawable.phone_auth_button_disable);
            }

            @Override
            public void onOTPComplete(String otp) {
                binding.buttonContinue.setEnabled(true);
                binding.buttonContinue.setTextColor(Color.WHITE);
                binding.buttonContinue.setBackgroundResource(R.drawable.phone_auth_button_enable);

                binding.buttonContinue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, otp);
                        closeKeyBoard();
                        authenticationUser(credential);
                    }
                });
            }
        });
    }

    private void authenticationUser(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            progressBar.setVisibility(View.INVISIBLE);
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            Intent intent = new Intent(OtpVerification.this, UserDetails.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finishAffinity();
                            if (firebaseUser != null){
                                String userID = firebaseAuth.getCurrentUser().getUid();
                                FirebaseDatabase firebaseDatabase = new FirebaseDatabase(
                                        userID,
                                        "",
                                        "",
                                        firebaseUser.getPhoneNumber(),
                                        "",
                                        "",
                                        0L
                                );
                                DocumentReference documentReference = firebaseFirestore.collection("Users").document(userID);
                                documentReference.set(firebaseDatabase).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                    }
                                });
                            }
                        }
                        else
                        {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(OtpVerification.this, "Wrong code", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(OtpVerification.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void closeKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}