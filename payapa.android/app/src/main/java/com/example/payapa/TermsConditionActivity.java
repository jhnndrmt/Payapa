package com.example.payapa;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TermsConditionActivity extends AppCompatActivity {
    private Button acceptButton;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_condition);

        WebView webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true); // Enable JavaScript if needed
        webView.setWebViewClient(new WebViewClient()); // Use default WebViewClient

        // Load the deployed URL
        webView.loadUrl("http://195.26.255.19:3000/temp");  // Replace with your deployed URL
    }

}
