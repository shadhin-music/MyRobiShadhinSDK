package com.shadhinmusiclibrary.utils

import android.content.SearchRecentSuggestionsProvider

internal class MySuggestionProvider : SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        const val AUTHORITY = "com.shadhinmusiclibrary.utils.MySuggestionProvider"
        const val MODE: Int = SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES
    }
}