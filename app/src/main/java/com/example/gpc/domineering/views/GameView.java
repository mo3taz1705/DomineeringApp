package com.example.gpc.domineering.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.example.gpc.domineering.managers.GameManager;
import com.example.gpc.domineering.beans.Dot;
import com.example.gpc.domineering.beans.Line;
import com.example.gpc.domineering.beans.Player;
import com.example.gpc.domineering.utils.Const;
import java.util.ArrayList;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class GameView extends SurfaceView implements Runnable {

    // boolean variable to track if the game is playing or not
    volatile boolean playing;

    // the game thread
    private Thread gameThread = null;

    // game variables
    private GameManager mGameManager;

    // drawing objects
    private Paint mDotPaint;
    private Paint mHeaderPaint;
    private Paint mWinnerPaint;
    private Canvas mCanvas;
    private SurfaceHolder mSurfaceHolder;
    private KonfettiView konfettiView;

    // touch variables
    private float downX = 0f;
    private float downY = 0f;
    private boolean isRealClick = false;

    public GameView(Context context){
        super(context);
        init();
    }

    private void init(){
        mDotPaint = new Paint();
        mDotPaint.setStyle(Paint.Style.FILL);
        mDotPaint.setColor(Color.BLACK);
        mDotPaint.setAntiAlias(true);

        mHeaderPaint = new Paint();
        mHeaderPaint.setStyle(Paint.Style.FILL);
        mHeaderPaint.setColor(Color.BLACK);
        mHeaderPaint.setAntiAlias(true);
        mHeaderPaint.setTextSize(80);

        mWinnerPaint = new Paint();
        mWinnerPaint.setStyle(Paint.Style.FILL);
        mWinnerPaint.setColor(Color.rgb(139,137,112));    // (205,190,112) lighter color
        mWinnerPaint.setAntiAlias(true);
        mWinnerPaint.setTextSize(80);

        mSurfaceHolder = getHolder();
        mGameManager = new GameManager(getContext());
    }

    public void setKonfettiView(KonfettiView konfettiView) { this.konfettiView = konfettiView; }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        // inform the game manager with the available coordinates
        mGameManager.onSizeChanged(w, h, getPaddingTop(), getPaddingLeft(), getPaddingBottom(), getPaddingRight());
    }

    @Override
    public void run() {
        while (playing){
            // to update the frame, move objects if desired
            // update();

            // to draw the frame
            draw();

            // to control the frame and it's used to make our frame rate to be around 60fps.
            control();
        }
    }

    public void pause(){
        // this will cause the game thread to pause
        playing = false;
        try{
            // wait for the game thread to join the main thread
            if (gameThread != null) {
                gameThread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume(){
        playing = true;
        // start new game thread
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void draw(){
        // check if the surface is valid
        if (mSurfaceHolder.getSurface().isValid()){
            // lock the canvas
            mCanvas = mSurfaceHolder.lockCanvas();

            // draw background color
            mCanvas.drawColor(Color.WHITE);

            // draw the main board
            drawBoard();

            // draw the lines
            drawLines();

            // draw the header to say which player is going to play
            drawPlayerGoingToPlayHeader();

            // draw the winner player
            Player winnerPlayer = mGameManager.getWinnerPlayer();
            if(winnerPlayer != null){
                drawWinner(winnerPlayer);
            }

            // unlock the canvas
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    private void control(){
        try{
            // sleep in order to make our frame rate to be around 60fps.
            Thread.sleep(Const.SLEEP_DURATION);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void drawBoard(){
        if (mCanvas != null) {
            // draw dots on canvas
            Dot[][] dots = mGameManager.getDotsMatrix();
            for (Dot[] dotArr : dots){
                for (Dot dot : dotArr){
                    mCanvas.drawCircle(dot.getX(), dot.getY(), Const.DOT_RADIUS, mDotPaint);
                }
            }
        }
    }

    private void drawLines(){
        // for each player, draw his active lines on the canvas
        Player[] players = {mGameManager.getPlayer1(), mGameManager.getPlayer2()};
        ArrayList<Line> lines;
        ArrayList<Integer> lineCoordinates;
        for (Player player : players){
            lines = player.getLinesArray();
            for (Line line : lines){
                if (line.isActive() && mCanvas != null){
                    lineCoordinates = line.getLineDrawingCoordinates();
                    mCanvas.drawLine(lineCoordinates.get(0), lineCoordinates.get(1), lineCoordinates.get(2), lineCoordinates.get(3), player.getLinePaint());
                }
            }
        }
    }

    private void drawPlayerGoingToPlayHeader(){
        // draw the player going to play header
        // the text would be drawn in half of the vertical margin, and quarter of the actual width
        float x = (float) mGameManager.getActualWidth() / 4;
        float y = mGameManager.getVerticalMargin() / 2;
        Player player = mGameManager.getPlayerToPlay();
        if (mCanvas != null &&  player != null){
            mCanvas.drawText(player.getName(), x, y, mHeaderPaint);
            if(player.getLinesOrientation() == Const.HOR_LINE_ID){
                mCanvas.drawLine(x * 2.75f, y - 20f, x * 2.75f + mGameManager.getHorizontalLength(), y - 20f, player.getLinePaint());
            }else if (player.getLinesOrientation() == Const.VER_LINE_ID){
                mCanvas.drawLine(x * 3, y - mGameManager.getVerticalLength() / 2 - 20f, x * 3, y + mGameManager.getVerticalLength() / 2 - 20f, player.getLinePaint());
            }
        }
    }

    private void drawWinner(Player winnerPlayer){
        // draw the winner player message and the konfettiview for celebration
        float x = (float) mGameManager.getActualWidth() / 20;
        float y = (float) mGameManager.getActualHeight() / 2;
        if (mCanvas != null &&  winnerPlayer != null){
            mCanvas.drawText("Congratulations :)", x, y, mWinnerPaint);
            mCanvas.drawText(winnerPlayer.getName() + " has won!!", x, y + 80, mWinnerPaint);


            konfettiView.build()
                    .addColors(Color.rgb(180, 138, 227), Color.rgb(243, 46, 114), Color.rgb(245, 226, 149), Color.rgb(56, 183, 153))
                    .setDirection(0.0, 359.0)
                    .setSpeed(1f, 7f)
                    .setFadeOutEnabled(true)
                    .setTimeToLive(2000L)
                    .addShapes(Shape.RECT, Shape.CIRCLE)
                    .addSizes(new Size(12, 6f))
                    .setPosition(-500f, konfettiView.getWidth() + 250f, -250f, -250f)
                    .burst(100);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                // save the down touch coordinates, and set the isRealClick flag to true
                downX = event.getX();
                downY = event.getY();
                isRealClick = true;
                break;
            case MotionEvent.ACTION_UP:
                // if the isRealClick flag is true, so accept the touch and send the down coordinates to the game manager
                if (isRealClick) {
                    mGameManager.onPressAction(new Dot(downX, downY));
                    isRealClick = false;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                // if the touch canceled, make the isRealClick flag to false to cancel the touch
                isRealClick = false;
                break;
            case MotionEvent.ACTION_MOVE:
                // if the user moved the touch greater than the allowed threshold, make the isRealClick flag to false to cancel the touch
                if (isRealClick && ((Math.abs(event.getX() - downX) > Const.TOUCH_MOVEMENT_THRESHOLD) || (Math.abs(event.getY() - downY) > Const.TOUCH_MOVEMENT_THRESHOLD)))
                    isRealClick = false;
                break;
        }
        return true;
    }

}
