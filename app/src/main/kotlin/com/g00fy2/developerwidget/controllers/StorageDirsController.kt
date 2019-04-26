package com.g00fy2.developerwidget.controllers

import java.io.File

interface StorageDirsController {

  fun getStorageDirectories(): Collection<File>
}