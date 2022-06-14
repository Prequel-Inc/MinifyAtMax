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

        gl_nearest_mipmap_nearest.setOnClickListener { minifyTexture(FilterType.GL_NEAREST_MIPMAP_NEAREST) }
        gl_nearest_mipmap_linear.setOnClickListener { minifyTexture(FilterType.GL_NEAREST_MIPMAP_LINEAR) }
        gl_linear_mipmap_nearest.setOnClickListener { minifyTexture(FilterType.GL_LINEAR_MIPMAP_NEAREST) }
        gl_linear_mipmap_linear.setOnClickListener { minifyTexture(FilterType.GL_LINEAR_MIPMAP_LINEAR) }
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
                        FilterType.GL_NEAREST_MIPMAP_NEAREST -> "GL_NEAREST_MIPMAP_NEAREST.${IMAGE_EXTENSION}"
                        FilterType.GL_NEAREST_MIPMAP_LINEAR -> "GL_NEAREST_MIPMAP_LINEAR.${IMAGE_EXTENSION}"
                        FilterType.GL_LINEAR_MIPMAP_NEAREST-> "GL_LINEAR_MIPMAP_NEAREST.${IMAGE_EXTENSION}"
                        FilterType.GL_LINEAR_MIPMAP_LINEAR -> "GL_LINEAR_MIPMAP_LINEAR.${IMAGE_EXTENSION}"
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
        GL_NEAREST_MIPMAP_NEAREST,
        GL_NEAREST_MIPMAP_LINEAR,
        GL_LINEAR_MIPMAP_NEAREST,
        GL_LINEAR_MIPMAP_LINEAR,
    }

    companion object {
        const val RESOURCE_ID = R.drawable.blue_reed_5_10
        const val SOURCE_IMAGE_SIZE = 10
        var filterType = FilterType.GL_NEAREST_MIPMAP_NEAREST
        private const val DELAY_FOR_DRAWING = 100L
        private const val IMAGE_EXTENSION = "png"
    }
}