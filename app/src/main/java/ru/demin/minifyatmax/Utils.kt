package ru.demin.minifyatmax

import android.opengl.GLES30
import android.util.Log

fun log(message: String) = Log.d("OpenGLTag", message)

fun checkGLError(message: String){
    val error = GLES30.glGetError()
    if (error == GLES30.GL_NO_ERROR) {
        log("NO ERROR")
    } else {
        log("$message ERROR = ${"%x".format(error)}")
    }
}