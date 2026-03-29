package data.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import io.ktor.client.statement.*

object ApiClient {
    // Backend URL
    @PublishedApi internal const val BASE_URL = "https://backend-mangli.vercel.app/api"

    // Token JWT
    var token: String? = null

    // HttpClient Ktor
    @PublishedApi
    internal val client = HttpClient {
        install(ContentNegotiation) {
            json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    }
            )
        }
    }

    // POST Request
     suspend inline fun <reified T> post(endpoint: String, body: Any): T {
        val response = client.post("$BASE_URL$endpoint") {
            contentType(ContentType.Application.Json)
            setBody(body)
            token?.let { bearerAuth(it) }
        }
        if (response.status.value !in 200..299) {
            val errorBody = response.bodyAsText()
            throw Exception("Login gagal: $errorBody")
        }
        return response.body()
    }

    // GET Request
    suspend inline fun <reified T> get(endpoint: String): T {
        return client.get("$BASE_URL$endpoint") { token?.let { bearerAuth(it) } }.body()
    }

    // PATCH Request
    suspend inline fun <reified T> patch(endpoint: String, body: Any? = null): T {
        return client
                .patch("$BASE_URL$endpoint") {
                    contentType(ContentType.Application.Json)
                    body?.let { setBody(it) }
                    token?.let { bearerAuth(it) }
                }
                .body()
    }

    // PUT Request
    suspend inline fun <reified T> put(endpoint: String, body: Any) : T{
        val response = client.put("$BASE_URL$endpoint"){
            contentType(ContentType.Application.Json)
            setBody(body)
            token?.let{
                bearerAuth(it)
            }
        }
        if (response.status.value !in 200..299){
            val errorBody = response.bodyAsText()
            throw Exception("Request gagal: $errorBody")
        }
        return response.body()
    }

    // DELETE Request
    suspend inline fun <reified T> delete(endpoint: String) : T{
        val response = client.delete("$BASE_URL$endpoint"){
            token?.let{
                bearerAuth(it)
            }
        }
        if (response.status.value !in 200..299){
            val errorBody = response.bodyAsText()
            throw Exception("Request gagal: $errorBody")
        }
        return response.body()
    }
}
