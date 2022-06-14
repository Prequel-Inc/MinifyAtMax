package ru.demin.minifyatmax

import android.opengl.GLES32.*

object ShaderHelper {
    fun compileFragmentShader(shaderCode: String): Int {
        return compileShader(GL_FRAGMENT_SHADER, shaderCode)
    }

    fun compileVertexShader(shaderCode: String): Int {
        return compileShader(GL_VERTEX_SHADER, shaderCode)

    }

    fun linkProgram(vertexShaderId: Int, fragmentShaderId: Int): Int {
        val programId = glCreateProgram()
        if (programId == 0) {
            log("error in program creation")
            return 0
        }
        glAttachShader(programId, vertexShaderId)
        glAttachShader(programId, fragmentShaderId)


        glLinkProgram(programId)

        val linkStatus = IntArray(1)
        glGetProgramiv(programId, GL_LINK_STATUS, linkStatus, 0)

        if (linkStatus[0]==0) {
            log("Results of linking program:\n" + glGetProgramInfoLog(programId))
            glDeleteProgram(programId)
            return 0
        }

        return programId
    }

    fun validateProgram(programId: Int): Boolean {
        glValidateProgram(programId)
        val validateStatus = IntArray(1)
        glGetProgramiv(programId, GL_VALIDATE_STATUS, validateStatus, 0)

        log("validate status of program $programId = ${validateStatus[0]}" + glGetProgramInfoLog(programId))

        return validateStatus[0] != 0
    }

    private fun compileShader(shaderType: Int, shaderCode: String): Int {
        val shaderObjectId = glCreateShader(shaderType)
        if (shaderObjectId == 0) {
            log("error in shader creation")
            return 0
        }

        glShaderSource(shaderObjectId, shaderCode)
        glCompileShader(shaderObjectId)

        val compileStatus = IntArray(1)
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0)

        if (compileStatus[0]==0) {
            log(
                "Results of compiling source:" + "\n" + shaderCode + "\n:"
                        + glGetShaderInfoLog(shaderObjectId)
            )
            glDeleteShader(shaderObjectId)
            return 0
        }
        return shaderObjectId
    }
}