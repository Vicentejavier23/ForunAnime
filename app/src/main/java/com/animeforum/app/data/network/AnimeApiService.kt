package com.animeforum.app.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class AnimeApiService {

  suspend fun fetchRandomAnimeSuggestion(): AnimeSuggestion? = withContext(Dispatchers.IO) {
    val page = (1..50).random()
    val requestUrl = URL("https://api.jikan.moe/v4/top/anime?page=$page&limit=1")
    val connection = (requestUrl.openConnection() as HttpURLConnection).apply {
      requestMethod = "GET"
      connectTimeout = 5000
      readTimeout = 5000
    }

    try {
      val response = connection.inputStream.bufferedReader().use { it.readText() }
      val root = JSONObject(response)
      val data = root.optJSONArray("data") ?: return@withContext null
      if (data.length() == 0) return@withContext null

      val anime = data.getJSONObject(0)
      val title = anime.optString("title")
      val synopsis = anime.optString("synopsis")

      val translatedSynopsis = translateToSpanish(synopsis)

      AnimeSuggestion(
        title = title,
        synopsis = translatedSynopsis ?: synopsis
      )

    } catch (_: Exception) {
      null
    } finally {
      connection.disconnect()
    }
  }

  private fun safeJson(value: String?): String? {
    return if (value == "null" || value.isNullOrBlank()) null else value
  }

  private fun sleepControl(ms: Long = 300L) = try {
    Thread.sleep(ms)
  } catch (_: Exception) { }

  private fun cleanText(text: String): String =
    text.replace("\n", " ").replace("\r", " ").trim()

  private fun requestTranslation(part: String): String? {
    val encoded = URLEncoder.encode(part, "UTF-8")
    val url = URL("https://api.mymemory.translated.net/get?q=$encoded&langpair=en|es")

    val connection = (url.openConnection() as HttpURLConnection).apply {
      requestMethod = "GET"
      connectTimeout = 6000
      readTimeout = 6000
    }

    return try {
      val response = connection.inputStream.bufferedReader().use { it.readText() }
      val json = JSONObject(response)
      val responseData = json.optJSONObject("responseData")
      safeJson(responseData?.optString("translatedText"))
    } catch (_: Exception) {
      null
    } finally {
      connection.disconnect()
    }
  }

  private fun splitText(text: String, size: Int = 400): List<String> {
    if (text.length <= size) return listOf(text)
    val result = mutableListOf<String>()
    var index = 0
    while (index < text.length) {
      val end = (index + size).coerceAtMost(text.length)
      result.add(text.substring(index, end))
      index += size
    }
    return result
  }

  private suspend fun translateToSpanish(text: String?): String? = withContext(Dispatchers.IO) {
    if (text.isNullOrBlank()) return@withContext null

    val cleaned = cleanText(text)
    val parts = splitText(cleaned)

    val translated = mutableListOf<String>()

    for (part in parts) {
      val t = requestTranslation(part)
      translated.add(t ?: part)
      sleepControl(300)
    }

    return@withContext translated.joinToString(" ")
  }
}

data class AnimeSuggestion(val title: String, val synopsis: String)




