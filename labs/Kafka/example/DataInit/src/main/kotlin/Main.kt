import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

fun main() {
    val apiUrl = "http://localhost:8080/addStudent"

    repeat(10) { index ->
        val student = StudentDto(FIODto("Ilya","$index",""),index.toLong(), listOf(3,4,5))
        val json = Json.encodeToString(student)

        val connection = URL(apiUrl).openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.doOutput = true
        connection.setRequestProperty("Content-Type", "application/json; utf-8")

        try {
            OutputStreamWriter(connection.outputStream).use {
                it.write(json)
            }

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                println("Request ${index + 1}: Success")
            } else {
                println("Request ${index + 1}: Failed. Response Code: $responseCode")
            }
        } finally {
            connection.disconnect()
        }
    }
}
