package com.example.gpc.domineering.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.gpc.domineering.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        if (buttonPlay != null) {
            // add play button click listener
            buttonPlay.setOnClickListener(this);
        }
    }

    private void init(){
        buttonPlay = findViewById(R.id.buttonPlay);
    }

    @Override
    public void onClick(View v){
        // start game activity
        startActivity(new Intent(this, GameActivity.class));
    }
}
