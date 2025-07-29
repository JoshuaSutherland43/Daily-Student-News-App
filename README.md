# DailyStudent

DailyStudent is an Android app that delivers daily inspirational quotes and up-to-date news articles. Users can browse quotes, save their favorites, share them, and read news by category or search for specific topics.

---

## Features

### Quotes
- Fetches **Quote of the Day** from FavQs API.
- Browse quotes page by page.
- Save favorite quotes locally.
- Share quotes via other apps.
- Like/unlike quotes with UI feedback.

---
### News
- Fetches news articles from NewsAPI.org.
- Browse news by category or search by keyword.
- Pagination support with infinite scroll.
- View full articles in the browser.
- Displays article title, description, and images with graceful loading and placeholders.

---

## Screenshots

*(Add your app screenshots here for quotes and news views)*

---

## Getting Started

### Prerequisites

- Android Studio (Arctic Fox or later recommended)
- Android SDK (API Level 30+)
- Gradle 7.0+

---
### API Keys

You need to obtain API keys for:

- **FavQs API**: [https://favqs.com/api](https://favqs.com/api)
- **NewsAPI**: [https://newsapi.org](https://newsapi.org)

---
## Build & Run
1. Clone the repo:

```bash
git clone https://github.com/yourusername/dailystudent.git
cd dailystudent
```

2. Open the project in Android Studio.

3. Sync Gradle and ensure API keys are set in local.properties.

4. Build and run the app on an emulator or physical device with Android 11 (API 30) or higher.

---
## Code Overview
- QuoteManager.kt — Handles fetching, saving, and retrieving quotes using OkHttp and SharedPreferences.

- NewsAPIManager.kt — Fetches news articles asynchronously from NewsAPI.

- NewsAdapter.kt — RecyclerView adapter to display news articles with Glide image loading.

- MainActivity.kt — Core UI and interaction logic including quote navigation, news fetching, search, and category filtering.

---
## Dependencies
- OkHttp - For network calls.

- Glide - For image loading and caching.

- AndroidX libraries including RecyclerView, AppCompat, and Material Components.

---

## License
- This project is licensed under the MIT License - see the LICENSE file for details.

---
## Acknowledgments

### FavQs - API for quotes.

### NewsAPI - for news data.

### Open-source libraries: OkHttp, Glide, AndroidX.

---
