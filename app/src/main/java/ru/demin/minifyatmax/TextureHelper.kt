package ru.demin.minifyatmax

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES30.*
import android.opengl.GLUtils.texImage2D
import android.util.Log
import androidx.annotation.DrawableRes
import kotlinx.android.synthetic.main.activity_main.*
import ru.demin.minifyatmax.MainActivity.Companion.SOURCE_IMAGE_SIZE
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer

object TextureHelper {
    fun loadTexture(context: Context, @DrawableRes resource: Int): Int {
        val texturesIds = IntArray(1)
        glGenTextures(1, texturesIds, 0)
        if (texturesIds[0] == 0) {
            log("Could not generate texture")
            return 0
        }

        glBindTexture(GL_TEXTURE_2D, texturesIds[0])

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, SOURCE_IMAGE_SIZE, SOURCE_IMAGE_SIZE, 0, GL_RGBA, GL_UNSIGNED_BYTE, getByteBufferFromResource(context, resource))
        glBindTexture(GL_TEXTURE_2D, 0)
        return texturesIds[0]
    }

    fun readPixels(context: Context): Bitmap {
        val size = context.resources.getDimension(R.dimen.scale_size).toInt()
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)

        val buffer: ByteBuffer = ByteBuffer.allocateDirect(size * size * BYTES_PER_PIXEL)
        glReadPixels(0, 0, size, size, GL_RGBA, GL_UNSIGNED_BYTE, buffer)
        bitmap.copyPixelsFromBuffer(buffer)

        return bitmap
    }

    private fun getByteBufferFromResource(context: Context, @DrawableRes resource: Int): ByteBuffer {
        val bitmap = BitmapFactory.decodeResource(
            context.resources,
            resource,
            BitmapFactory.Options().apply { inScaled = false })

        val buffer = ByteBuffer.allocateDirect(bitmap.byteCount)
        bitmap.copyPixelsToBuffer(buffer)
        buffer.flip()
        return buffer
    }

    private const val BYTES_PER_PIXEL = 4

}