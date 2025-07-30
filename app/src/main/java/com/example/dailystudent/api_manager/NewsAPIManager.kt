package com.example.dailystudent.api_manager

import com.example.dailystudent.models.NewsArticle
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import com.example.dailystudent.BuildConfig



// Manages fetching news articles from the NewsAPI
object NewsAPIManager {

    // HTTP client for making requests
    private val client = OkHttpClient()

    // News API key pulled from BuildConfig
    private const val API_KEY = BuildConfig.NEWS_API_KEY

    // Fetches news articles based on query or category
    fun fetchNews(
        title: String?,
        category: String,
        page: Int,
        callback: (Result<List<NewsArticle>>) -> Unit
    ) {
        val request = Request.Builder()
            .url(buildUrl(title, category, page))
            .build()

        // Sends request and handles success/failure
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                callback(Result.failure(e))
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    callback(Result.failure(IOException("Unexpected API response: ${response.code}")))
                    return
                }

                try {
                    val articles = parseArticles(response.body?.string())
                    callback(Result.success(articles))
                } catch (e: JSONException) {
                    e.printStackTrace()
                    callback(Result.failure(e))
                }
            }
        })
    }

    // Builds the API request URL based on query and category
    private fun buildUrl(query: String?, category: String, page: Int): HttpUrl {
        val isSearch = !query.isNullOrEmpty()
        val endpoint = if (isSearch) "everything" else "top-headlines"

        val urlBuilder = HttpUrl.Builder()
            .scheme("https")
            .host("newsapi.org")
            .addPathSegment("v2")
            .addPathSegment(endpoint)
            .addQueryParameter("page", page.toString())
            .addQueryParameter("pageSize", "10")
            .addQueryParameter("apiKey", API_KEY)

        if (isSearch) {
            urlBuilder.addQueryParameter("q", query)
        } else {
            urlBuilder.addQueryParameter("country", "us")
            if (category != "all") {
                urlBuilder.addQueryParameter("category", category)
            }
        }

        return urlBuilder.build()
    }

    // Parses JSON response into a list of NewsArticle objects
    private fun parseArticles(jsonString: String?): List<NewsArticle> {
        if (jsonString.isNullOrEmpty()) return emptyList()

        val articleList = mutableListOf<NewsArticle>()
        val jsonObject = JSONObject(jsonString)
        val articlesArray = jsonObject.getJSONArray("articles")

        for (i in 0 until articlesArray.length()) {
            val articleObject = articlesArray.getJSONObject(i)
            articleList.add(
                NewsArticle(
                    title = articleObject.optString("title", "No Title"),
                    description = articleObject.optString("description", ""),
                    url = articleObject.optString("url"),
                    urlToImage = articleObject.optString("urlToImage")
                )
            )
        }

        return articleList
    }
}
