package com.example.payapa;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionActivity extends AppCompatActivity {

    private TextView questionText;
    private Button choiceNone, choiceLittle, choiceSome, choiceMost, choiceAll;

    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private static final int NONE = 1;
    private static final int LITTLE = 2;
    private static final int SOME = 3;
    private static final int MOST = 4;
    private static final int ALL = 5;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private int cumulativeScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        questionText = findViewById(R.id.question_text);
        choiceNone = findViewById(R.id.choice_none);
        choiceLittle = findViewById(R.id.choice_little);
        choiceSome = findViewById(R.id.choice_some);
        choiceMost = findViewById(R.id.choice_most);
        choiceAll = findViewById(R.id.choice_all);

        questions = loadQuestionsFromJson();
        displayQuestion();

        View.OnClickListener choiceClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int valuation = 0;

                if (v.getId() == R.id.choice_none) {
                    valuation = NONE;
                } else if (v.getId() == R.id.choice_little) {
                    valuation = LITTLE;
                } else if (v.getId() == R.id.choice_some) {
                    valuation = SOME;
                } else if (v.getId() == R.id.choice_most) {
                    valuation = MOST;
                } else if (v.getId() == R.id.choice_all) {
                    valuation = ALL;
                }

                // Set the valuation for the current question
                Question currentQuestion = questions.get(currentQuestionIndex);
                currentQuestion.setValuation(valuation);

                // Save the choice immediately
                saveChoice(currentUser.getUid(), currentQuestion);

                // Move to the next question
                nextQuestion();
            }
        };

        choiceNone.setOnClickListener(choiceClickListener);
        choiceLittle.setOnClickListener(choiceClickListener);
        choiceSome.setOnClickListener(choiceClickListener);
        choiceMost.setOnClickListener(choiceClickListener);
        choiceAll.setOnClickListener(choiceClickListener);
    }

    private void saveChoice(String userId, Question currentQuestion) {
        int valuation = currentQuestion.getValuation();
        String questionId = String.valueOf(currentQuestion.getId());

        int score = calculateScore(valuation);

        cumulativeScore += score;

        String stressLevel = determineStressLevel(cumulativeScore);

        Map<String, Object> userData = new HashMap<>();
        userData.put("score", cumulativeScore);
        userData.put("stress_level", stressLevel);

        db.collection("questions")
                .document(userId)
                .set(userData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(QuestionActivity.this, "Choice saved successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(QuestionActivity.this, "Error saving choice: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private int calculateScore(int valuation) {
        return valuation;
    }

    private String determineStressLevel(int score) {
        if (score >= 1 && score <= 19) {
            return "Low";
        } else if (score >= 20 && score <= 24) {
            return "Mid";
        } else if (score >= 25 && score <= 29) {
            return "High";
        } else if (score >= 30 && score <= 50) {
            return "Severe";
        }
        return null;
    }

    private List<Question> loadQuestionsFromJson() {
        List<Question> questions = new ArrayList<>();
        String json = loadJsonFromAssets();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray questionsArray = jsonObject.getJSONArray("questions");

            for (int i = 0; i < questionsArray.length(); i++) {
                JSONObject questionObject = questionsArray.getJSONObject(i);
                int id = questionObject.getInt("id");
                String text = questionObject.getString("text");
                questions.add(new Question(id, text, i + 1));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return questions;
    }

    private String loadJsonFromAssets() {
        String json = null;
        try {
            InputStream inputStream = getAssets().open("questions.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    @SuppressLint("SetTextI18n")
    private void displayQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            questionText.setText(currentQuestion.getText());

            TextView questionNoText = findViewById(R.id.question_no);
            questionNoText.setText("Question " + currentQuestion.getQuestionNumber() + " of " + questions.size());
        }
    }

    private void nextQuestion() {
        currentQuestionIndex++;
        if (currentQuestionIndex < questions.size()) {
            displayQuestion();
        } else {
            // Show a Toast message
            Toast.makeText(QuestionActivity.this, "Thank you for completing the survey!", Toast.LENGTH_LONG).show();

            String stressLevel = determineStressLevel(cumulativeScore);

            // Navigate to HomepageActivity after showing the Toast
            Intent intent = new Intent(QuestionActivity.this, SuggestionActivity.class);
            intent.putExtra("stressLevel", stressLevel);
            startActivity(intent);

            finish();
        }
    }

    private void disableChoices() {
        choiceNone.setEnabled(false);
        choiceLittle.setEnabled(false);
        choiceSome.setEnabled(false);
        choiceMost.setEnabled(false);
        choiceAll.setEnabled(false);
    }

    private class Question {
        private int id;
        private String text;
        private int valuation;
        private int questionNumber;
        public Question(int id, String text, int questionNumber) {
            this.id = id;
            this.text = text;
            this.questionNumber = questionNumber;
        }

        public int getId() {
            return id;
        }

        public String getText() {
            return text;
        }
        public int getValuation() { return valuation; }
        public void setValuation(int valuation) {
            this.valuation = valuation;
        }
        public int getQuestionNumber() {
            return questionNumber;
        }
    }
}
