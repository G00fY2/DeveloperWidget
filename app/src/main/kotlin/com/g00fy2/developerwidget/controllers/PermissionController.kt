package com.g00fy2.developerwidget.controllers

interface PermissionController {

  fun hasPermission(permission: String): Boolean

  fun hasPermissions(permissions: Array<String>): Boolean

  fun requestPermission(permission: String)

  fun requestPermissions(permissions: Array<String>)
}