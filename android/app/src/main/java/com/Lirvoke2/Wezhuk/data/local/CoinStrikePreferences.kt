package com.Lirvoke2.Wezhuk.data.local

import android.content.Context
import android.content.SharedPreferences

class CoinStrikePreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE)

    fun readInt(key: String, fallback: Int): Int = prefs.getInt(key, fallback)

    fun readLong(key: String, fallback: Long): Long = prefs.getLong(key, fallback)

    fun readBoolean(key: String, fallback: Boolean): Boolean = prefs.getBoolean(key, fallback)

    fun readString(key: String, fallback: String): String = prefs.getString(key, fallback) ?: fallback

    fun readSet(key: String, fallback: Set<String>): Set<String> =
        prefs.getStringSet(key, null)?.toSet() ?: fallback

    fun writeInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun writeLong(key: String, value: Long) {
        prefs.edit().putLong(key, value).apply()
    }

    fun writeBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun writeString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun writeSet(key: String, value: Set<String>) {
        prefs.edit().putStringSet(key, value).apply()
    }

    fun clear(keys: List<String>) {
        val editor = prefs.edit()
        keys.forEach { editor.remove(it) }
        editor.apply()
    }

    companion object {
        private const val STORE_NAME = "coin_strike_store"
    }
}
