package com.animeforum.app.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class AnimeApiService {
  suspend fun fetchRandomAnimeSuggestion(): AnimeSuggestion? = withContext(Dispatchers.IO) {
    val page = (1..5).random()
    val requestUrl = URL("https://api.jikan.moe/v4/top/anime?page=$page&limit=1")
    val connection = (requestUrl.openConnection() as HttpURLConnection).apply {
      requestMethod = "GET"
      connectTimeout = 5000
      readTimeout = 5000
    }

    try {
      val code = connection.responseCode
      if (code in 200..299) {
        val response = connection.inputStream.bufferedReader().use { it.readText() }
        val root = JSONObject(response)
        val data = root.optJSONArray("data") ?: return@withContext null
        if (data.length() == 0) return@withContext null

        val anime = data.getJSONObject(0)
        val title = anime.optString("title")
        val synopsis = anime.optString("synopsis")
        if (title.isNotBlank()) AnimeSuggestion(title = title, synopsis = synopsis) else null
      } else {
        null
      }
    } catch (_: Exception) {
      null
    } finally {
      connection.disconnect()
    }
  }
}

data class AnimeSuggestion(
  val title: String,
  val synopsis: String? = null
)
