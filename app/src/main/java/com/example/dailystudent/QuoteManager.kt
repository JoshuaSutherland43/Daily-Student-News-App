package com.example.dailystudent
import android.content.Context
import android.content.SharedPreferences
import com.example.dailystudent.BuildConfig
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

// Handles fetching and storing quotes from the FavQs API
class QuoteManager(private val context: Context) {

    // HTTP client used for network requests
    private val client = OkHttpClient()

    // SharedPreferences used to store favorite quotes locally
    private val prefs: SharedPreferences = context.getSharedPreferences("quotes", Context.MODE_PRIVATE)

    // API key for the FavQs service
    private val apiKey = BuildConfig.FAVQS_API_KEY

    // Base URL for FavQs API
    private val baseUrl = "https://favqs.com/api"

    // Fetches the quote of the day from the API
    fun getQuoteOfTheDay(onResult: (quote: String, author: String) -> Unit) {
        val request = Request.Builder()
            .url("$baseUrl/qotd")
            .header("Authorization", "Token token=$apiKey")
            .build()

        // Sends request asynchronously
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            override fun onResponse(call: Call, response: Response) {
                val json = JSONObject(response.body?.string() ?: return)
                val quote = json.getJSONObject("quote")
                val body = quote.getString("body")
                val author = quote.getString("author")
                onResult(body, author)
            }
        })
    }

    // Fetches quotes by page number
    fun getQuoteByPage(page: Int, onResult: (quote: String, author: String) -> Unit) {
        val request = Request.Builder()
            .url("$baseUrl/quotes?page=$page")
            .header("Authorization", "Token token=$apiKey")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            override fun onResponse(call: Call, response: Response) {
                val json = JSONObject(response.body?.string() ?: return)
                val quotes = json.getJSONArray("quotes")
                if (quotes.length() > 0) {
                    val quote = quotes.getJSONObject(0)
                    val body = quote.getString("body")
                    val author = quote.getString("author")
                    onResult(body, author)
                }
            }
        })
    }

    // Saves a quote to local favorites
    fun saveFavorite(quote: String) {
        val set = prefs.getStringSet("favorites", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        set.add(quote)
        prefs.edit().putStringSet("favorites", set).apply()
    }

    // Retrieves the list of saved favorite quotes
    fun getFavorites(): List<String> {
        return prefs.getStringSet("favorites", emptySet())?.toList() ?: emptyList()
    }
}
