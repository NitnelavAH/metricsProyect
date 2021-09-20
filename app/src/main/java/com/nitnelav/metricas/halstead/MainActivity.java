package com.nitnelav.metricas.halstead;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private final int CHOOSE_FILE_FROM_DEVICE = 1001;
    private static final String TAG = "MainActivity";

    private Button chooseFile_btn;
    private TextView path_file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chooseFile_btn = findViewById(R.id.choose_file_btn);
        path_file = findViewById(R.id.path_file);

        chooseFile_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                callChooseFileFromDevice();
            }
        });
    }

    private void callChooseFileFromDevice() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        startActivityForResult(intent, CHOOSE_FILE_FROM_DEVICE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == CHOOSE_FILE_FROM_DEVICE && resultCode == Activity.RESULT_OK) {
            if(resultData != null) {
                Log.d(TAG, "onActivityResult: "+ resultData.getData().getPath());
                Log.d(TAG, "onActivityResult: "+ resultData.getDataString());
                //path_file.setText("File Path:" + resultData.getData() );
                try {
                    String code = readTextFromUri(resultData.getData());
                    Log.d(TAG, "onActivityResult: "+ code);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private String readTextFromUri(Uri uri) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream =
                     getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        return stringBuilder.toString();
    }

}