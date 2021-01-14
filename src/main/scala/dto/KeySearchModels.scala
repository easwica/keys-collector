package dto

import utils.Utils


class FileWithKeyData(item: GitHubApiSearchItem, file: GitHubApiFile) {
  val name: String = item.name
  val path: String = item.path
  val sha: String = item.sha
  val content: String = Utils.decodeFileBase64(file.content)
  val repo: GitHubApiRepoDetails = item.repository

  if (item.sha != file.sha) {
    println("Incorrect zipping")
  }
}

case class ExposedKeyData(fileName: String, filePath: String, sha: String, repo_full_name: String, repo_html_url: String)