package com.example.dharmaraj.scanningexternalstorage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by dharmaraj on 2/29/16.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView fileDetailsRecyclerView;
    private Button startBtn, stopBtn, shareBtn;
    private TextView fileAverageSizeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fileDetailsRecyclerView = (RecyclerView) findViewById(R.id.fileDetailsRecyclerView);
        startBtn = (Button) findViewById(R.id.startBtn);
        stopBtn = (Button) findViewById(R.id.stopBtn);
        shareBtn = (Button) findViewById(R.id.shareBtn);
        fileAverageSizeTV = (TextView) findViewById(R.id.fileAverageSizeTV);

        //Register click event listener
        startBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);
        shareBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startBtn:

                break;

            case R.id.stopBtn:

                break;

            case R.id.shareBtn:

                break;
        }

    }
}
