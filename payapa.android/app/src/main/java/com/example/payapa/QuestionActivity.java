package com.example.payapa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends AppCompatActivity {

    private TextView questionText;
    private Button choiceNone, choiceLittle, choiceSome, choiceMost, choiceAll;

    private List<Question> questions;
    private int currentQuestionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

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
                nextQuestion();
            }
        };

        choiceNone.setOnClickListener(choiceClickListener);
        choiceLittle.setOnClickListener(choiceClickListener);
        choiceSome.setOnClickListener(choiceClickListener);
        choiceMost.setOnClickListener(choiceClickListener);
        choiceAll.setOnClickListener(choiceClickListener);
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
                questions.add(new Question(id, text));
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

    private void displayQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            questionText.setText(currentQuestion.getText());
        }
    }

    private void nextQuestion() {
        currentQuestionIndex++;
        if (currentQuestionIndex < questions.size()) {
            displayQuestion();
        } else {
            // Show a Toast message
            Toast.makeText(QuestionActivity.this, "Thank you for completing the survey!", Toast.LENGTH_LONG).show();

            // Navigate to HomepageActivity after showing the Toast
            Intent intent = new Intent(QuestionActivity.this, HomepageActivity.class);
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

        public Question(int id, String text) {
            this.id = id;
            this.text = text;
        }

        public int getId() {
            return id;
        }

        public String getText() {
            return text;
        }
    }
}
