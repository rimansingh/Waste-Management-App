package com.example.projecta.homeScreen.rewards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projecta.R;
import com.example.projecta.databinding.ActivityWasteQuestionsBinding;
import com.example.projecta.model.QuestionDatabase;
import com.example.projecta.model.QuizCategory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Random;

public class WasteQuestionsActivity extends AppCompatActivity {

    private ActivityWasteQuestionsBinding binding;
    private ArrayList<QuestionDatabase> questionsArrayList;
    private LinearProgressIndicator progressIndicator;
    Random random;
    int currentNumber = 0, rightAnswer = 0;
    QuestionDatabase question;
    CountDownTimer timer;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWasteQuestionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressIndicator = findViewById(R.id.progressHorizontal);
        firebaseFirestore = FirebaseFirestore.getInstance();

        questionsArrayList = new ArrayList<>();
        random = new Random();

        timerReset();
        quizQuestions(questionsArrayList);

        onButtonQuit();

    }

    private void onButtonQuit() {
        binding.buttonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(WasteQuestionsActivity.this);
                dialog.setTitle("Quit Quiz");
                dialog.setMessage("Are you sure want to quit this quiz?");
                dialog.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void timerReset(){
        final int oneMin = 1 * 20 * 1000;
        timer = new CountDownTimer(oneMin, 100) {
            @Override
            public void onTick(long l) {
                long finishedSeconds = oneMin - l;
                int total = (int) (((float)finishedSeconds / (float)oneMin) * 100.0);
                progressIndicator.setProgress(total);
            }
            @Override
            public void onFinish() {
            }
        };
    }

    private void quizQuestions(ArrayList<QuestionDatabase> questionsArrayList) {

        String quizCategoryID = getIntent().getStringExtra("quizCategoryID");
        random = new Random();
        int randomNumber = random.nextInt(10);

        firebaseFirestore.collection("QuizCategories")
                .document(quizCategoryID)
                .collection("questionAnswer")
                .whereGreaterThanOrEqualTo("currentNumber", randomNumber)
                .limit(5).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.getDocuments().size() < 5){
                            firebaseFirestore.collection("QuizCategories")
                                    .document(quizCategoryID)
                                    .collection("questionAnswer")
                                    .whereLessThanOrEqualTo("currentNumber", randomNumber)
                                    .limit(5).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                                QuestionDatabase questionDatabase = documentSnapshot.toObject(QuestionDatabase.class);
                                                questionsArrayList.add(questionDatabase);
                                            }
                                            setQuestion();
                                        }
                                    });
                        }
                        else {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                QuestionDatabase questionDatabase = documentSnapshot.toObject(QuestionDatabase.class);
                                questionsArrayList.add(questionDatabase);
                            }
                            setQuestion();
                        }
                    }
                });
    }

    private void setQuestion(){
        if (timer != null){
            timer.cancel();
        }
        timer.start();

        if (currentNumber < questionsArrayList.size()){
            binding.questionCount.setText(String.format("%d/%d", (currentNumber+1), questionsArrayList.size()));
            question = questionsArrayList.get(currentNumber);
            binding.questions.setText(question.getQuestions());
            binding.option1.setText(question.getOption1());
            binding.option2.setText(question.getOption2());
            binding.option3.setText(question.getOption3());
            binding.option4.setText(question.getOption4());
        }
    }

    public void showAnswer(){
        if (question.getAnswer().equals(binding.option1.getText().toString())){
            binding.option1.setBackground(getResources().getDrawable(R.drawable.option_correct));
        }
        else if (question.getAnswer().equals(binding.option2.getText().toString())){
            binding.option2.setBackground(getResources().getDrawable(R.drawable.option_correct));
        }
        else if (question.getAnswer().equals(binding.option3.getText().toString())){
            binding.option3.setBackground(getResources().getDrawable(R.drawable.option_correct));
        }
        else if (question.getAnswer().equals(binding.option4.getText().toString())){
            binding.option4.setBackground(getResources().getDrawable(R.drawable.option_correct));
        }
    }

    public void checkAnswer(TextView textView){
        if (textView.getText().toString().equals(question.getAnswer())){
            rightAnswer++;
            textView.setBackground(getResources().getDrawable(R.drawable.option_correct));
        }
        else {
            showAnswer();
            textView.setBackground(getResources().getDrawable(R.drawable.option_incorrect));
        }
    }

    public void resetQuestion(){
        binding.option1.setBackground(getResources().getDrawable(R.drawable.option_unselect));
        binding.option2.setBackground(getResources().getDrawable(R.drawable.option_unselect));
        binding.option3.setBackground(getResources().getDrawable(R.drawable.option_unselect));
        binding.option4.setBackground(getResources().getDrawable(R.drawable.option_unselect));
    }

    public void onClickNextQuestion(View view){
        switch (view.getId()){
            case R.id.option1:
            case R.id.option2:
            case R.id.option3:
            case R.id.option4:
                if (timer != null){
                    timer.cancel();
                }
                TextView selected = (TextView) view;
                checkAnswer(selected);
                break;

            case R.id.buttonContinue:
                resetQuestion();
                if (currentNumber <= questionsArrayList.size()){
                    currentNumber++;
                    setQuestion();
                }
                else {
                    Intent intent = new Intent(WasteQuestionsActivity.this, QuizResultActivity.class);
                    intent.putExtra("rightAnswer", rightAnswer);
                    intent.putExtra("totalQuestions", questionsArrayList.size());
                    startActivity(intent);
                    Toast.makeText(this, "Finished", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        String quizCategoryID = getIntent().getStringExtra("quizCategoryID");
        firebaseFirestore.collection("QuizCategories")
                .document(quizCategoryID).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String categoryName = documentSnapshot.getString("quizCategoryName");

                                binding.categoryTitle.setText(categoryName);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Data", "onFailure: "+e.getMessage());
                    }
                });

        super.onStart();
    }
}