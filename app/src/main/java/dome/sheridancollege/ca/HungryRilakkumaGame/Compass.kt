package dome.sheridancollege.ca.HungryRilakkumaGame

import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView

class Compass constructor(var compassImage: ImageView)

fun moveCompassDirection(compass: Compass, currentDegree: Float, degree: Float){
// create a rotation animation (reverse turn degree degrees)
    var ra = RotateAnimation(
            currentDegree,
            -degree, Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f)
// how long the animation will take place
    ra.duration = 210
// set the animation after the end of the reservation status
    ra.fillAfter = true

    // Start the animation
    compass.compassImage.startAnimation(ra)
}
