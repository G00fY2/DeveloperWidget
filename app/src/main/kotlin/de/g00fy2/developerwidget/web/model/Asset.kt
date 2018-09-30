package de.g00fy2.developerwidget.web.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Asset {
  var url: String? = null
  @Json(name = "browser_download_url")
  var browserDownloadUrl: String? = null
  var id: Int? = null
  @Json(name = "node_id")
  var nodeId: String? = null
  var name: String? = null
  var label: String? = null
  var state: String? = null
  @Json(name = "content_type")
  var contentType: String? = null
  var size: Int? = null
  @Json(name = "download_count")
  var downloadCount: Int? = null
  @Json(name = "created_at")
  var createdAt: String? = null
  @Json(name = "updated_at")
  var updatedAt: String? = null
  var uploader: Uploader? = null
}