package com.example.payapa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.widget.Toast;


import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class ResultView extends View {

    private RectF rect;
    private Paint mPaintText;
    private int textHeight;
    private Paint mPaintRectangle;
    private ArrayList<Result> mResults;
    private ArrayList<String> savedResults;
    private FirebaseFirestore db;

    public ResultView(Context context) {
        super(context);
        initializeFirestore();
    }

    public ResultView(Context context, AttributeSet attrs){
        super(context, attrs);
        initializeFirestore();
        mPaintRectangle = new Paint();
        mPaintRectangle.setColor(Color.YELLOW);
        rect = new RectF();  // Initialize rect
        initTextPaint();
        savedResults = new ArrayList<>();
    }

    private void initializeFirestore() {
        db = FirebaseFirestore.getInstance();
    }

//    protected void updateTextView(String newText) {
//        TextView textViewDetect = getRootView().findViewById(R.id.textViewDetect);
//        textViewDetect.setText(newText);
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mResults == null || mResults.size() == 0) {
            Toast.makeText(getContext(), "Please try another image <3", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Result result : mResults) {
            mPaintRectangle.setStrokeWidth(8);
            mPaintRectangle.setStyle(Paint.Style.STROKE);
            mPaintRectangle.setColor(ContextCompat.getColor(getContext(), R.color.dgreen));
            canvas.drawRect(result.rect, mPaintRectangle);
            rect.set(result.rect);

            String labelDetected = PrePostProcessor.mClasses[result.classIndex];
            labelDetected = labelDetected.replace("_", " ");
            labelDetected = labelDetected.toUpperCase();
            String confidenceText = String.valueOf(Math.round(result.score * 10000) / 100) + "%";

            // Concatenate the percentage and label text
            String combinedText = confidenceText + " " + labelDetected;


            // Calculate text width
            float textWidth = mPaintText.measureText(combinedText);

            // Calculate the position for the combined text above the bounding box
            float textX = rect.centerX() - textWidth / 2;

            float padding = 10; // Adjust the padding value as needed
            float textY = rect.top - textHeight - padding;

            canvas.drawText(combinedText, textX, textY, mPaintText);

            savedResults.add(combinedText);

        }

        saveResultsToFirestore();
        navigateToGamesActivity();
    }

    private void initTextPaint() {
        mPaintText = new Paint();
        mPaintText.setColor(ContextCompat.getColor(getContext(), R.color.white));
        mPaintText.setStyle(Paint.Style.FILL);
        mPaintText.setTextSize(28);
        mPaintText.setAntiAlias(true);
        mPaintText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

    }

    public void setResults(ArrayList<Result> results) {
        mResults = results;
        if (savedResults != null) {
            savedResults.clear();
        }
    }

    private void saveResultsToFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String timestamp = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());

        for (String result : savedResults) {
            // Split the result into percentage and label
            String[] parts = result.split(" ", 2); // Assuming format: "95% LABEL_NAME"
            if (parts.length < 2) continue;

            String percentage = parts[0];
            String label = parts[1];

            // Prepare data for Firestore
            Map<String, Object> data = new HashMap<>();
            data.put("label", label);
            data.put("percentage", percentage);
            data.put("timestamp", timestamp);

            // Save each result as a separate document
            db.collection("detected_labels")
                    .add(data)
                    .addOnSuccessListener(documentReference ->
                            Toast.makeText(getContext(), "Result saved to Firestore", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(getContext(), "Error saving result: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void navigateToGamesActivity() {
        Context context = getContext();
        if (context != null) {
            Intent intent = new Intent(context, QuestionActivity.class);
            context.startActivity(intent);
        }
    }

    // Method to get saved results for external use
    public ArrayList<String> getSavedResults() {
        return savedResults;
    }
}
