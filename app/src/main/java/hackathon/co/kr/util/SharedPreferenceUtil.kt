package hackathon.co.kr.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

private lateinit var sharedPreferences: SharedPreferences
private lateinit var editor: SharedPreferences.Editor

const val token = "TOKEN"

fun sharedPreferenceInit(context: Context) {
    sharedPreferences = context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
    editor = sharedPreferences.edit()
    editor.apply()
}

// 일반 스트링 getter
fun getSpStringData(key: String): String {
    return sharedPreferences.getString(key, "") ?: ""
}

// 일반 Int getter
fun getSpIntData(key: String): Int {
    return sharedPreferences.getInt(key, -1)
}

// 일반 Long getter
fun getSpLongData(key: String): Long {
    return sharedPreferences.getLong(key, -1L)
}

// 일반 Boolean getter
fun getSpBooleanData(key: String): Boolean {
    return sharedPreferences.getBoolean(key, false)
}

// 일반 스트링 setter
fun setSpData(key: String, value: String) {
    editor.putString(key, value)
    editor.apply()
}

// 일반 Int setter
fun setSpData(key: String, value: Int) {
    editor.putInt(key, value)
    editor.apply()
}

// 일반 Long setter
fun setSpData(key: String, value: Long) {
    editor.putLong(key, value)
    editor.apply()
}

// 일반 Boolean setter
fun setSpData(key: String, value: Boolean) {
    editor.putBoolean(key, value)
    editor.apply()
}


