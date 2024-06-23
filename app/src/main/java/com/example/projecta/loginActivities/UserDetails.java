package com.example.projecta.loginActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projecta.MainActivity;
import com.example.projecta.databinding.ActivityUserDetailsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class UserDetails extends AppCompatActivity {

    private ActivityUserDetailsBinding binding;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        if(firebaseUser != null){
            getUserInfo();
        }

        uploadDetails();
    }

    private void getUserInfo(){
        firebaseFirestore.collection("Users")
                .document(firebaseUser.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String userPhone = documentSnapshot.getString("userPhone");
                        binding.userPhoneNumber.setText(userPhone);
                    }
                });
    }

    private void uploadDetails() {
        binding.buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(binding.firstName.getText().toString()) ){
                    binding.firstName.setError("Name cannot be empty.");
                }
                else if(TextUtils.isEmpty(binding.lastName.getText().toString()) ){
                    binding.lastName.setError("Name cannot be empty.");
                }
                else {
                    uploadToFirebaseDatabase();
                }
            }
        });
        binding.userImageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPhoneGallery();
            }
        });
    }

    private void openPhoneGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select image"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null)
        {
            imageUri = data.getData();
            Glide.with(UserDetails.this).load(imageUri).into(binding.userImage);
        }
    }

    private void uploadToFirebaseDatabase() {
        closeKeyBoard();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference reference = storageReference.child("images/" + randomKey);

        reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful());
                Uri downloadUrl = urlTask.getResult();

                final String download_url = String.valueOf(downloadUrl);

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("firstName", binding.firstName.getText().toString());
                hashMap.put("lastName", binding.lastName.getText().toString());
                hashMap.put("imgProfile", download_url);

                Snackbar.make(findViewById(android.R.id.content), "Uploaded Successfully", Snackbar.LENGTH_LONG).show();

                firebaseFirestore.collection("Users").document(firebaseUser.getUid()).update(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(),"Successfully Upload", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to Upload", Toast.LENGTH_LONG).show();
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