package dome.sheridancollege.ca.HungryRilakkumaGame;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

     int MIN_X =0;
     int MAX_X ;
     int MIN_Y =0;
     int MAX_Y;
     int speed = 7;

    // System sensor manager instance.
    private SensorManager sensorService;

    RelativeLayout rl;

    //creating a sensor for compass
    private Sensor sensor;

    //imgview for compass
    private ImageView compass;

    //donut
    private ImageView donut;
    private boolean isDonutAlive;
//    final int MAX_DX = 370;
//    final int MAX_DY = 515;

    //degree for rotation
    private float currentDegree = 0f;


    //imgview for rilakkuma
    private ImageView rilakkuma;
    private float translationX;
    private float translationY;
    private int rilakkumaWidth;
    private int rilakkumaHeight;

    //text views
    private TextView tvTimer;
    private TextView tvScore;

    //timer and score
    private int score;
    private int timer;
    private boolean isRunning;

    //button view
    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorService = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorService.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        //compass and donut
        compass = (ImageView) findViewById(R.id.imageViewCompass);
//        Donut donut = new Donut((ImageView)findViewById(R.id.donut),false);
        donut = (ImageView) findViewById(R.id.donut);


        //rilakkuma
        rilakkuma = findViewById(R.id.imageViewRilakkuma);
        rilakkumaWidth = rilakkuma.getLayoutParams().width;
        rilakkumaHeight = rilakkuma.getLayoutParams().height;
        System.out.println(rilakkumaWidth);
        System.out.println(rilakkumaHeight);

        //button
        Button btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //textViews
                tvScore = findViewById(R.id.tvScore);
                tvTimer = findViewById(R.id.tvTimer);

                //set button to restart when pressed
                Button btnStart = findViewById(R.id.btnStart);
                if (btnStart.getText().equals("Start")) {
                    btnStart.setText("Restart");
                }

                //countdown timer if cdt is already running, cancel current one and reset score
                cdt.cancel();
                score = 0;
                timerCountdown(timer);
                tvScore.setText("Score : " + score);
                tvTimer.setText("Timer : " + timer);
                do{
                    createDonut();
                }while(isRunning && !isDonutAlive);

            }
        });

        //relative layout
        rl = findViewById(R.id.relativeLayout);
        MAX_Y = rl.getLayoutParams().height;
        MAX_X = rl.getLayoutParams().width;
        System.out.println("Max X: " + MAX_X + "| Donut width: " + donut.getLayoutParams().width);
        System.out.println("Max Y: " + MAX_Y + "| Donut height: " + donut.getLayoutParams().height);


        if (sensor != null) {
            sensorService.registerListener(mySensorEventListener, sensor,
                    SensorManager.SENSOR_DELAY_GAME);
            Log.i("Compass MainActivity", "Registerered for ORIENTATION Sensor");
        } else {
            Log.e("Compass MainActivity", "Registerered for ORIENTATION Sensor");
            Toast.makeText(this, "ORIENTATION Sensor not found",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void createDonut(){
        //if there is no donut out, create new one
        double dx;
        double dy;

        do{
            dx = (Math.random()*(((MAX_X) - MIN_X) + 1)) + donut.getLayoutParams().width;
        }while(dx>MAX_X-donut.getLayoutParams().width || dx<(donut.getLayoutParams().width));

        do{
            dy = (Math.random()*((MAX_Y - MIN_Y) + 1)) + donut.getLayoutParams().height;
        }while(dy>MAX_Y-donut.getLayoutParams().height || dy<(donut.getLayoutParams().height));

        System.out.println("X: " + dx);
        System.out.println("Y: " + dy);
        donut.setTranslationX((float)dx);
        donut.setTranslationY((float)dy);
        isDonutAlive=true;
    }

    //countdown timer for 60 seconds
    CountDownTimer cdt = new CountDownTimer(60000, 1000) {

        public void onTick(long millisUntilFinished) {
            isRunning=true;
            //countdown timer
            tvTimer.setText("Timer: " + millisUntilFinished / 1000);

        }

        public void onFinish() {
            isRunning = false;
            Toast toast = Toast.makeText(getApplicationContext(), "Final Score: " + score, Toast.LENGTH_SHORT);
            toast.show();
        }
    };

    private SensorEventListener mySensorEventListener = new SensorEventListener() {
        //rect for collision
        Rect rilakkumaRect = new Rect();
        Rect donutRect = new Rect();

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {

            tvScore = findViewById(R.id.tvScore);

            float degree = Math.round(event.values[0]);
            //rotate animation
            // create a rotation animation (reverse turn degree degrees)
            RotateAnimation ra = new RotateAnimation(
                    currentDegree,
                    -degree,Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,0.5f);
            // how long the animation will take place
            ra.setDuration(210);
            // set the animation after the end of the reservation status
            ra.setFillAfter(true);

            // Start the animation
            compass.startAnimation(ra);
            currentDegree = -degree;

            //rilakkuma does not move if game isnt started yet. compass is fine though
            if (!isRunning){
                //handles the start collision..
                if (score == 1) {
                    tvScore.setText("Score : 0");
                }
                return;
            }

            // angle between the magnetic north direction
            // 0=North, 90=East, 180=South, 270=West
            float azimuth = event.values[0];
            moveDirection(Math.floor(azimuth));


            rilakkuma.getHitRect(rilakkumaRect);
            donut.getHitRect(donutRect);

            //check for collision using rect of both donut and rilakkuma
            if (Rect.intersects(rilakkumaRect,donutRect)) {

                score++;
                isDonutAlive = false;
                createDonut();
                tvScore.setText("Score : " + score);
            }
        }
    };

    //check collision to increase score
    private void checkCollision(float rX, float rY, float dX, float dY){
        tvScore = findViewById(R.id.tvScore);
        //if collision increase score
        if (rX == dX) {
            score++;
            isDonutAlive = false;
        }
        else if (rY == dY) {
            score++;
            isDonutAlive = false;
        }

        tvScore.setText("Score : " + score);
    }
    //move direction based on compass
    public void moveDirection(double azimuth){
        //move north (up) 0-44
        if (azimuth < 45 && translationY >=MIN_Y+speed && translationY <= MAX_Y){
            moveNorth();
        } //move northeast 45-89
        else if (azimuth >= 45 && azimuth< 90 && translationY >=MIN_Y+speed && translationY <= MAX_Y  && translationX >=MIN_X && translationX <= MAX_X) {
            moveNorth();
            moveEast();
        }//move east (right) 90-179
        else if (azimuth >= 90 && azimuth< 135 && translationX >=MIN_X && translationX <= MAX_X){
            moveEast();
        }//move southeast 135-179
        else if (azimuth >= 135 && azimuth < 180 && translationY >=MIN_Y-speed && translationY <= MAX_Y-rilakkumaHeight && translationX >=MIN_X && translationX <= MAX_X){
            moveSouth();
            moveEast();
        }//move south (down) 180-224
        else if (azimuth >= 180 && azimuth < 225 && translationY >=MIN_Y-speed && translationY <= MAX_Y-rilakkumaHeight){
            moveSouth();
        } //move southwest 225-269
        else if (azimuth >= 225 && azimuth < 280 && translationY >=MIN_Y-speed && translationY <= MAX_Y-rilakkumaHeight &&  translationX >=MIN_X-speed && translationX <= MAX_X-rilakkumaWidth){
            moveSouth();
            moveWest();
        }// move left (west) 270-314
        else if (azimuth >= 270 && azimuth < 315 &&  translationX >=MIN_X-speed && translationX <= MAX_X-rilakkumaWidth){
            moveWest();
        } //move northwest 315-360
        else if (azimuth >=315 && translationY >=MIN_Y+speed && translationY <= MAX_Y &&  translationX >=MIN_X-speed && translationX <= MAX_X-rilakkumaWidth){
            moveNorth();
            moveWest();
        }

    }

    public void moveWest(){
        translationX = translationX +speed;
        rilakkuma.setTranslationX(translationX);
    }

    public void moveEast(){
        translationX = translationX -speed;
        rilakkuma.setTranslationX(translationX);
    }

    public void moveNorth(){
        translationY = translationY -speed;
        rilakkuma.setTranslationY(translationY);
    }

    public void moveSouth(){
        translationY = translationY +speed;
        rilakkuma.setTranslationY(translationY);
    }


    public void timerCountdown(int timer) {
        tvTimer = findViewById(R.id.tvTimer);
        cdt.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sensor != null) {
            sensorService.unregisterListener(mySensorEventListener);
        }
    }

}
