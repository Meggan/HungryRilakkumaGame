package dome.sheridancollege.ca.HungryRilakkumaGame

import android.widget.ImageView

class Rilakkuma constructor(var rilakkumaImage: ImageView, var isRunning: Boolean)


var translationX: Float = 0.toFloat() // X coordinate for rilakkuma
var translationY: Float = 0.toFloat() // Y coordinate for rilakkuma
var rilakkumaWidth: Int = 0
var rilakkumaHeight: Int = 0


//move direction based on compass
fun moveDirection(rilakkuma: Rilakkuma, azimuth: Double, MAX_X: Int, MAX_Y: Int, speed: Int) {

    rilakkumaWidth = rilakkuma.rilakkumaImage.layoutParams.width
    rilakkumaHeight = rilakkuma.rilakkumaImage.layoutParams.height

    //move north (up) 0-44
    if (azimuth < 45 && translationY >= speed && translationY <= MAX_Y) {
        moveNorth(rilakkuma,speed)
    } //move northeast 45-89
    else if (azimuth >= 45 && azimuth < 90 && translationY >= speed && translationY <= MAX_Y && translationX >= 0 && translationX <= MAX_X) {
        moveNorth(rilakkuma,speed)
        moveEast(rilakkuma,speed)
    }//move east (right) 90-179
    else if (azimuth >= 90 && azimuth < 135 && translationX >= 0 && translationX <= MAX_X) {
        moveEast(rilakkuma,speed)
    }//move southeast 135-179
    else if (azimuth >= 135 && azimuth < 180 && translationY >= -speed && translationY <= MAX_Y - rilakkumaHeight && translationX >= 0 && translationX <= MAX_X) {
        moveSouth(rilakkuma,speed)
        moveEast(rilakkuma,speed)
    }//move south (down) 180-224
    else if (azimuth >= 180 && azimuth < 225 && translationY >= -speed && translationY <= MAX_Y - rilakkumaHeight) {
        moveSouth(rilakkuma,speed)
    } //move southwest 225-269
    else if (azimuth >= 225 && azimuth < 280 && translationY >= -speed && translationY <= MAX_Y - rilakkumaHeight && translationX >= -speed && translationX <= MAX_X - rilakkumaWidth) {
        moveSouth(rilakkuma,speed)
        moveWest(rilakkuma,speed)
    }// move left (west) 270-314
    else if (azimuth >= 270 && azimuth < 315 && translationX >= -speed && translationX <= MAX_X - rilakkumaWidth) {
        moveWest(rilakkuma,speed)
    } //move northwest 315-360
    else if (azimuth >= 315 && translationY >= speed && translationY <= MAX_Y && translationX >= -speed && translationX <= MAX_X - rilakkumaWidth) {
        moveNorth(rilakkuma,speed)
        moveWest(rilakkuma,speed)
    }

}

fun moveWest(rilakkuma: Rilakkuma, speed: Int) {
    translationX += speed
    rilakkuma.rilakkumaImage.translationX = translationX
}

fun moveEast(rilakkuma: Rilakkuma, speed: Int) {
    translationX -= speed
    rilakkuma.rilakkumaImage.translationX = translationX
}

fun moveNorth(rilakkuma: Rilakkuma, speed: Int) {
    translationY -= speed
    rilakkuma.rilakkumaImage.translationY = translationY
}

fun moveSouth(rilakkuma: Rilakkuma, speed: Int) {
    translationY += speed
    rilakkuma.rilakkumaImage.translationY = translationY
}
