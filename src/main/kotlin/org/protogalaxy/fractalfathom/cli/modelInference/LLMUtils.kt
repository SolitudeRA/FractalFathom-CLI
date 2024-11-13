package org.protogalaxy.fractalfathom.cli.modelInference

import kotlinx.coroutines.runBlocking
import okhttp3.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.protogalaxy.fractalfathom.cli.analysis.ir.IRClassEntity
import java.util.concurrent.TimeUnit

class LLMUtils {

    private val client = OkHttpClient.Builder()
        .connectTimeout(300, TimeUnit.SECONDS)
        .readTimeout(300, TimeUnit.SECONDS)
        .writeTimeout(300, TimeUnit.SECONDS)
        .build()
    private val mapper = jacksonObjectMapper()
    private val endpointURL = "http://localhost:5000/generate_plantuml"

    fun generatePlantUML(irClasses: List<IRClassEntity>): String = runBlocking {
        // Prepare data in chunks if necessary
        val plantUMLCode = callGPT4API(irClasses)
        plantUMLCode
    }

    private fun callGPT4API(irClasses: List<IRClassEntity>): String {
        val jsonData = mapper.writeValueAsString(mapOf("ir_classes" to irClasses))
        val requestBody = jsonData.toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(endpointURL)
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw Exception("Unexpected code $response")

            val responseJson = response.body?.string() ?: throw Exception("Empty response body")
            val responseMap: Map<String, String> = mapper.readValue(responseJson)

            return responseMap["plantuml_code"] ?: throw Exception("No PlantUML code in response")
        }
    }
}