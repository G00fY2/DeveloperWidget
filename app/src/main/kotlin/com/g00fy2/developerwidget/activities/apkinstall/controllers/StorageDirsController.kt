package com.g00fy2.developerwidget.activities.apkinstall.controllers

import java.io.File

interface StorageDirsController {

  fun getStorageDirectories(): Collection<File>
}