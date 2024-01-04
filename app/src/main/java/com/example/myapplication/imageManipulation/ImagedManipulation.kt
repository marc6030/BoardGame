package com.example.myapplication.imageManipulation

import android.R.color
import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color


class ImagedManipulation {
    fun getAverageColor(bitmap: Bitmap): Color{
        var totalRed = 0
        var totalGreen = 0
        var totalBlue = 0
        for(i in 0 until bitmap.height) {
            for (j in 0 until bitmap.width){
                val color = bitmap.getPixel(j, i) // Swap i and j for correct pixel access
                val red: Int = color shr 16 and 0xFF
                val green: Int = color shr 8 and 0xFF
                val blue: Int = color and 0xFF
                totalRed += red
                totalGreen += green
                totalBlue += blue
            }
        }
        val pixelCount = bitmap.width * bitmap.height
        val avgRed = totalRed / pixelCount
        val avgGreen = totalGreen / pixelCount
        val avgBlue = totalBlue / pixelCount

        return Color(red = avgRed, green = avgGreen, blue = avgBlue)
    }
}