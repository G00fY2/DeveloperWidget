package de.g00fy2.developerwidget.web.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Repository {

  var id: Int? = null
  @Json(name = "node_id")
  var nodeId: String? = null
  var name: String? = null
  @Json(name = "full_name")
  var fullName: String? = null
  var owner: Owner? = null
  var _private: Boolean? = null
  @Json(name = "html_url")
  var htmlUrl: String? = null
  var description: String? = null
  var fork: Boolean? = null
  var url: String? = null
  @Json(name = "archive_url")
  var archiveUrl: String? = null
  @Json(name = "assignees_url")
  var assigneesUrl: String? = null
  @Json(name = "blobs_url")
  var blobsUrl: String? = null
  @Json(name = "branches_url")
  var branchesUrl: String? = null
  @Json(name = "collaborators_url")
  var collaboratorsUrl: String? = null
  @Json(name = "comments_url")
  var commentsUrl: String? = null
  @Json(name = "commits_url")
  var commitsUrl: String? = null
  @Json(name = "compare_url")
  var compareUrl: String? = null
  @Json(name = "contents_url")
  var contentsUrl: String? = null
  @Json(name = "contributors_url")
  var contributorsUrl: String? = null
  @Json(name = "deployments_url")
  var deploymentsUrl: String? = null
  @Json(name = "downloads_url")
  var downloadsUrl: String? = null
  @Json(name = "events_url")
  var eventsUrl: String? = null
  @Json(name = "forks_url")
  var forksUrl: String? = null
  @Json(name = "git_commits_url")
  var gitCommitsUrl: String? = null
  @Json(name = "git_refs_url")
  var gitRefsUrl: String? = null
  @Json(name = "git_tags_url")
  var gitTagsUrl: String? = null
  @Json(name = "git_url")
  var gitUrl: String? = null
  @Json(name = "issue_comment_url")
  var issueCommentUrl: String? = null
  @Json(name = "issue_events_url")
  var issueEventsUrl: String? = null
  @Json(name = "issues_url")
  var issuesUrl: String? = null
  @Json(name = "keys_url")
  var keysUrl: String? = null
  @Json(name = "labels_url")
  var labelsUrl: String? = null
  @Json(name = "languages_url")
  var languagesUrl: String? = null
  @Json(name = "merges_url")
  var mergesUrl: String? = null
  @Json(name = "milestones_url")
  var milestonesUrl: String? = null
  @Json(name = "notifications_url")
  var notificationsUrl: String? = null
  @Json(name = "pulls_url")
  var pullsUrl: String? = null
  @Json(name = "releases_url")
  var releasesUrl: String? = null
  @Json(name = "ssh_url")
  var sshUrl: String? = null
  @Json(name = "stargazers_url")
  var stargazersUrl: String? = null
  @Json(name = "statuses_url")
  var statusesUrl: String? = null
  @Json(name = "subscribers_url")
  var subscribersUrl: String? = null
  @Json(name = "subscription_url")
  var subscriptionUrl: String? = null
  @Json(name = "tags_url")
  var tagsUrl: String? = null
  @Json(name = "teams_url")
  var teamsUrl: String? = null
  @Json(name = "trees_url")
  var treesUrl: String? = null
  @Json(name = "clone_url")
  var cloneUrl: String? = null
  @Json(name = "mirror_url")
  var mirrorUrl: String? = null
  @Json(name = "hooks_url")
  var hooksUrl: String? = null
  @Json(name = "svn_url")
  var svnUrl: String? = null
  var homepage: String? = null
  var language: Any? = null
  @Json(name = "forks_count")
  var forksCount: Int? = null
  @Json(name = "stargazers_count")
  var stargazersCount: Int? = null
  @Json(name = "watchers_count")
  var watchersCount: Int? = null
  var size: Int? = null
  @Json(name = "default_branch")
  var defaultBranch: String? = null
  @Json(name = "open_issues_count")
  var openIssuesCount: Int? = null
  var topics: List<String>? = null
  @Json(name = "has_issues")
  var hasIssues: Boolean? = null
  @Json(name = "has_projects")
  var hasProjects: Boolean? = null
  @Json(name = "has_wiki")
  var hasWiki: Boolean? = null
  @Json(name = "has_pages")
  var hasPages: Boolean? = null
  @Json(name = "has_downloads")
  var hasDownloads: Boolean? = null
  var archived: Boolean? = null
  @Json(name = "pushed_at")
  var pushedAt: String? = null
  @Json(name = "created_at")
  var createdAt: String? = null
  @Json(name = "updated_at")
  var updatedAt: String? = null
  var permissions: Permissions? = null
  @Json(name = "allow_rebase_merge")
  var allowRebaseMerge: Boolean? = null
  @Json(name = "allow_squash_merge")
  var allowSquashMerge: Boolean? = null
  @Json(name = "allow_merge_commit")
  var allowMergeCommit: Boolean? = null
  @Json(name = "subscribers_count")
  var subscribersCount: Int? = null
  @Json(name = "network_count")
  var networkCount: Int? = null
  var license: License? = null
  @Json(name = "organization")
  var organization: Organization? = null
}