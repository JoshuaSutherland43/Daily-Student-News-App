package com.example.dailystudent.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dailystudent.R
import com.example.dailystudent.models.NewsArticle

// Adapter displays the list of news articles in a RecyclerView
class NewsAdapter(
    private var articles: List<NewsArticle>,
    private val onClick: (NewsArticle) -> Unit
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    // ViewHolder class
    inner class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val description: TextView = view.findViewById(R.id.description)
        val imageView: ImageView = view.findViewById(R.id.newsImage)

        // Click listener for item view
        init {
            view.setOnClickListener {
                onClick(articles[adapterPosition])
            }
        }
    }

    // Inflates the item layout and creates the ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_item, parent, false)
        return NewsViewHolder(view)
    }

    // Binds data from the article to views in the ViewHolder
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = articles[position]
        holder.title.text = article.title
        holder.description.text = article.description

        // Loads the article image using Glide with placeholders
        Glide.with(holder.itemView.context)
            .load(article.urlToImage)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_image)
            .into(holder.imageView)
    }

    // Returns the total number of articles
    override fun getItemCount(): Int = articles.size

    // Updates the adapter's data
    fun updateData(newArticles: List<NewsArticle>) {
        this.articles = newArticles
        notifyDataSetChanged()
    }

}
