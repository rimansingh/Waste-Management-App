package com.example.projecta.loginActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.projecta.MainActivity;
import com.example.projecta.R;
import com.example.projecta.databinding.ActivityPhoneAuthBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;


import java.util.concurrent.TimeUnit;

public class PhoneAuth extends AppCompatActivity {

    private ActivityPhoneAuthBinding binding;
    private ProgressBar progressBar;
    private String number, phoneNumber;
    private FirebaseAuth firebaseAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        binding = ActivityPhoneAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressBar = findViewById(R.id.progressBar);
        firebaseAuth = FirebaseAuth.getInstance();

        binding.buttonContinue.setTextColor(Color.GRAY);
        binding.buttonContinue.setBackgroundResource(R.drawable.phone_auth_button_disable);

        binding.userPhoneNumber.addTextChangedListener(btnColorChanger);

        sendOtp();
    }

    private TextWatcher btnColorChanger = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.toString().trim().length() < 9) {
                binding.buttonContinue.setEnabled(false);
            } else {
                binding.buttonContinue.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().trim().length() > 8) {
                binding.buttonContinue.setTextColor(Color.WHITE);
                binding.buttonContinue.setBackgroundResource(R.drawable.phone_auth_button_enable);

                binding.buttonContinue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressBar.setVisibility(View.VISIBLE);
                        closeKeyBoard();

                        number = binding.userPhoneNumber.getText().toString().trim();
                        phoneNumber = "+" + binding.ccp.getSelectedCountryCode() + number;

                        PhoneAuthOptions options =
                                PhoneAuthOptions.newBuilder(firebaseAuth)
                                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                        .setActivity(PhoneAuth.this)                 // (optional) Activity for callback binding
                                        // If no activity is passed, reCAPTCHA verification can not be used.
                                        .setCallbacks(mCallback)          // OnVerificationStateChangedCallbacks
                                        .build();
                        PhoneAuthProvider.verifyPhoneNumber(options);

                    }
                });
            }
            else
            {
                binding.buttonContinue.setTextColor(Color.GRAY);
                binding.buttonContinue.setBackgroundResource(R.drawable.phone_auth_button_disable);
            }
        }
    };

    private void sendOtp(){
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted( PhoneAuthCredential credential) {
                authenticationUser(credential);
            }
            @Override
            public void onVerificationFailed( FirebaseException e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(PhoneAuth.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent( String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(PhoneAuth.this, OtpVerification.class);
                        intent.putExtra("id", verificationId);
                        intent.putExtra("phoneNumber", phoneNumber);
                        closeKeyBoard();
                        startActivity(intent);
                    }
                }, 3000);
            }
        };
    }

    private void authenticationUser(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(PhoneAuth.this, OtpVerification.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(PhoneAuth.this, "Error", Toast.LENGTH_SHORT).show();
                        }
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

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuth.getCurrentUser() != null){
            Intent intent = new Intent(PhoneAuth.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}