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

<img width="400" height="800" alt="DailyStudent" src="https://github.com/user-attachments/assets/86e98a18-bff6-4e79-8283-3ee066331976" />


---
## Technical Challenges Faced

### 1. API Integration & Authentication
- **Challenge:** Both FavQs and NewsAPI required API key authentication and rate-limiting.
- **Solution:** Implemented secure key management using `local.properties` and integrated OkHttp to handle headers and queries dynamically.

### 2. Caching & Offline Access for Quotes
- **Challenge:** Users needed access to favorite quotes even when offline.
- **Solution:** Used `SharedPreferences` to store favorite quotes locally as JSON, allowing persistence between sessions.

### 3. Efficient Image Loading in News
- **Challenge:** News articles often contain large or broken image URLs.
- **Solution:** Used Glide with `placeholder` and `error` fallbacks to avoid crashes and improve UX.

### 4. Pagination & Dynamic Updates
- **Challenge:** Fetching more quotes or news without duplicating data or freezing the UI.
- **Solution:** Implemented pagination and `RecyclerView` with `notifyDataSetChanged()` to update views smoothly on new data fetch.

### 5. Modular Code Design
- **Challenge:** Keeping networking, UI, and logic layers separated.
- **Solution:** Created dedicated manager classes (`QuoteManager`, `NewsAPIManager`) to handle external logic and APIs separately from UI code.

### 6. Handling Multiple View States
- **Challenge:** Supporting loading, empty, and error states in the UI.
- **Solution:** Added progress indicators, fallback images, and retry UI messages to guide users during errors or empty API responses.

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
