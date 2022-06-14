package ru.demin.minifyatmax

import android.graphics.Bitmap
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scale_nearest.setOnClickListener { minifyTexture(FilterType.NEIGHBOUR) }
        scale_linear.setOnClickListener { minifyTexture(FilterType.BILINEAR) }
    }

    private fun minifyTexture(filterType: FilterType) {
        MainActivity.filterType = filterType
        val glView = GLSurfaceView(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            setupSurface()
        }
        container.run {
            removeAllViews()
            addView(glView)
        }
        glView.run {
            postDelayed({
                queueEvent {
                    val bitmap = TextureHelper.readPixels(this@MainActivity)
                    val fileName = when (filterType) {
                        FilterType.NEIGHBOUR -> "opengl_neighbour.${IMAGE_EXTENSION}"
                        FilterType.BILINEAR -> "opengl_bilinear.${IMAGE_EXTENSION}"
                    }
                    writeBitmapToFile(bitmap, fileName)
                    post {
                        Toast.makeText(
                            this@MainActivity,
                            "scaled with $filterType saved to $fileName",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }, DELAY_FOR_DRAWING)
        }
    }

    private fun writeBitmapToFile(bitmap: Bitmap, fileName: String) {
        FileOutputStream(File(filesDir, fileName)).use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
    }

    private fun GLSurfaceView.setupSurface() {
        setEGLContextClientVersion(3)
        setRenderer(SimpleTextureRenderer(this@MainActivity))
        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }

    enum class FilterType {
        NEIGHBOUR, BILINEAR
    }

    companion object {
        const val RESOURCE_ID = R.drawable.red_blue_source
        const val SOURCE_IMAGE_SIZE = 100
        var filterType = FilterType.NEIGHBOUR
        private const val DELAY_FOR_DRAWING = 100L
        private const val IMAGE_EXTENSION = "png"
    }
}