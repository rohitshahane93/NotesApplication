package com.example.myapplication.util.color

import android.graphics.Color

object ColorUtil {

    fun getColorWithAlpha(color: Int, alpha: Float): Int {
        val a = Color.alpha(color)
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
        return Color.argb(
            java.lang.Float.valueOf(
                0f.coerceAtLeast(alpha * 255f)
            ).toInt(), r, g, b
        )
    }
}