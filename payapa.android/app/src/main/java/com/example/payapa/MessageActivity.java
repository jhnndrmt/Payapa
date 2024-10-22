package com.example.payapa;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.webkit.WebViewAssetLoader;
import androidx.webkit.WebViewClientCompat;

public class MessageActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        WebView webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true); // Enable JavaScript if needed
        webView.setWebViewClient(new WebViewClient()); // Use default WebViewClient

        // Load the deployed URL
        webView.loadUrl("http://195.26.255.19:3000/");  // Replace with your deployed URL
    }
}