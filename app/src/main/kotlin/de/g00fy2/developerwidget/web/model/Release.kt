package de.g00fy2.developerwidget.web.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Release {
  var url: String? = null
  @Json(name = "html_url")
  var htmlUrl: String? = null
  @Json(name = "assets_url")
  var assetsUrl: String? = null
  @Json(name = "upload_url")
  var uploadUrl: String? = null
  @Json(name = "tarball_url")
  var tarballUrl: String? = null
  @Json(name = "zipball_url")
  var zipballUrl: String? = null
  var id: Int? = null
  @Json(name = "node_id")
  var nodeId: String? = null
  @Json(name = "tag_name")
  var tagName: String? = null
  @Json(name = "target_commitish")
  var targetCommitish: String? = null
  var name: String? = null
  var body: String? = null
  var draft: Boolean? = null
  var prerelease: Boolean? = null
  @Json(name = "created_at")
  var createdAt: String? = null
  @Json(name = "published_at")
  var publishedAt: String? = null
  var author: Author? = null
  var assets: List<Asset>? = null
}
