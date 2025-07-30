# 🎓 DailyStudent 📱

**DailyStudent** is a sleek and minimal Android app that delivers **daily inspirational quotes** ✨ and **up-to-date news articles** 📰. Users can browse quotes, save their favorites, share them with friends, and explore news by category or search for specific topics.

Student Name: *Joshua Sutherland* 
Student Number: *ST10255930*
---

## 🚀 Features

### 💬 Quotes
- 🌟 Fetches **Quote of the Day** from the FavQs API.
- 📖 Browse quotes page by page.
- 💾 Save favorite quotes locally.
- 🔗 Share quotes via other apps.
- ❤️ Like/unlike quotes with instant UI feedback.

---

### 🗞 News
- 🌐 Fetches current news articles from **NewsAPI.org**.
- 📚 Browse by category or 🕵️‍♂️ search using keywords.
- 🔄 Smooth pagination with infinite scrolling.
- 🌍 Open full articles in your browser.
- 🖼 Displays article title, summary, and images with graceful loading and fallbacks.

---

## 📸 Screenshots

<img width="400" height="800" alt="DailyStudent" src="https://github.com/user-attachments/assets/86e98a18-bff6-4e79-8283-3ee066331976" />

---

## ⚙️ Technical Challenges & Solutions

### 🔑 1. API Integration & Authentication
- **Challenge:** Secure API key management and rate-limiting.
- **Solution:** Used `local.properties` to store keys securely and `OkHttp` for dynamic header/query handling.

### 📦 2. Caching & Offline Access
- **Challenge:** Ensure quotes are accessible offline.
- **Solution:** Used `SharedPreferences` to store favorite quotes as JSON and persist data between sessions.

### 🖼 3. Efficient Image Loading
- **Challenge:** Handle large or missing image URLs in news articles.
- **Solution:** Integrated **Glide** with placeholders and error handling to ensure a smooth UX.

### 🔄 4. Pagination & Dynamic Updates
- **Challenge:** Prevent UI freezes and data duplication when loading more content.
- **Solution:** Used `RecyclerView` with proper pagination logic and `notifyDataSetChanged()` for seamless updates.

### 🧩 5. Modular Architecture
- **Challenge:** Keep business logic separate from UI.
- **Solution:** Created dedicated manager classes:
  - `QuoteManager` for quotes
  - `NewsAPIManager` for news

### 📶 6. Managing View States
- **Challenge:** Handle loading, error, and empty states gracefully.
- **Solution:** Added progress bars, fallback UIs, and retry prompts to guide user flow.

---

## 🛠 Getting Started

### ✅ Prerequisites

- Android Studio **Arctic Fox (or newer)**
- Android SDK (API Level **30+**)
- Gradle **7.0+**

---

### 🔐 API Keys Required

Get your free API keys from:
- **FavQs API** 👉 [https://favqs.com/api](https://favqs.com/api)
- **NewsAPI** 👉 [https://newsapi.org](https://newsapi.org)

Save them in `local.properties` as:
```properties
FAVQS_API_KEY=your_favqs_key_here
NEWS_API_KEY=your_newsapi_key_here
```

---

## 🧪 Build & Run

1. **Clone the repo:**
```bash
git clone https://github.com/yourusername/dailystudent.git
cd dailystudent
```

2. **Open** the project in Android Studio.

3. **Sync Gradle** and ensure your API keys are configured in `local.properties`.

4. **Run** the app on an emulator or physical device (Android 11 / API 30+ recommended).

---

## 🧠 Code Overview

| File | Responsibility |
|------|----------------|
| `QuoteManager.kt` | Fetches and stores quotes using `OkHttp` and `SharedPreferences`. |
| `NewsAPIManager.kt` | Manages async news fetching via `OkHttp`. |
| `NewsAdapter.kt` | RecyclerView adapter for displaying articles with **Glide** integration. |
| `MainActivity.kt` | Handles main UI interactions: quote nav, news search, and filters. |

---

## 📦 Dependencies

- 🔌 **OkHttp** – For HTTP requests.
- 🖼 **Glide** – For image loading and caching.
- 🧱 **AndroidX** – Includes RecyclerView, AppCompat, and Material Components.

---

## 📜 License

This project is licensed under the **MIT License** – see the [LICENSE](LICENSE) file for details.

---

## 🙌 Acknowledgments

- 💬 **[FavQs](https://favqs.com/api)** – Inspirational quote API.
- 🗞 **[NewsAPI.org](https://newsapi.org)** – Trusted news data provider.
- 🛠 Open-source tools: **OkHttp**, **Glide**, **AndroidX**.

---
