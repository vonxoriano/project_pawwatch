package edu.cit.soriano.pawwatch.mobile.util

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("pawwatch_session", Context.MODE_PRIVATE)

    fun saveSession(token: String, role: String, fullName: String) {
        prefs.edit()
            .putString("token", token)
            .putString("role", role)
            .putString("fullName", fullName)
            .apply()
    }

    fun getToken(): String? = prefs.getString("token", null)
    fun getRole(): String? = prefs.getString("role", null)
    fun getFullName(): String? = prefs.getString("fullName", null)

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}