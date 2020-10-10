package com.g00fy2.developerwidget.controllers

interface PermissionController {

  fun hasPermissions(vararg permissions: String): Boolean

  fun requestPermissions(vararg permissions: String, onGranted: (() -> Unit) = {}, onDenied: () -> Unit = {})
}