package com.moneyreader

import android.content.Context
import android.content.SharedPreferences

object PreferenceManager {

    private const val PREFS_NAME = "MoneyReaderPrefs"
    private const val KEY_KEYWORDS = "keywords"
    private const val KEY_REGEX = "regex_patterns"
private const val KEY_VOICE_TYPE = "voice_type" // "male" or "female"
private const val KEY_SPEECH_RATE = "speech_rate" // Float

fun getVoiceType(context: Context): String {
    return getPrefs(context).getString(KEY_VOICE_TYPE, "male") ?: "male"
}

fun saveVoiceType(context: Context, type: String) {
    getPrefs(context).edit().putString(KEY_VOICE_TYPE, type).apply()
}

fun getSpeechRate(context: Context): Float {
    return getPrefs(context).getFloat(KEY_SPEECH_RATE, 1.0f)
}

fun saveSpeechRate(context: Context, rate: Float) {
    getPrefs(context).edit().putFloat(KEY_SPEECH_RATE, rate).apply()
}
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