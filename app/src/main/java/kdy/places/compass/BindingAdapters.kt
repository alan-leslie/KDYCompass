package kdy.places.compass

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("app:angleAndDirection")
fun angleAndDirection(view: TextView, number: Int) {
    val direction = getDirection(number)
    view.text = "$number  $direction"
}

private fun getDirection(angle: Int): String {
    var direction = ""

    if (angle >= 350 || angle <= 10)
        direction = "N"
    if (angle in 281..349)
        direction = "NW"
    if (angle in 261..280)
        direction = "W"
    if (angle in 191..260)
        direction = "SW"
    if (angle in 171..190)
        direction = "S"
    if (angle in 101..170)
        direction = "SE"
    if (angle in 81..100)
        direction = "E"
    if (angle in 11..80)
        direction = "NE"

    return direction
}
