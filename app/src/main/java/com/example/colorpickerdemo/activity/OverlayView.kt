package com.example.colorpickerdemo.activity

import android.content.Context
import android.util.AttributeSet
import android.view.View

class YourOverlayView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private var xPosition = 0
    private var yPosition = 0

    fun updatePosition(x: Int, y: Int) {
        xPosition = x
        yPosition = y
        invalidate() // Kích hoạt lại việc vẽ lại view
    }
}