package com.myrobi.shadhinmusiclibrary.utils

import android.content.SearchRecentSuggestionsProvider

internal class MySuggestionProvider : SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        const val AUTHORITY = "com.myrobi.shadhinmusiclibrary.utils.MySuggestionProvider"
        const val MODE: Int = SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES
    }
}