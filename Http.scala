package com.frozenminds.magento_gatling

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.gatling.http.HeaderNames._

/** HTTP object and configuration */
object Http {

  // HTTP configuration
  val httpConf = http
    .baseURL(Configuration.configBaseUrl)
    .acceptCharsetHeader("ISO-8859-1,utf-8;q=0.7,*;q=0.7")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate, sdch")
    //.disableCaching
    //.inferHtmlResources
}
