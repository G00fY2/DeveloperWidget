package de.g00fy2.developerwidget.web.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Author {
  var login: String? = null
  var id: Int? = null
  @Json(name = "node_id")
  var nodeId: String? = null
  @Json(name = "avatar_url")
  var avatarUrl: String? = null
  @Json(name = "gravatar_id")
  var gravatarId: String? = null
  var url: String? = null
  @Json(name = "html_url")
  var htmlUrl: String? = null
  @Json(name = "followers_url")
  var followersUrl: String? = null
  @Json(name = "following_url")
  var followingUrl: String? = null
  @Json(name = "gists_url")
  var gistsUrl: String? = null
  @Json(name = "starred_url")
  var starredUrl: String? = null
  @Json(name = "subscriptions_url")
  var subscriptionsUrl: String? = null
  @Json(name = "organizations_url")
  var organizationsUrl: String? = null
  @Json(name = "repos_url")
  var reposUrl: String? = null
  @Json(name = "events_url")
  var eventsUrl: String? = null
  @Json(name = "received_events_url")
  var receivedEventsUrl: String? = null
  var type: String? = null
  @Json(name = "site_admin")
  var siteAdmin: Boolean? = null
}