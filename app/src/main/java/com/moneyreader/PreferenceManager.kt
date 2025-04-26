package com.moneyreader

import android.content.Context
import android.content.SharedPreferences

object PreferenceManager {

    private const val PREFS_NAME = "MoneyReaderPrefs"
    private const val KEY_KEYWORDS = "keywords"
    private const val KEY_REGEX = "regex_patterns"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getKeywords(context: Context): MutableSet<String> {
        return getPrefs(context).getStringSet(KEY_KEYWORDS, mutableSetOf()) ?: mutableSetOf()
    }

    fun saveKeywords(context: Context, keywords: Set<String>) {
        getPrefs(context).edit().putStringSet(KEY_KEYWORDS, keywords).apply()
    }

    fun getRegexPatterns(context: Context): MutableSet<String> {
        return getPrefs(context).getStringSet(KEY_REGEX, mutableSetOf()) ?: mutableSetOf()
    }

    fun saveRegexPatterns(context: Context, regex: Set<String>) {
        getPrefs(context).edit().putStringSet(KEY_REGEX, regex).apply()
    }
}