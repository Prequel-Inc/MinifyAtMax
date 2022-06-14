package ru.demin.minifyatmax

import android.content.Context
import androidx.annotation.RawRes
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder

object ResourceReader {
    fun readStringFromResource(context: Context, @RawRes resource: Int): String {
        val builder = StringBuilder()

        val reader = BufferedReader(InputStreamReader(context.resources.openRawResource(resource)))

        var line: String?
        while (reader.readLine().also { line = it } != null) {
            builder.append(line).append('\n')
        }

        return builder.toString()
    }
}