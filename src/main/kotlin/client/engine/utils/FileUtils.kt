package client.engine.utils

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object FileUtils {

    fun loadAsString(path: String): String {
        val result = StringBuilder()
        try {
            val reader = BufferedReader(InputStreamReader(FileUtils::class.java.getResourceAsStream(path)))
            var line = reader.readLine()
            while (line != null) {
                result.append(line).append("\n")
                line = reader.readLine()
            }
        } catch (e: IOException) {
            System.err.println("Couldn't find the file at $path")
        }
        catch (e: IOException) {
            System.err.println("Couldn't find the file at $path")
        }

        return result.toString()
    }
//vec3(position.x, position.y - position.x, position.y);
}