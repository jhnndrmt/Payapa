package com.example.payapa;

import android.content.Context;
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


import java.util.ArrayList;


public class ResultView extends View {

    private RectF rect;
    private Paint mPaintText;
    private int textHeight;
    private Paint mPaintRectangle;
    private ArrayList<Result> mResults;

    public ResultView(Context context) {
        super(context);
    }

    public ResultView(Context context, AttributeSet attrs){
        super(context, attrs);
        mPaintRectangle = new Paint();
        mPaintRectangle.setColor(Color.YELLOW);
        rect = new RectF();  // Initialize rect
        initTextPaint();
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

        }
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
    }
}
