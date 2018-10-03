package de.g00fy2.developerwidget.web.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Permissions {

  var admin: Boolean? = null
  var push: Boolean? = null
  var pull: Boolean? = null
}