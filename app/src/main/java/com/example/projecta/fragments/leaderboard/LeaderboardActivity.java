package com.example.projecta.fragments.leaderboard;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.projecta.R;
import com.example.projecta.model.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    List<FirebaseDatabase> list;
    leaderboardAdapter adapter;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        recyclerView = findViewById(R.id.leaderBoardRecyclerView);
        progressBar = findViewById(R.id.leaderBoardProgressBar);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        CollectionReference collection = firebaseFirestore.collection("Users");
        DocumentReference document = collection.document(firebaseUser.getUid());

        list = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        collection.orderBy("earnedPoints").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("FirestoreListener", "Listen failed.", error);
                    return;
                }

                for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                    FirebaseDatabase data = documentSnapshot.toObject(FirebaseDatabase.class);
//                    list.add(data);

//                    String earnedPoints = documentSnapshot.getString("earnedPoints");
//                    data.setEarnedPoints(earnedPoints);
                    Long earnedPointsLong = documentSnapshot.getLong("earnedPoints");
                    data.setEarnedPoints(earnedPointsLong);
                    list.add(data);
                }
                adapter = new leaderboardAdapter(list, LeaderboardActivity.this);
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}