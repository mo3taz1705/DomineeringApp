package com.example.gpc.domineering.beans;

public class Dot {
    private float x;
    private float y;
    private boolean connected;

    public Dot(float x, float y){
        this.x = x;
        this.y = y;
        connected = false;
    }

    public float getX() { return x; }

    public float getY() { return y; }

    public void setConnected(boolean connected) { this.connected = connected; }

    public boolean isConnected() { return connected; }

    public int diff(Dot dot1){
        // get the difference between 2 dots, to know the line length
        if (Math.abs(this.x - dot1.getX()) != 0)
            return (int) Math.abs(this.x - dot1.getX());
        return (int) Math.abs(this.y - dot1.getY());
    }
}
