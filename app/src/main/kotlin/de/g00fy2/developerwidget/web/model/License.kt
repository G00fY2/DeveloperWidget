package de.g00fy2.developerwidget.web.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class License {

  var key: String? = null
  var name: String? = null
  @Json(name = "spdx_id")
  var spdxId: String? = null
  var url: String? = null
  @Json(name = "node_id")
  var nodeId: String? = null
}
