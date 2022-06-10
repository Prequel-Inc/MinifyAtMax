package ru.demin.minifyatmax

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        check_scaling.setOnClickListener {
            val bitmapOptions = BitmapFactory.Options().apply { inScaled = false }
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.red_blue_source, bitmapOptions)

            scaleBitmap(bitmap, FilterType.NEIGHBOUR)
            handler.postDelayed({scaleBitmap(bitmap, FilterType.BILINEAR)}, SCALING_DELAY_IN_MILLIS)
        }
    }

    private fun scaleBitmap(source: Bitmap, filterType: FilterType) {
        val flag = filterType == FilterType.BILINEAR
        val scaledBitmap = Bitmap.createScaledBitmap(source, SCALED_SIZE, SCALED_SIZE, flag)
        val fileName = when(filterType) {
            FilterType.NEIGHBOUR -> "neighbour.${IMAGE_EXTENSION}"
            FilterType.BILINEAR -> "bilinear.${IMAGE_EXTENSION}"
        }
        writeBitmapToFile(scaledBitmap, fileName)
        Toast.makeText(this, "scaled with $filterType saved to $fileName", Toast.LENGTH_SHORT).show()
    }

    private fun writeBitmapToFile(bitmap: Bitmap, fileName: String) {
        FileOutputStream(File(filesDir, fileName)).use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
    }

    enum class FilterType{
        NEIGHBOUR, BILINEAR
    }



    companion object {
        private const val SCALED_SIZE = 3
        private const val IMAGE_EXTENSION = "png"
        private const val SCALING_DELAY_IN_MILLIS = 1000L
    }
}