package com.example.gpc.domineering.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import com.example.gpc.domineering.views.GameView;
import nl.dionsegijn.konfetti.KonfettiView;

public class GameActivity extends AppCompatActivity {

    // the parent layout, this holds 2 views (GameView, KonfettiView)
    private FrameLayout parentFrameLayout;
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        if (parentFrameLayout != null) {
            // attach the parent layout to this activity
            setContentView(parentFrameLayout);
        }
    }

    private void init(){
        parentFrameLayout = new FrameLayout(this);

        KonfettiView konfettiView = new KonfettiView(this);
        gameView = new GameView(this);
        gameView.setKonfettiView(konfettiView);

        // add the views to the parent layout
        parentFrameLayout.addView(gameView);
        parentFrameLayout.addView(konfettiView);
    }

    @Override
    protected void onPause(){
        super.onPause();
        if (gameView != null) {
            // pause the game
            gameView.pause();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (gameView != null) {
            // resume the game
            gameView.resume();
        }
    }
}