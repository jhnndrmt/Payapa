package com.example.payapa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.payapa.CameraController;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.pytorch.IValue;
import org.pytorch.LiteModuleLoader;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainDetectActivity extends AppCompatActivity {
    private int mImageIndex = 0;
    private String[] mTestImages;
    final private String imagesPath = "examples/";
    private Button buttonTest;

    private ImageView mImageView;
    private ResultView mResultView;
    private Button mButtonDetect;
    private TextView textViewDetection;
    private ProgressBar mProgressBar;
    private Bitmap mBitmap = null;
    private Module mModule = null;  // our model
    private float mImgScaleX, mImgScaleY, mIvScaleX, mIvScaleY, mStartX, mStartY;

    CameraController cameraController;
    private PreviewView previewView;
    final int PERMISSION_REQUEST_CODE = 1;

    // BroadcastReceiver to trigger "Detect" button click
    private BroadcastReceiver detectButtonReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("trigger_detect".equals(intent.getAction())) {
                // Programmatically trigger the "Detect" button click event
                mButtonDetect = findViewById(R.id.detectButton);
                mButtonDetect.performClick();
            }
        }
    };

    // Function to get path of assets as YOLO weights
    public static String assetFilePath(Context context, String assetName) throws IOException {
        File file = new File(context.getFilesDir(), assetName);
        if (file.exists() && file.length() > 0) {
            return file.getAbsolutePath();
        }

        try (InputStream is = context.getAssets().open(assetName)) {
            try (OutputStream os = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    os.write(buffer, 0, read);
                }
                os.flush();
            }
            return file.getAbsolutePath();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check permission to use the camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }



        // Set layout
        setContentView(R.layout.activity_main);
//        textViewDetection = findViewById(R.id.textViewDetect);


        // Show image
        mImageView = findViewById(R.id.imageView);

        // Retrieve the captured image from the intent
        Bitmap capturedImage = getIntent().getParcelableExtra("captured_image");

        // Set the captured image to the ImageView
        if (capturedImage != null) {
            mBitmap = capturedImage;
            mImageView.setImageBitmap(mBitmap);
            mButtonDetect = findViewById(R.id.detectButton);
            mButtonDetect.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mButtonDetect.performClick(); // Programmatically trigger detection
                }
            }, 100); // Programmatically trigger detection
        } else {

        }

        // Hide resultView
        mResultView = findViewById(R.id.resultView);
        mResultView.setVisibility(View.INVISIBLE);
        mResultView.getLayoutParams().height = mImageView.getHeight();

        // Detect image loaded in the mImageView
        mButtonDetect = findViewById(R.id.detectButton);
        mProgressBar = findViewById(R.id.progressBar);
        mButtonDetect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mButtonDetect.setEnabled(false);
                mProgressBar.setVisibility(ProgressBar.VISIBLE);
                mButtonDetect.setText(getString(R.string.run_model));
                // Scale image to 320x320
                mImgScaleX = (float) mBitmap.getWidth() / PrePostProcessor.mInputWidth;
                mImgScaleY = (float) mBitmap.getHeight() / PrePostProcessor.mInputHeight;

                mIvScaleX = (mBitmap.getWidth() > mBitmap.getHeight() ? (float) mImageView.getWidth() / mBitmap.getWidth() : (float) mImageView.getHeight() / mBitmap.getHeight());
                mIvScaleY = (mBitmap.getHeight() > mBitmap.getWidth() ? (float) mImageView.getHeight() / mBitmap.getHeight() : (float) mImageView.getWidth() / mBitmap.getWidth());

                mStartX = (mImageView.getWidth() - mIvScaleX * mBitmap.getWidth()) / 2;
                mStartY = (mImageView.getHeight() - mIvScaleY * mBitmap.getHeight()) / 2;

                // Run on a new Thread
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Get scaled bitmap
                        Bitmap resizedBitmap = Bitmap.createScaledBitmap(mBitmap, PrePostProcessor.mInputWidth, PrePostProcessor.mInputHeight, true);
                        // Create tensor
                        final Tensor inputTensor = TensorImageUtils.bitmapToFloat32Tensor(resizedBitmap, PrePostProcessor.NO_MEAN_RGB, PrePostProcessor.NO_STD_RGB);
                        // Forward tensor for inference
                        IValue[] outputTuple = mModule.forward(IValue.from(inputTensor)).toTuple();
                        // Retrieve output tensor
                        final Tensor outputTensor = outputTuple[0].toTensor();
                        final float[] outputs = outputTensor.getDataAsFloatArray();
                        // Get class result
                        final ArrayList<Result> results = PrePostProcessor.outputsToNMSPredictions(outputs, mImgScaleX, mImgScaleY, mIvScaleX, mIvScaleY, mStartX, mStartY);
                        // Update UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mButtonDetect.setEnabled(true);
                                mButtonDetect.setText(getString(R.string.detect));
                                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                                mResultView.setResults(results);
                                mResultView.invalidate();
                                mResultView.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                });
                thread.start();
            }
        });

        // Try to load our trained YOLOv5 model
        try {
            mModule = LiteModuleLoader.load(MainDetectActivity.assetFilePath(getApplicationContext(), "best.torchscript.ptl"));
            BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("classes.txt")));
            String line;
            List<String> classes = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                classes.add(line);
            }
            PrePostProcessor.mClasses = new String[classes.size()];
            classes.toArray(PrePostProcessor.mClasses);
        } catch (IOException e) {
            Log.e("Object Detection", "Error reading assets", e);
            finish();
        }

        LifecycleOwner lifecycleOwner = this;
        cameraController = new CameraController(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                } else {
                    Toast.makeText(getApplicationContext(), "Permission to Camera Denied", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0: // photo taken
                    if (resultCode == RESULT_OK && data != null) {
                        mBitmap = (Bitmap) data.getExtras().get("data");
                        Matrix matrix = new Matrix();
                        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
                        mImageView.setImageBitmap(mBitmap);
                    }
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Register the BroadcastReceiver to listen for "trigger_detect" action
        IntentFilter intentFilter = new IntentFilter("trigger_detect");
        registerReceiver(detectButtonReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Unregister the BroadcastReceiver when the activity is paused
        unregisterReceiver(detectButtonReceiver);
    }
}
