package com.missouristate.davis916.roamingball;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Laura Davis CIS 262-902
 * 28 May 2018
 *
 * This app uses sensor events and the device's accelerometer
 * to create a virtual rolling ball that moves on the x and y axis
 * when the phone is titled.
 */

public class MainActivity extends Activity implements SensorEventListener{

    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;

    private LayoutInflater layoutInflater;
    private ConstraintLayout mainLayout;
    private ImageView ballImage;
    private Ball mBall;

    private Thread movementThread;

    static int TOP;
    static int BOTTOM;
    static int LEFT;
    static int RIGHT;

    private TextView x_axis;
    private TextView y_axis;
    private TextView z_axis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        //Set the references to the layouts
        mainLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);
        x_axis = (TextView) findViewById(R.id.textView2);
        y_axis = (TextView) findViewById(R.id.textView4);
        z_axis = (TextView) findViewById(R.id.textView6);

        //Add the ball and initialize movement settings
        mBall = new Ball();
        initializeBall();
        layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ballImage = (ImageView) layoutInflater.inflate(R.layout.ball_item, null);
        ballImage.setX(50.0f);
        ballImage.setY(50.0f);
        mainLayout.addView(ballImage, 0);

        //Register the sensor manager
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //Implement the movement thread
        movementThread = new Thread(BallMovement);
    }//end onCreate()

    private void initializeBall() {
        //Compute the height and width of the device
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;

        //Configure the roaming ball
        mBall.setX(50.0f);
        mBall.setY(50.0f);
        mBall.setWidth(225);

        mBall.setVelocityX(0.0f);
        mBall.setVelocityY(0.0f);

        TOP = 0;
        BOTTOM = screenHeight - mBall.getWidth();
        LEFT = 0;
        RIGHT = screenWidth - mBall.getWidth();
    }//end initializeBall()

    //Register the sensor listener
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        //Start the thread
        movementThread.start();
    }//end onResume()

    //Unregister the listener
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this, sensorAccelerometer);
    }//end onPause()

    protected void onStop(){
        super.onStop();
        finish();
    }//end onStop()

    @Override
    public void onDestroy(){
        finish();
        super.onDestroy();
    }//end onDestroy()

    public void onSensorChanged(SensorEvent sensorEvent){

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            mBall.setVelocityX(sensorEvent.values[0]);
            mBall.setVelocityY(sensorEvent.values[1]);

            x_axis.setText(" " + sensorEvent.values[0]);
            y_axis.setText(" " + sensorEvent.values[1]);
            z_axis.setText(" " + sensorEvent.values[2]);
        }
    }//end onSensorChanged

    public void onAccuracyChanged(Sensor arg0, int arg1){}

    //Updates the ball position continuously
    private Runnable BallMovement = new Runnable() {
        private static final int DELAY = 20;
        @Override
        public void run() {
            try{
                while (true){
                    mBall.setX(mBall.getX() - mBall.getVelocityX());
                    mBall.setY(mBall.getY() + mBall.getVelocityY());

                    //Check for collisions
                    if (mBall.getY() < TOP)
                        mBall.setY(TOP);
                    else if (mBall.getY() > BOTTOM)
                        mBall.setY(BOTTOM);

                    if (mBall.getX() < LEFT)
                        mBall.setX(LEFT);
                    else if (mBall.getX() > RIGHT)
                        mBall.setX(RIGHT);

                    //Delay between animations
                    Thread.sleep(DELAY);

                    //Handle the relocation of the View (ImageView)
                    threadHandler.sendEmptyMessage(0);
                }
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };//end Runnable

    public Handler threadHandler = new Handler(){
        public void handleMessage(android.os.Message msg){
            //Handle the relocation of the ImageView
            ballImage.setX(mBall.getX());
            ballImage.setY(mBall.getY());
        }
    };//End threadHandler

    @Override
    //Stops the user from changing the orientation.
    //If the user rotates the device it will not
    //change to the landscape orientation.
    public void onConfigurationChanged(Configuration newConfig){
        //TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //Inflate the menu
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }//end createOptionsMenu()

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Handle action bar item clicks here. The action bar will
        //automatically handle clicks on the Home/Up button,
        //as long as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }//end onOptionsItemSelected()

}//end MainActivity class
