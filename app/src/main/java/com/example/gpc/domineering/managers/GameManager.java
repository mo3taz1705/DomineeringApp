package com.example.gpc.domineering.managers;

import android.content.Context;
import android.graphics.Color;
import com.example.gpc.domineering.beans.Dot;
import com.example.gpc.domineering.beans.Line;
import com.example.gpc.domineering.beans.Player;
import com.example.gpc.domineering.utils.Const;
import com.example.gpc.domineering.utils.Utils;
import java.util.ArrayList;

public class GameManager {

    // the game context
    private Context context;

    // game variables
    private int matrixSize = 5;
    private Dot[][] dotsMatrix;
    private boolean gameEnded = false;

    // the players variables
    private Player player1;
    private Player player2;
    private Player playerToPlay;
    private Player winnerPlayer;

    // drawing variables
    private float horizontalLength;
    private float horizontalMargin;
    private float verticalLength;
    private float verticalMargin;
    private int actualWidth;
    private int actualHeight;

    public GameManager(Context context){
        this.context = context;
        player1 = new Player("Player 1", Color.BLUE, Const.HOR_LINE_ID);
        player2 = new Player("Player 2", Color.RED, Const.VER_LINE_ID);
        playerToPlay = player1;
    }

    public int getActualWidth() { return actualWidth; }

    public int getActualHeight() { return actualHeight; }

    public float getHorizontalLength() { return horizontalLength; }

    public float getVerticalLength() { return verticalLength; }

    public float getVerticalMargin() { return verticalMargin; }

    public Dot[][] getDotsMatrix() { return dotsMatrix; }

    public Player getWinnerPlayer() { return winnerPlayer; }

    public Player getPlayerToPlay() { return playerToPlay; }

    public Player getPlayer1() { return player1; }

    public Player getPlayer2() { return player2; }

    private Player getOtherPlayer(){
        if(playerToPlay == player1)
            return player2;
        return player1;
    }

    private void changePlayer(){
        if(playerToPlay == player1) {
            playerToPlay = player2;
        } else {
            playerToPlay = player1;
        }
    }

    public void onSizeChanged(int w, int h, int paddingTop, int paddingLeft, int paddingBottom, int paddingRight){
        // get the view coordinates without padding
        int mViewBottom = h - paddingBottom;
        int mViewRight = w - paddingRight;

        // calculate new actual width and height
        actualWidth = mViewRight - paddingLeft;
        actualHeight = mViewBottom - paddingTop;

        // calculate the drawing variables
        horizontalLength = (float) actualWidth / this.matrixSize;
        horizontalMargin = horizontalLength / 2;
        verticalLength = horizontalLength;
        verticalMargin = (actualHeight - (verticalLength * (this.matrixSize - 1))) / 2;

        // create the dots matrix and lines array
        createDots();
        createLines();
    }

    private void createDots(){
        // create the dots matrix
        Dot[][] dots = new Dot[this.matrixSize][this.matrixSize];
        for(int i = 0; i < this.matrixSize; i++){
            for(int j = 0; j < this.matrixSize; j++){
                float cx = horizontalMargin + i * horizontalLength;
                float cy = verticalMargin + j * verticalLength;
                dots[i][j] = new Dot(cx, cy);
            }
        }
        this.dotsMatrix = dots;
    }

    private void createLines(){
        // create the horizontal and vertical lines array
        ArrayList<Line> horizontalLines = new ArrayList<>();
        ArrayList<Line> verticalLines = new ArrayList<>();
        Dot[][] dots = getDotsMatrix();

        Line horLine;
        Line verLine;
        for (int i = 0; i < this.matrixSize; i++){
            for (int j = 0; j < this.matrixSize; j++){
                if (i < this.matrixSize - 1) {
                    horLine = new Line(dots[i][j], dots[i + 1][j], Const.HOR_LINE_ID);
                    horizontalLines.add(horLine);
                }

                if (j < this.matrixSize - 1) {
                    verLine = new Line(dots[i][j], dots[i][j + 1], Const.VER_LINE_ID);
                    verticalLines.add(verLine);
                }
            }
        }

        // set the lines array for each player
        player1.setLinesArray(horizontalLines);
        player2.setLinesArray(verticalLines);
    }

    private boolean checkGameEnded(){
        // check if the current player have any valid line, so the game has not ended
        ArrayList<Line> lines = playerToPlay.getLinesArray();
        for(Line line : lines){
            if (line.isValidLine())
                return false;
        }
        // the current player has no valid lines, so set the other player as the winner
        winnerPlayer = getOtherPlayer();
        // set the gameEnded flag to true
        gameEnded = true;
        return gameEnded;
    }

    public Line onPressAction(Dot dot){
        // check on the lines of the current player
        Line pressedLine = playerToPlay.getEnclosedLine(dot);
        // check if the game still has not ended and there is a an actual pressed line
        if (!gameEnded && pressedLine != null) {
            // check if the pressed line is valid, if not so the line is already connected before, so show message
            if (pressedLine.isValidLine()) {
                // set the line as active and the dots as connected
                pressedLine.setActive();
                pressedLine.setDotsConnected();
                // change the player to play
                changePlayer();
                // check whether the game has ended
                checkGameEnded();
                return pressedLine;
            }else{
                // show toast message to the user
                Utils.showToast(context, "Invalid move! The dots are already connected.");
                return pressedLine;
            }
        }

        // check on the lines of the other player
        Line tempLine = getOtherPlayer().getEnclosedLine(dot);
        // check if the game still has not ended and there is a an actual pressed line
        if (!gameEnded && tempLine != null){
            // so the user has pressed on a line of the other player, so show toast message to the user
            StringBuilder stringBuilder = new StringBuilder("Invalid move! You are playing ");
            stringBuilder.append(playerToPlay.getLinesOrientation() == Const.HOR_LINE_ID ? "horizontally" : "vertically");
            stringBuilder.append('.');
            Utils.showToast(context, stringBuilder.toString());
        }
        return null;
    }
}
