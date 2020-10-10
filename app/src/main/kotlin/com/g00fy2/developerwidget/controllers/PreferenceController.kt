package com.g00fy2.developerwidget.controllers

interface PreferenceController {


  companion object {
    const val SEARCH_DEPTH = "search_depth"
  }

  suspend fun get(key: String, default: String): String

  suspend fun set(key: String, data: String)

  suspend fun get(key: String, default: Long): Long

  suspend fun set(key: String, data: Long)

  suspend fun get(key: String, default: Int): Int

  suspend fun set(key: String, data: Int)

  suspend fun get(key: String, default: Boolean): Boolean

  suspend fun set(key: String, data: Boolean)
}