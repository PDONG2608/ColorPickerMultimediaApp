package com.example.colorpickerdemo.activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.colorpickerdemo.R

class MainActivity : AppCompatActivity() {

    private lateinit var buttonColorPicker: Button
    private lateinit var buttonReplaceColor: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonColorPicker = findViewById(R.id.button_color_picker)
        buttonReplaceColor = findViewById(R.id.button_replace_color)
        buttonColorPicker.setOnClickListener {
            val intent = Intent(this, ColorPickerActivity::class.java)
            startActivity(intent)
        }

        buttonReplaceColor.setOnClickListener {
            val intent = Intent(this, ReplaceColorActivity::class.java)
            startActivity(intent)
        }
    }
}