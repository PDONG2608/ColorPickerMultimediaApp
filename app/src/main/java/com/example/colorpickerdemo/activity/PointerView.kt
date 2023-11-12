package com.example.colorpickerdemo.activity

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.colorpickerdemo.R

class PointerView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var pointerPaint = Paint().apply {
        color = Color.RED
    }
    private var pointerX = 0f
    private var pointerY = 0f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(pointerX, pointerY, 20f, pointerPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        pointerX = event.x
        pointerY = event.y
        invalidate()
        return true
    }
}
