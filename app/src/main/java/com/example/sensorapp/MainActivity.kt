package com.example.sensorapp

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import android.view.View
import android.widget.TextView
import com.example.sensorapp.databinding.ActivityMainBinding

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var text_sensorName : TextView
    private lateinit var text_sensorValue : TextView
    private lateinit var graphView: GraphView
    private lateinit var graphViewY: GraphView
    private lateinit var graphViewZ: GraphView
    private lateinit var mSensorManager : SensorManager

    private val sensorList = arrayOf("Accelerometer", "Magnetometer", "Ambient-light", "Gyroscope", "Proximity")
    private var index : Int = 0
    private var mAccelerometer : Sensor ?= null
    private var mMagnetometer : Sensor ?= null
    private var mAmbient : Sensor ?= null
    private var mProximity : Sensor ?= null
    private var mGyroscope : Sensor ?= null
    private var resume = false;
    private var mPoints : ArrayList<Point> = ArrayList()
    private var mPointsY : ArrayList<Point> = ArrayList()
    private var mPointsZ : ArrayList<Point> = ArrayList()
    private var yNumber : Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        mAmbient = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        graphView = binding.graphview
        graphViewY = binding.graphViewY
        graphViewZ = binding.graphViewZ
        text_sensorName = binding.sensorName
        text_sensorValue = binding.sensorValue
        text_sensorName.setText(sensorList.get(0))
        text_sensorValue.setText("")

        binding.btNext.setOnClickListener( View.OnClickListener {
            index++;
            init();
        })

        binding.btPrev.setOnClickListener( View.OnClickListener {
            index--
            init()
        })

        binding.btSensor.setOnClickListener( View.OnClickListener {
            this.resume = !this.resume
            if(resume)
                binding.btSensor.setBackgroundResource(R.drawable.round_button_checked)
            else
                binding.btSensor.setBackgroundResource(R.drawable.round_button)
        })
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        print("okokoko");
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && resume) {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER && index == 0) {
                yNumber++;
                mPoints.add(Point(yNumber, event.values[0].toInt()))
                mPointsY.add(Point(yNumber, event.values[1].toInt()))
                mPointsZ.add(Point(yNumber, event.values[2].toInt()))
                graphView.drawChart(mPoints, Color.BLUE, Color.RED)
                graphViewY.drawChart(mPointsY, Color.BLUE, Color.BLUE)
                graphViewZ.drawChart(mPointsZ, Color.BLUE, Color.GREEN)
                findViewById<TextView>(R.id.sensorValue).text =
                    "aX: " + event.values[0].toString() + "(m/s^2)" +
                            "\naY: " + event.values[1].toString() + "(m/s^2)" +
                            "\naZ: " + event.values[2].toString() + "(m/s^2)";

            }
            if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD  && index == 1) {
                yNumber++;
                mPoints.add(Point(yNumber, event.values[0].toInt()))
                mPointsY.add(Point(yNumber, event.values[1].toInt()))
                mPointsZ.add(Point(yNumber, event.values[2].toInt()))
                graphView.drawChart(mPoints, Color.BLUE, Color.RED)
                graphViewY.drawChart(mPointsY, Color.BLUE, Color.BLUE)
                graphViewZ.drawChart(mPointsZ, Color.BLUE, Color.GREEN)
                findViewById<TextView>(R.id.sensorValue).text =
                    "X: " + event.values[0].toString() + "(uT)" +
                            "\nY: " + event.values[1].toString() + "(uT)" +
                            "\nZ: " + event.values[2].toString() + "(uT)";
            }
            if (event.sensor.type == Sensor.TYPE_GYROSCOPE  && index == 2) {
                yNumber++;
                mPoints.add(Point(yNumber, event.values[0].toInt()))
                graphView.drawChart(mPoints, Color.BLUE, Color.RED)
                findViewById<TextView>(R.id.sensorValue).text = event.values[0].toString() + "(rad/s)"
            }
            if (event.sensor.type == Sensor.TYPE_AMBIENT_TEMPERATURE  && index == 3) {
                yNumber++;
                mPoints.add(Point(yNumber, event.values[0].toInt()))
                graphView.drawChart(mPoints, Color.BLUE, Color.RED)
                findViewById<TextView>(R.id.sensorValue).text = event.values[0].toString() + "('C)"
            }
            if (event.sensor.type == Sensor.TYPE_PROXIMITY  && index == 4) {
                yNumber++;
                mPoints.add(Point(yNumber, event.values[0].toInt()))
                graphView.drawChart(mPoints, Color.BLUE, Color.RED)
                findViewById<TextView>(R.id.sensorValue).text = event.values[0].toString() + "(cm)"
            }
        }
    }

    fun init()
    {
        this.resume = false;
        text_sensorValue.setText("")
        yNumber = 0;
        mPoints = ArrayList()
        mPointsY = ArrayList()
        mPointsZ = ArrayList()
        graphView.clear()
        graphViewY.clear()
        graphViewZ.clear()
        binding.btSensor.setBackgroundResource(R.drawable.round_button)
        if (index < 0) index = 4
        if (index > 4) index = 0
        text_sensorName.setText(sensorList.get(index))
    }

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL)
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL)
        mSensorManager.registerListener(this, mAmbient, SensorManager.SENSOR_DELAY_NORMAL)
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(this)
    }
}