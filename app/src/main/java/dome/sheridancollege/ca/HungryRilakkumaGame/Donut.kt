@file:JvmName("DonutUtils")

package dome.sheridancollege.ca.HungryRilakkumaGame

import android.widget.ImageView

//class and constructor in one
class Donut constructor(var donutImage: ImageView, var isDonutAlive: Boolean)

internal var MIN_X = 0
internal var MAX_X: Int = 0
internal var MIN_Y = 0
internal var MAX_Y: Int = 0

fun createDonut(donut: Donut, MAX_X: Int, MAX_Y: Int) {
    //if there is no donut out, create new one
    var dx: Double
    var dy: Double

    //while out of bounds, keep rerolling X
    do {
        dx = Math.random() * (MAX_X - MIN_X + 1) + donut.donutImage.getLayoutParams().width
    } while (dx > MAX_X - donut.donutImage.getLayoutParams().width || dx < donut.donutImage.getLayoutParams().width)

    //while out of bounds, keep rerolling Y
    do {
        dy = Math.random() * (MAX_Y - MIN_Y + 1) + donut.donutImage.getLayoutParams().height
    } while (dy > MAX_Y - donut.donutImage.getLayoutParams().height || dy < donut.donutImage.getLayoutParams().height)

    println("X: $dx")
    println("Y: $dy")
    donut.donutImage.setTranslationX(dx.toFloat())
    donut.donutImage.setTranslationY(dy.toFloat())
    donut.isDonutAlive = true
}