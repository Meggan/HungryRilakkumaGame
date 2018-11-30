@file:JvmName("DonutUtils")

package dome.sheridancollege.ca.HungryRilakkumaGame

import android.widget.ImageView

//class and constructor in one
class Donut constructor(var donutImage: ImageView, var isDonutAlive: Boolean)

fun createDonut(donut: Donut, MAX_X: Int, MAX_Y: Int) {
    //if there is no donut out, create new one
    var dx: Double
    var dy: Double

    //while out of bounds, keep re-rolling X
    do {
        dx = Math.random() * (MAX_X + 1) + donut.donutImage.getLayoutParams().width
    } while (dx > MAX_X - donut.donutImage.getLayoutParams().width || dx < donut.donutImage.getLayoutParams().width)

    //while out of bounds, keep re-rolling Y
    do {
        dy = Math.random() * (MAX_Y - 1) + donut.donutImage.getLayoutParams().height
    } while (dy > MAX_Y - donut.donutImage.getLayoutParams().height || dy < donut.donutImage.getLayoutParams().height)

    println("X: $dx")
    println("Y: $dy")
    donut.donutImage.setTranslationX(dx.toFloat())
    donut.donutImage.setTranslationY(dy.toFloat())
    donut.isDonutAlive = true
}