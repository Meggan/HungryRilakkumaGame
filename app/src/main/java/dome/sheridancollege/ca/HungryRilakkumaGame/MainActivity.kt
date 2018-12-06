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
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    //instantiating a new objects using lateinit as it is fully instantiated later on the onCreate etc..
    lateinit var donut: Donut
    lateinit var rilakkuma: Rilakkuma
    lateinit var rl: RelativeLayout
    lateinit var compass: Compass


    // System sensor manager and sensors
    private var sensorService: SensorManager? = null
    private var sensor: Sensor? = null


    // numbers for calculations
    private val speed: Int = 7
    private var score: Int = 0
    private val timer: Int = 0
    internal var MAX_X: Int = 0
    internal var MAX_Y: Int = 0
    private var currentDegree = 0f //degree for rotation of the compass

    //imgviews and textviews and button
    private var tvTimer: TextView? = null
    private var tvScore: TextView? = null
    private val btnStart: Button? = null

    //countdown timer for 60 seconds
    internal var cdt: CountDownTimer = object : CountDownTimer(60000, 1000) {

        override fun onTick(millisUntilFinished: Long) {
            rilakkuma.isRunning=true //set to true while timer is still on
            tvTimer!!.text = "Timer: " + millisUntilFinished / 1000   //countdown timer

        }

        override fun onFinish() {
            rilakkuma.isRunning=false; //set to false when timer is done
            val toast = Toast.makeText(applicationContext, "Final Score: $score", Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    private val mySensorEventListener = object : SensorEventListener {
        //rects for collision
        var rilakkumaRect = Rect()
        var donutRect = Rect()

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

        override fun onSensorChanged(event: SensorEvent) {

            tvScore = findViewById(R.id.tvScore)
            var degree = Math.round(event.values[0]).toFloat()
            moveCompassDirection(compass, currentDegree,degree)
            currentDegree = -degree

            //rilakkuma does not move if game isnt started yet. compass is fine though
            if (!rilakkuma.isRunning) {
                //handles the start collision..
                if (score == 1) {
                    tvScore!!.text = "Score : 0"
                }
                return
            }

            // angle between the magnetic north direction
            // 0=North, 90=East, 180=South, 270=West
            val azimuth = event.values[0]
            moveDirection(rilakkuma, Math.floor(azimuth.toDouble()),MAX_X,MAX_Y,speed)

            rilakkuma.rilakkumaImage.getHitRect(rilakkumaRect)
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

        //create all objects
        donut = Donut(findViewById(R.id.donut), false)
        rilakkuma = Rilakkuma(findViewById(R.id.imageViewRilakkuma),false)
        compass = Compass(findViewById(R.id.imageViewCompass))
        rl = findViewById(R.id.relativeLayout)

        //values based on device
        MAX_Y = rl.layoutParams.height
        MAX_X = rl.layoutParams.width


        //button instantiated and listener created
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
            cdt.start()
            tvScore!!.text = "Score : $score"
            tvTimer!!.text = "Timer : $timer"
            do {
                createDonut(donut, MAX_X, MAX_Y)
            } while (rilakkuma.isRunning && !donut.isDonutAlive)
        }

        if (sensor != null) {
            sensorService!!.registerListener(mySensorEventListener, sensor,
                    SensorManager.SENSOR_DELAY_GAME)
            Log.i("Compass MainActivity", "ORIENTATION Sensor Ready")
        } else {
            Log.e("Compass MainActivity", "ORIENTATION Sensor Ready")
            Toast.makeText(this, "ERROR: ORIENTATION Sensor not found",
                    Toast.LENGTH_LONG).show()
            finish()
        }
    }
        override fun onDestroy() {
        super.onDestroy()
        if (sensor != null) {
            sensorService!!.unregisterListener(mySensorEventListener)
        }
    }

}
