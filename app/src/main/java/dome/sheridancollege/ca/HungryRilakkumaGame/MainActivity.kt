package dome.sheridancollege.ca.HungryRilakkumaGame

import android.content.Context
import android.graphics.Rect
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    //instantiating a new donut object
    internal var donut = Donut(findViewById<View>(R.id.donut) as ImageView, false)

    internal var MIN_X = 0
    internal var MAX_X: Int = 0
    internal var MIN_Y = 0
    internal var MAX_Y: Int = 0
    internal var speed = 7

    // System sensor manager instance.
    private var sensorService: SensorManager? = null

    internal var rl: RelativeLayout

    //creating a sensor for compass
    private var sensor: Sensor? = null

    //imgview for compass
    private var compass: ImageView? = null

    //donut
    //    private ImageView donut;
    //    private boolean isDonutAlive;

    //degree for rotation
    private var currentDegree = 0f


    //imgview for rilakkuma
    private var rilakkuma: ImageView? = null
    private var translationX: Float = 0.toFloat()
    private var translationY: Float = 0.toFloat()
    private var rilakkumaWidth: Int = 0
    private var rilakkumaHeight: Int = 0

    //text views
    private var tvTimer: TextView? = null
    private var tvScore: TextView? = null

    //timer and score
    private var score: Int = 0
    private val timer: Int = 0
    private var isRunning: Boolean = false

    //button view
    private val btnStart: Button? = null


    //countdown timer for 60 seconds
    internal var cdt: CountDownTimer = object : CountDownTimer(60000, 1000) {

        override fun onTick(millisUntilFinished: Long) {
            isRunning = true
            //countdown timer
            tvTimer!!.text = "Timer: " + millisUntilFinished / 1000

        }

        override fun onFinish() {
            isRunning = false
            val toast = Toast.makeText(applicationContext, "Final Score: $score", Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    private val mySensorEventListener = object : SensorEventListener {
        //rect for collision
        internal var rilakkumaRect = Rect()
        internal var donutRect = Rect()

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

        override fun onSensorChanged(event: SensorEvent) {

            tvScore = findViewById(R.id.tvScore)

            val degree = Math.round(event.values[0]).toFloat()
            //rotate animation
            // create a rotation animation (reverse turn degree degrees)
            val ra = RotateAnimation(
                    currentDegree,
                    -degree, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f)
            // how long the animation will take place
            ra.duration = 210
            // set the animation after the end of the reservation status
            ra.fillAfter = true

            // Start the animation
            compass!!.startAnimation(ra)
            currentDegree = -degree

            //rilakkuma does not move if game isnt started yet. compass is fine though
            if (!isRunning) {
                //handles the start collision..
                if (score == 1) {
                    tvScore!!.text = "Score : 0"
                }
                return
            }

            // angle between the magnetic north direction
            // 0=North, 90=East, 180=South, 270=West
            val azimuth = event.values[0]
            moveDirection(Math.floor(azimuth.toDouble()))


            rilakkuma!!.getHitRect(rilakkumaRect)
            donut.donutImage.getHitRect(donutRect)

            //check for collision using rect of both donut and rilakkuma
            if (Rect.intersects(rilakkumaRect, donutRect)) {

                score++
                donut.isDonutAlive = false
                createDonut(donut, MAX_X, MAX_Y)
                tvScore!!.text = "Score : $score"
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorService = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorService!!.getDefaultSensor(Sensor.TYPE_ORIENTATION)

        //compass and donut
        compass = findViewById<View>(R.id.imageViewCompass) as ImageView

        //        donut = (ImageView) findViewById(R.id.donut);


        //rilakkuma
        rilakkuma = findViewById(R.id.imageViewRilakkuma)
        rilakkumaWidth = rilakkuma!!.layoutParams.width
        rilakkumaHeight = rilakkuma!!.layoutParams.height
        println(rilakkumaWidth)
        println(rilakkumaHeight)

        //button
        val btnStart = findViewById<Button>(R.id.btnStart)
        btnStart.setOnClickListener {
            //textViews
            tvScore = findViewById(R.id.tvScore)
            tvTimer = findViewById(R.id.tvTimer)

            //set button to restart when pressed
            val btnStart = findViewById<Button>(R.id.btnStart)
            if (btnStart.text == "Start") {
                btnStart.text = "Restart"
            }

            //countdown timer if cdt is already running, cancel current one and reset score
            cdt.cancel()
            score = 0
            timerCountdown(timer)
            tvScore!!.text = "Score : $score"
            tvTimer!!.text = "Timer : $timer"
            do {
                createDonut(donut, MAX_X, MAX_Y)
                //                    donut.createDonut(donut);
            } while (isRunning && !donut.isDonutAlive)
        }

        //relative layout
        rl = findViewById(R.id.relativeLayout)
        MAX_Y = rl.layoutParams.height
        MAX_X = rl.layoutParams.width
        println("Max X: " + MAX_X + "| Donut width: " + donut.donutImage.layoutParams.width)
        println("Max Y: " + MAX_Y + "| Donut height: " + donut.donutImage.layoutParams.height)


        if (sensor != null) {
            sensorService!!.registerListener(mySensorEventListener, sensor,
                    SensorManager.SENSOR_DELAY_GAME)
            Log.i("Compass MainActivity", "Registerered for ORIENTATION Sensor")
        } else {
            Log.e("Compass MainActivity", "Registerered for ORIENTATION Sensor")
            Toast.makeText(this, "ORIENTATION Sensor not found",
                    Toast.LENGTH_LONG).show()
            finish()
        }
    }

    //check collision to increase score
    private fun checkCollision(rX: Float, rY: Float, dX: Float, dY: Float) {
        tvScore = findViewById(R.id.tvScore)
        //if collision increase score
        if (rX == dX) {
            score++
            donut.isDonutAlive = false
        } else if (rY == dY) {
            score++
            donut.isDonutAlive = false
        }

        tvScore!!.text = "Score : $score"
    }

    //move direction based on compass
    fun moveDirection(azimuth: Double) {
        //move north (up) 0-44
        if (azimuth < 45 && translationY >= MIN_Y + speed && translationY <= MAX_Y) {
            moveNorth()
        } //move northeast 45-89
        else if (azimuth >= 45 && azimuth < 90 && translationY >= MIN_Y + speed && translationY <= MAX_Y && translationX >= MIN_X && translationX <= MAX_X) {
            moveNorth()
            moveEast()
        }//move east (right) 90-179
        else if (azimuth >= 90 && azimuth < 135 && translationX >= MIN_X && translationX <= MAX_X) {
            moveEast()
        }//move southeast 135-179
        else if (azimuth >= 135 && azimuth < 180 && translationY >= MIN_Y - speed && translationY <= MAX_Y - rilakkumaHeight && translationX >= MIN_X && translationX <= MAX_X) {
            moveSouth()
            moveEast()
        }//move south (down) 180-224
        else if (azimuth >= 180 && azimuth < 225 && translationY >= MIN_Y - speed && translationY <= MAX_Y - rilakkumaHeight) {
            moveSouth()
        } //move southwest 225-269
        else if (azimuth >= 225 && azimuth < 280 && translationY >= MIN_Y - speed && translationY <= MAX_Y - rilakkumaHeight && translationX >= MIN_X - speed && translationX <= MAX_X - rilakkumaWidth) {
            moveSouth()
            moveWest()
        }// move left (west) 270-314
        else if (azimuth >= 270 && azimuth < 315 && translationX >= MIN_X - speed && translationX <= MAX_X - rilakkumaWidth) {
            moveWest()
        } //move northwest 315-360
        else if (azimuth >= 315 && translationY >= MIN_Y + speed && translationY <= MAX_Y && translationX >= MIN_X - speed && translationX <= MAX_X - rilakkumaWidth) {
            moveNorth()
            moveWest()
        }

    }

    fun moveWest() {
        translationX = translationX + speed
        rilakkuma!!.translationX = translationX
    }

    fun moveEast() {
        translationX = translationX - speed
        rilakkuma!!.translationX = translationX
    }

    fun moveNorth() {
        translationY = translationY - speed
        rilakkuma!!.translationY = translationY
    }

    fun moveSouth() {
        translationY = translationY + speed
        rilakkuma!!.translationY = translationY
    }


    fun timerCountdown(timer: Int) {
        tvTimer = findViewById(R.id.tvTimer)
        cdt.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (sensor != null) {
            sensorService!!.unregisterListener(mySensorEventListener)
        }
    }

}
