package com.example.gpc.domineering.beans;

import android.graphics.Rect;
import com.example.gpc.domineering.utils.Const;
import java.util.ArrayList;

public class Line {
    private Dot dot1;
    private Dot dot2;
    // the bounding rectangle for the line, this rectangle is greater than the actual line coordinates, to detect user click
    private Rect rect;
    // flag to know if the user touched this line or not
    private boolean active;

    public Line(Dot dot1, Dot dot2, int lineOrientation){
        active = false;
        this.dot1 = dot1;
        this.dot2 = dot2;
        int lineLength = dot2.diff(dot1);

        if (lineOrientation == Const.HOR_LINE_ID)
            rect = new Rect((int) dot1.getX(), Math.round(dot1.getY() - Const.LINE_STROKE_WIDTH), Math.round(dot1.getX() + lineLength), Math.round(dot1.getY() + Const.LINE_STROKE_WIDTH));
        else if(lineOrientation == Const.VER_LINE_ID)
            rect = new Rect(Math.round(dot1.getX() - Const.LINE_STROKE_WIDTH * 1.5f), (int) dot1.getY(), Math.round(dot1.getX() + Const.LINE_STROKE_WIDTH * 1.5f), Math.round(dot1.getY() + lineLength));
    }

    public Rect getRect() { return rect; }

    public ArrayList<Integer> getLineDrawingCoordinates(){
        ArrayList<Integer> coordinatesList = new ArrayList<>();
        coordinatesList.add((int) dot1.getX());
        coordinatesList.add((int) dot1.getY());
        coordinatesList.add((int) dot2.getX());
        coordinatesList.add((int) dot2.getY());
        return coordinatesList;
    }

    public void setActive() { active = true; }

    public void setDotsConnected(){
        dot1.setConnected(true);
        dot2.setConnected(true);
    }

    public boolean isActive() { return active; }

    public boolean isValidLine(){
        // check if this line is valid and can be selected
        return !(dot1.isConnected() || dot2.isConnected());
    }

}
