package com.frozenminds.magento_gatling

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.HeaderNames._
import scala.concurrent.duration._

import CartAdd._

/** Browse object */
object Browse {

  // sitemap feeder
  val feeder = sitemap("sitemap.xml").random

  // Action browse website
  val actionBrowse =
    feed(feeder)
      .exec(
        http("Browse Pages")
          .get("${loc}")
          .headers(Headers.headersGet)
          .check(CartAdd.checksAddToCart: _*))
}
