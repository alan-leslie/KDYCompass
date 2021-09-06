package kdy.places.compass

class Bearing(_value: Float) {
    val value: Float = if (_value.isNaN() || !_value.isFinite()) 0f else normalizeAngle(_value)
}