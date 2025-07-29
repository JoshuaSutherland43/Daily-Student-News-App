package com.example.dailystudent

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailystudent.adapters.NewsAdapter
import com.example.dailystudent.api_manager.NewsAPIManager
import com.example.dailystudent.models.NewsArticle
import android.graphics.Color



// Main activity that displays quotes and news articles with interactive UI
class MainActivity : AppCompatActivity() {

    // List to hold fetched news articles
    private val articleList = ArrayList<NewsArticle>()

    // Pagination and loading state variables
    private var currentPage = 1
    private var isLoading = false
    private var currentQuery: String? = null
    private var selectedCategory: String = "general"

    // Quote-related UI components and state variables
    private lateinit var quoteManager: QuoteManager
    private lateinit var quoteText: TextView
    private lateinit var btnSave: ImageView
    private lateinit var btnNext: ImageView
    private lateinit var btnPrev: ImageView
    private lateinit var btnShare: ImageView
    private var currentQuote: String = ""
    private var currentAuthor: String = ""
    private var quotePage = 1
    private var isQuoteLiked: Boolean = false

    // News-related UI components
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchInput: EditText
    private lateinit var categorySpinner: Spinner

    // Called when activity is created; sets layout and initializes components
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        categorySpinner = findViewById(R.id.categorySpinner)
        searchInput = findViewById(R.id.searchInput)

        enableEdgeToEdge()

        // Adjust padding to handle system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()
        setupQuoteSection()
        setupNewsSection()
        setupCategorySpinner()

        fetchNews(reset = true)
    }

    // Initialize references to UI
    private fun initializeViews() {
        quoteText = findViewById(R.id.centerText)
        btnSave = findViewById(R.id.btnSave)
        btnNext = findViewById(R.id.btnNext)
        btnPrev = findViewById(R.id.btnPrev)
        btnShare = findViewById(R.id.btnShare)
        recyclerView = findViewById(R.id.newsRecyclerView)
    }

    // Configure the quote display and navigation buttons
    private fun setupQuoteSection() {
        quoteManager = QuoteManager(this)

        // Load and display the quote of the day
        quoteManager.getQuoteOfTheDay { quote, author ->
            runOnUiThread {
                currentQuote = quote
                currentAuthor = author
                quoteText.text = "\"$quote\"\n\n— $author"
                isQuoteLiked = quoteManager.getFavorites().contains(currentQuote)
                updateSaveButtonTint()
            }
        }

        // Navigate to previous quote page
        btnPrev.setOnClickListener {
            if (quotePage > 1) {
                quotePage--
                quoteManager.getQuoteByPage(quotePage) { quote, author ->
                    runOnUiThread {
                        currentQuote = quote
                        currentAuthor = author
                        quoteText.text = "\"$quote\"\n\n— $author"
                        isQuoteLiked = quoteManager.getFavorites().contains(currentQuote)
                        updateSaveButtonTint()
                    }
                }
            } else {
                Toast.makeText(this, "You are on the first page of quotes.", Toast.LENGTH_SHORT).show()
            }
        }

        // Navigate to next quote page
        btnNext.setOnClickListener {
            quotePage++
            quoteManager.getQuoteByPage(quotePage) { quote, author ->
                runOnUiThread {
                    currentQuote = quote
                    currentAuthor = author
                    quoteText.text = "\"$quote\"\n\n— $author"
                    isQuoteLiked = quoteManager.getFavorites().contains(currentQuote)
                    updateSaveButtonTint()
                }
            }
        }

        // Save btn
        btnSave.setOnClickListener {
            isQuoteLiked = !isQuoteLiked
            updateSaveButtonTint()

            if (isQuoteLiked) {
                quoteManager.saveFavorite(currentQuote)
                Toast.makeText(this, "Quote saved!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Quote unliked (not removed from favorites for now)", Toast.LENGTH_SHORT).show()
            }
        }

        // Share the current quote
        btnShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Quote from DailyStudent App")
                putExtra(Intent.EXTRA_TEXT, "\"$currentQuote\"\n— $currentAuthor")
            }
            startActivity(Intent.createChooser(shareIntent, "Share Quote via"))
        }
    }

    // Update the save button
    private fun updateSaveButtonTint() {
        if (isQuoteLiked) {
            btnSave.setColorFilter(Color.WHITE)
        } else {
            btnSave.setColorFilter(ContextCompat.getColor(this, R.color.off_white))
        }
    }

    // RecyclerView and related news fetching logic
    private fun setupNewsSection() {
        newsAdapter = NewsAdapter(articleList) { article ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
            startActivity(intent)  // Open article URL in browser on click
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = newsAdapter

        setupSearchInput()
        setupScrollListener()
    }

    // Category spinner dropdown and its selection listener initialization
    private fun setupCategorySpinner() {
        val categories = listOf("All", "Business", "Entertainment", "General", "Health", "Science", "Sports", "Technology")
        val spinnerAdapter = ArrayAdapter(this, R.layout.custom_spinner_dropdown_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = spinnerAdapter
        categorySpinner.post {
            categorySpinner.setSelection(0)
        }

        // Update category and fetch news when user selects a category
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedCategory = categories[position].lowercase()
                currentQuery = null
                searchInput.text.clear()
                fetchNews(reset = true)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    // Setup the search input
    private fun setupSearchInput() {
        searchInput.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                val query = searchInput.text.toString().trim()
                if (query.isNotEmpty()) {
                    currentQuery = query
                    categorySpinner.setSelection(0)
                    fetchNews(reset = true)
                }
                true
            } else {
                false
            }
        }
    }

    // Add scroll listener to RecyclerView
    private fun setupScrollListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                val lm = rv.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = lm.findLastVisibleItemPosition()
                if (!isLoading && articleList.isNotEmpty() && lastVisibleItemPosition >= articleList.size - 2) {
                    fetchNews(reset = false)
                }
            }
        })
    }

    // Fetch news articles
    @SuppressLint("NotifyDataSetChanged")
    private fun fetchNews(reset: Boolean) {
        if (isLoading) return
        isLoading = true

        if (reset) {
            currentPage = 1
            articleList.clear()
            newsAdapter.notifyDataSetChanged()
        }

        NewsAPIManager.fetchNews(
            title = currentQuery,
            category = selectedCategory,
            page = currentPage
        ) { result ->
            isLoading = false
            runOnUiThread {
                result.onSuccess { newList ->
                    if (newList.isEmpty() && !reset) {
                        Toast.makeText(this, "No more articles", Toast.LENGTH_SHORT).show()
                    } else {
                        articleList.addAll(newList)
                        newsAdapter.notifyDataSetChanged()
                        currentPage++
                    }
                }.onFailure { error ->
                    Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
