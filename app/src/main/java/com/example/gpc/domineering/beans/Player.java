package com.example.gpc.domineering.beans;

import android.graphics.Paint;
import com.example.gpc.domineering.utils.Const;
import java.util.ArrayList;

public class Player {
    private String name;
    private Paint linePaint;
    private ArrayList<Line> linesArray;
    private int linesOrientation;

    public Player(String name, int color, int linesOrientation){
        this.name = name;
        this.linesOrientation = linesOrientation;

        linePaint = new Paint();
        linePaint.setColor(color);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(Const.LINE_STROKE_WIDTH);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeJoin(Paint.Join.ROUND);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setAlpha(255);
    }

    public Paint getLinePaint() { return linePaint; }

    public String getName() { return name; }

    public int getLinesOrientation() { return linesOrientation; }

    public ArrayList<Line> getLinesArray() { return linesArray; }

    public Line getEnclosedLine(Dot dot){
        Line detectedLine = null;
        for (Line line : this.linesArray){
            if (line.getRect().contains((int) dot.getX(), (int) dot.getY())) {
                detectedLine = line;
                break;
            }
        }
        return detectedLine;
    }

    public void setLinesArray(ArrayList<Line> linesArray) { this.linesArray = linesArray; }

}
