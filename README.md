<p align="center"><a href="#"><img src="https://raw.githubusercontent.com/G00fY2/DeveloperWidget/gh-pages/media/app_icon.png" alt="DeveloperWidget Icon" height="160px"></a></p>

# Developer Widget [![Build](https://github.com/G00fY2/DeveloperWidget/actions/workflows/build.yml/badge.svg)](https://github.com/G00fY2/DeveloperWidget/actions/workflows/build.yml) [![Release](https://img.shields.io/github/v/release/G00fY2/DeveloperWidget)](https://github.com/G00fY2/DeveloperWidget/releases) [![Pre-Release](https://img.shields.io/github/v/release/G00fY2/DeveloperWidget?include_prereleases&label=pre-release)](https://github.com/G00fY2/DeveloperWidget/releases) [![API](https://img.shields.io/badge/API-14%2B-blue.svg?style=flat)](https://developer.android.com/studio/releases/platforms#4.0)

**Lightweight Android app that offers a widget to show device data, manage installed apps and list locally stored APK files.**

## Screenshots 

[<img src="https://raw.githubusercontent.com/G00fY2/DeveloperWidget/gh-pages/media/store_screenshot_1.png" width=160>](https://raw.githubusercontent.com/G00fY2/DeveloperWidget/gh-pages/media/store_screenshot_1.png)
[<img src="https://raw.githubusercontent.com/G00fY2/DeveloperWidget/gh-pages/media/store_screenshot_2.png" width=160>](https://raw.githubusercontent.com/G00fY2/DeveloperWidget/gh-pages/media/store_screenshot_2.png)
[<img src="https://raw.githubusercontent.com/G00fY2/DeveloperWidget/gh-pages/media/store_screenshot_3.png" width=160>](https://raw.githubusercontent.com/G00fY2/DeveloperWidget/gh-pages/media/store_screenshot_3.png)
[<img src="https://raw.githubusercontent.com/G00fY2/DeveloperWidget/gh-pages/media/store_screenshot_4.png" width=160>](https://raw.githubusercontent.com/G00fY2/DeveloperWidget/gh-pages/media/store_screenshot_4.png)
[<img src="https://raw.githubusercontent.com/G00fY2/DeveloperWidget/gh-pages/media/store_screenshot_5.png" width=160>](https://raw.githubusercontent.com/G00fY2/DeveloperWidget/gh-pages/media/store_screenshot_5.png)

## Download
<a href='https://play.google.com/store/apps/details?id=com.g00fy2.developerwidget'><img alt='Get it on Google Play' width='215' src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png'/></a>

You also find the latest APK in the [GitHub releases](https://github.com/G00fY2/DeveloperWidget/releases).

## Description
The app was built from a developer for developers. You may know the hassle of having multiple physical devices running different software. This app will help you keep track of important device information and allows you to organize your apps and local APK files. You will never again struggle to find APK files using a file browser or search for the app settings menu on a custom manufacturer UI.

The main feature of the app is an 4x1 (horizontally resizable) homescreen widget that shows dynamically fetched device data and allows you to browse your installed apps and local APK files.

### Features
* Homescreen widget with customizable device name
* Overview of dynamically fetched device data
  * device model, system, cpu, memory, display, hardware features, software
  * Share/export the data
* Browse all installed (non system) apps and filter them by **package name**
  * Save multiple filters per widget 
  * Wildcard support (e.g. **`com.*xyz`**)
* Manage local APK files
  * Scan internal storage and SD-cards for APK files
  * Show file name, modification time, file size, app name, debuggable flag, versionname and versioncode
  * Directly install them from within the app
  * Delete files by long pressing
* Offers useful app shortcuts starting from API 25
  * Android developer settings shortcut
  * Language settings shortcut
  * Browse installed apps shortcut
  * Manage local APK files shortcut
* Material design with **Dark mode support** (can be toggled by the user)
* Allow hiding the launcher icon (< API 29)
* Written in Kotlin using Coroutines and Dagger
* No internet permission

## Release Notes

Check out the [Release Notes](https://github.com/G00fY2/DeveloperWidget/releases) to find out what changed
in each version of the Developer Widget app.

## Report issues or request features
 
 Issues and Feature Requests are welcome. You can report them [here](https://github.com/G00fY2/DeveloperWidget/issues).
 
 ## License
     The MIT License (MIT)

    Copyright (C) 2018 Thomas Wirth

    Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
    associated documentation files (the "Software"), to deal in the Software without restriction,
    including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
    and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
    subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all copies or substantial
    portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
    LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
    NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
    DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT
    OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
  
