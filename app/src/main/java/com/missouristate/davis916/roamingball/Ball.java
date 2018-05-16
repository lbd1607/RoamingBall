package com.missouristate.davis916.roamingball;

/**
 * Laura Davis CIS 262-902
 * 28 May 2018
 *
 * The Ball class sets initial values for x and y position,
 * the width of the ball, and its current velocity on the x and y axis.
 */

public class Ball {
    private float mX;
    private float mY;
    private int mWidth;
    private float mVelocityX;
    private float mVelocityY;

    public void setX(float x){
        mX = x;
    }

    public float getX(){
        return mX;
    }

    public void setY(float y){
        mY = y;
    }

    public float getY(){
        return mY;
    }

    public void setWidth(int width){
        mWidth = width;
    }

    public int getWidth(){
        return mWidth;
    }

    public void setVelocityX(float velocityX){
        mVelocityX = velocityX;
    }

    public float getVelocityX(){
        return mVelocityX;
    }

    public void setVelocityY(float velocityY){
        mVelocityY = velocityY;
    }

    public float getVelocityY(){
        return mVelocityY;
    }
}
