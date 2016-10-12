package com.frozenminds.magento_gatling

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.gatling.http.HeaderNames._
import scala.concurrent.duration._

/** HTTP headers object */
object Headers {

  // HTTP GET headers.
  val headersGet = Map(
    "Keep-Alive" -> "115")

  // HTTP POST headers.
  val headersPost = Map(
    "Keep-Alive" -> "115",
    "Content-Type" -> "application/x-www-form-urlencoded")

  // AJAX headers.
  val headersAjax = Map(
    "Accept" -> "application/json, text/javascript, */*; q=0.01",
    "Keep-Alive" -> "115",
    "X-Requested-With" -> "XMLHttpRequest",
    "X-Prototype-Version" -> "1.7",
    "Content-Type" -> "application/x-www-form-urlencoded")
}
