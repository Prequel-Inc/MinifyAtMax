package ru.demin.minifyatmax

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import ru.demin.minifyatmax.MainActivity.Companion.RESOURCE_ID
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class SimpleTextureRenderer(private val context: Context) :
    GLSurfaceView.Renderer {
    private val vertexShaderProgramString = ResourceReader.readStringFromResource(context, R.raw.texture_vertex_shader)
    private val fragmentShaderProgramString =
        ResourceReader.readStringFromResource(context, R.raw.texture_fragment_shader)

    private var vertexShader: Int = 0
    private var fragmentShader: Int = 0
    private var program: Int = 0
    private var aTextureCoordinatesLocation: Int = 0
    private var aPositionLocation: Int = 0
    private var uTextureUnitLocation: Int = 0

    private val positionVertexes = floatArrayOf(
        // Triangle 1
        -1f, -1f,
        1f, 1f,
        -1f, 1f,
        // Triangle 2
        -1f, -1f,
        1f, 1f,
        1f, -1f,
    )

    private val textureVertexes = floatArrayOf(
        // Triangle 1
        0f, 0f,
        1f, 1f,
        0f, 1f,
        // Triangle 2
        0f, 0f,
        1f, 1f,
        1f, 0f,
    )

    private val positionVertexesArray = VertexArray(positionVertexes)
    private val textureVertexesArray = VertexArray(textureVertexes)
    private var textureId = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(0f, 0f, 0f, 0f)

        vertexShader = ShaderHelper.compileVertexShader(vertexShaderProgramString)
        fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderProgramString)
        program = ShaderHelper.linkProgram(vertexShader, fragmentShader)
        ShaderHelper.validateProgram(program)
        GLES30.glUseProgram(program)

        textureId = TextureHelper.loadTexture(context, RESOURCE_ID)

        aTextureCoordinatesLocation = GLES30.glGetAttribLocation(program, A_TEXTURE_COORDINATES)
        aPositionLocation = GLES30.glGetAttribLocation(program, A_POSITION)
        positionVertexesArray.setVertexAttributePointer(aPositionLocation, POSITION_COMPONENT_COUNT)
        textureVertexesArray.setVertexAttributePointer(aTextureCoordinatesLocation, TEXTURE_COMPONENT_COUNT)

        uTextureUnitLocation = GLES30.glGetUniformLocation(program, U_TEXTURE_UNIT)
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId)
        GLES30.glUniform1i(uTextureUnitLocation, 0)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        val param = when(MainActivity.filterType) {
            MainActivity.FilterType.GL_NEAREST_MIPMAP_NEAREST -> GLES30.GL_NEAREST_MIPMAP_NEAREST
            MainActivity.FilterType.GL_NEAREST_MIPMAP_LINEAR -> GLES30.GL_NEAREST_MIPMAP_LINEAR
            MainActivity.FilterType.GL_LINEAR_MIPMAP_NEAREST -> GLES30.GL_LINEAR_MIPMAP_NEAREST
            MainActivity.FilterType.GL_LINEAR_MIPMAP_LINEAR -> GLES30.GL_LINEAR_MIPMAP_LINEAR
        }
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, param)

        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, textureVertexes.size / TEXTURE_COMPONENT_COUNT)
    }

    companion object {
        private const val POSITION_COMPONENT_COUNT = 2
        private const val TEXTURE_COMPONENT_COUNT = 2
        private const val A_POSITION = "a_Position"
        private const val A_TEXTURE_COORDINATES = "a_TextureCoordinates"
        private const val U_TEXTURE_UNIT = "u_TextureUnit"
    }
}