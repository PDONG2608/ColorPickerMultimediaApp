package com.example.colorpickerdemo.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.colorpickerdemo.Contants.Constants.CAMERA_REQUEST_CODE
import com.example.colorpickerdemo.Contants.Constants.PICK_IMAGE_REQUEST
import com.example.colorpickerdemo.R
import com.example.colorpickerdemo.databinding.ActivityReplaceColorBinding
import com.example.colorpickerdemo.viewmodel.ColorPickerViewModel
import java.io.ByteArrayOutputStream
import java.io.IOException
import kotlin.math.pow
import kotlin.math.roundToInt

class ReplaceColorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReplaceColorBinding
    private lateinit var viewModel: ColorPickerViewModel

    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var scaleFactor = 1.0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReplaceColorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(ColorPickerViewModel::class.java)

        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())

        binding.buttonOpenGallery.setOnClickListener {
            openGallery()
        }
        binding.buttonOpenCamera.setOnClickListener {
            openCamera()
        }
        observeViewModel()

        binding.imageView.setOnTouchListener { _, event ->
            handleTouch(event)
            true
        }
    }

    private fun handleTouch(event: MotionEvent?) {
        when (event?.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                val x = event.x.toInt()
                val y = event.y.toInt()
                if (x in 0 until binding.imageView.width && y in 0 until binding.imageView.height) {
                    val pixel = getPixelColorFromImage(event)
                    updateTextViewPixel(pixel)
                }
            }

            else -> false
        }
    }

    private fun getPixelColorFromImage(event: MotionEvent): Int {
        val bitmap: Bitmap = getBitmapFromView(binding.imageView)
        return bitmap.getPixel(event.x.toInt(), event.y.toInt())
    }

    private fun updateTextViewPixel(pixel: Int) {
        val red = Color.red(pixel)
        val green = Color.green(pixel)
        val blue = Color.blue(pixel)
        val hsv = FloatArray(3)
        Color.RGBToHSV(red, green, blue, hsv)
        val hue = round(hsv[0], 3)
        binding.imageColor.setBackgroundColor(Color.rgb(red, green, blue))
        val colorName = getColorName(hue)
        binding.textViewColor.text = " $colorName"
    }


    private fun getColorName(hue: Float): String {
        val segmentSize = 360 / viewModel.colorList.size
        val segmentIndex = (hue / segmentSize).toInt() % viewModel.colorList.size
        Log.d("phuongdong", "getColorName $segmentIndex")
        return viewModel.colorList[segmentIndex].toString()
    }

    private fun round(value: Float, places: Int): Float {
        require(places >= 0)
        return (value * 10.0.pow(places.toDouble())).roundToInt() / 10.0.pow(places.toDouble())
            .toFloat()
    }

    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(
            view.width, view.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

    private fun observeViewModel() {
        viewModel.selectedImageUrl.observe(this) { imageUrl ->
            binding.imageView.setImageURI(Uri.parse(imageUrl))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            selectedImageUri?.toString()?.let {
                viewModel.setSelectedImage(it)
            }
        }

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val imageBitmap = data.extras?.get("data") as Bitmap?
            if (imageBitmap != null) {
                val imageUri = getImageUri(this, imageBitmap)
                loadImage(imageUri)
            }
        }
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun loadImage(selectedImageUri: Uri) {
        try {
            val imageBitmap: Bitmap =
                MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImageUri)
            binding.imageView.setImageBitmap(imageBitmap)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            scaleFactor = 0.1f.coerceAtLeast(scaleFactor.coerceAtMost(5.0f))
            binding.imageView.scaleX = scaleFactor
            binding.imageView.scaleY = scaleFactor
            return true
        }
    }
}