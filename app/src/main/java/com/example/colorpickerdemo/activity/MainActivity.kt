package com.example.colorpickerdemo.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.colorpickerdemo.R
import com.example.colorpickerdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonColorPicker.setOnClickListener {
            val intent = Intent(this, ColorPickerActivity::class.java)
            startActivity(intent)
        }

        binding.buttonReplaceColor.setOnClickListener {
            val intent = Intent(this, ReplaceColorActivity::class.java)
            startActivity(intent)
        }
    }
}