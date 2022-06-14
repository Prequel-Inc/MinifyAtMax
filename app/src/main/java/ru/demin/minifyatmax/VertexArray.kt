package ru.demin.minifyatmax

import android.opengl.GLES32.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class VertexArray(vertexData: FloatArray) {
    private val floatBuffer: FloatBuffer = ByteBuffer
        .allocateDirect(vertexData.size * BYTES_PER_FLOAT)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer()
        .put(vertexData)

    fun setVertexAttributePointer(attributeLocation: Int, componentCount: Int) {
        floatBuffer.position(0)
        glVertexAttribPointer(attributeLocation, componentCount, GL_FLOAT, false, 0, floatBuffer)
        glEnableVertexAttribArray(attributeLocation)
    }

    companion object {
        const val BYTES_PER_FLOAT = 4
    }
}