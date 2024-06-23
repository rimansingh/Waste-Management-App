package com.example.projecta.model;

public class QuizCategory {
    private String quizCategoryID;
    private String quizCategoryName;
    private String quizCategoryImage;

    public QuizCategory() {
    }

    public QuizCategory(String quizCategoryID, String quizCategoryName, String quizCategoryImage) {
        this.quizCategoryID = quizCategoryID;
        this.quizCategoryName = quizCategoryName;
        this.quizCategoryImage = quizCategoryImage;
    }

    public String getQuizCategoryID() {
        return quizCategoryID;
    }

    public void setQuizCategoryID(String quizCategoryID) {
        this.quizCategoryID = quizCategoryID;
    }

    public String getQuizCategoryName() {
        return quizCategoryName;
    }

    public void setQuizCategoryName(String quizCategoryName) {
        this.quizCategoryName = quizCategoryName;
    }

    public String getQuizCategoryImage() {
        return quizCategoryImage;
    }

    public void setQuizCategoryImage(String quizCategoryImage) {
        this.quizCategoryImage = quizCategoryImage;
    }
}
