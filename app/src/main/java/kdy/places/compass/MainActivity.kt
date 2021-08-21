package kdy.places.compass

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kdy.places.compass.R
import kdy.places.compass.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager

    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)
    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    private var _angle: MutableLiveData<Int> = MutableLiveData(90)
    val angle : LiveData<Int> = _angle

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        val binding : ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        val service : Any? = getSystemService(SENSOR_SERVICE)

        if (service != null) {
            sensorManager = service as SensorManager
        }

        binding.angle = angle

        binding.lifecycleOwner = this  // use Fragment.viewLifecycleOwner for fragments
    }

    override fun onResume() {
        super.onResume()
        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
            )
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) {
            return
        }

        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
        }

        updateOrientationAngles()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        val sensorName = sensor?.name
        Log.d("onAccuracyChanged", "sensor <$sensorName> accuracy is <$accuracy>")
    }

    private fun updateOrientationAngles() {
        val result = SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            accelerometerReading,
            magnetometerReading
        )

        if (result){
            val orientation = SensorManager.getOrientation(rotationMatrix, orientationAngles)
            val degrees : Double = ((Math.toDegrees(orientation.get(0).toDouble()) + 360.0) % 360.0).toDouble()

            _angle.value = Math.round(degrees).toInt()
        }
    }
}
