package com.nitnelav.metricas.halstead;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToMetrics(View v){
        Intent metrics = new Intent(this, New_Metrics_Activity.class);
        startActivity(metrics);
    }
    
    public void goToDb(View v){
        Intent dbMetrics = new Intent(this, Metrics_Activity.class);
        startActivity(dbMetrics);
    }

}

