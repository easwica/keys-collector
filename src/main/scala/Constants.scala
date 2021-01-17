package constants

import akka.NotUsed
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.Flow

import scala.collection.mutable.ListBuffer
import scala.util.Try
import scala.util.matching.Regex

object Constants {
  val SourceQueueBufferSize: Int = 256
  val BufferOverflowStrategy: OverflowStrategy = OverflowStrategy.dropHead

  val GitHubSearchCodeUrl = "search/code"
  val GitHubSortByLatestIndexed = "indexed"
  val GitHubAcceptHeaderValue = "application/vnd.github.v3+json"
  val GitHubFileMaxSize = 1000000 // 1MB

  val SubscriptionForAllKeys = "all"

  val SearchKeyWords: List[String] = List("secret") // List("token") //, "apikey", "accesskey", "secret")
    //List("key", "secret", "client")

  val SecretSearchRegex: Regex = """(?i)(["']?([tT]oken|[kK]ey|[Ss]ecret)[\w]*["']?\s?(=|:)\s?["']|([Tt]oken|[Kk]ey)[\w]*: \w+ = ["'])([\w-!$%^&*()_+|~=`{}\[\]:"'<>?.\/]{12,60})["']""".r
  val SearchVariableNameGroupIndex = 1
  val SearchSecretGroupIndex = 4
  val MinKeyLength = 10

  val KeyNoNoWordsRegex: Regex = """(?i)(name)|(app)|(api)|(key)|(token)|(secret)|(url)|(user)|(auth)|(access)|(credentials)|(client)|(config)|(port)|(json)|([*]{5,})|(password)|(time)|(com)""".r

  val ServiceDetectionRegex: Regex = """(?i)(aws)|(bitly)|(facebook)|(flickr)|(foursquare)|(linkein)|(twitter)""".r

  type HttpPool = Flow[(HttpRequest, NotUsed), (Try[HttpResponse], NotUsed), NotUsed]

  val ServiceClientIdSecretPatterns: Map[String, (String, String)] = Map(
    ("aws", ("AKIA[0-9A-Z]{16}", "[0-9a-zA-Z/+]{40}")),
    ("bitly", ("[0-9a-zA-Z_]{5,31}", "R_[0-9a-f]{32}")),
    ("facebook", ("[0-9]{13,17}", "[0-9a-f]{32}")),
    ("flickr", ("[0-9a-f]{32}", "[0-9a-f]{16}")),
    ("foursquare", ("[0-9A-Z]{48}", "[0-9A-Z]{48}")),
    ("linkein", ("[0-9a-z]{12}", "[0-9a-zA-Z]{16}")),
    ("twitter", ("[0-9a-zA-Z]{18,25}", "[0-9a-zA-Z]{35,44}")),
  )

  def formatSearchRegex(serviceName: String): List[(String, String, Regex)] = {
    if (!ServiceClientIdSecretPatterns.contains(serviceName)) {
      return List.empty
    }

    val servicePatterns = ServiceClientIdSecretPatterns(serviceName)
    val pattern: String = """["']({{secretPattern}})["']"""

    List(
      (serviceName, "id", pattern.replace("{{secretPattern}}", servicePatterns._1).r),
      (serviceName, "secret", pattern.replace("{{secretPattern}}", servicePatterns._2).r)
    )
  }
}