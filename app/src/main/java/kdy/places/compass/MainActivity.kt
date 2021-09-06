package kdy.places.compass

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import kdy.places.compass.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var compass: OrientationSensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        try {
            compass = OrientationSensor(this)
        } catch (e:IllegalStateException) {
            e.printStackTrace()
            Toast.makeText(this, "Either accelerometer or magnetic sensor not found" , Toast.LENGTH_LONG).show()
        }

        val binding : ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.compass = compass

        binding.lifecycleOwner = this  // use Fragment.viewLifecycleOwner for fragments
    }

    override fun onResume() {
        super.onResume()
        compass!!.start(this)
    }

    override fun onPause() {
        super.onPause()
        compass!!.stop()
    }
}
