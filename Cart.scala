package com.frozenminds.magento_gatling

import io.gatling.core.Predef._
import io.gatling.http.check.HttpCheck
import io.gatling.http.Predef._
import scala.concurrent.duration._

/** Cart object */
object Cart {

  // Cart checks
  val checksCart: Seq[HttpCheck] = Seq(
    // Cart in header
    //css(".header-minicart .count").find.optional.saveAs("cartCount"),

    // Shopping cart table
    css("table#shopping-cart-table").find.optional.saveAs("cartTable"))

  // Action browse cart
  val actionCartBrowse = exec(
    http("Goto Cart")
      .get("/checkout/cart")
      .headers(Headers.headersGet)
      .check(checksCart: _*))
    .exitHereIfFailed
    .pause(2 * Configuration.configRealtimeRatio seconds, 10 * Configuration.configRealtimeRatio seconds)

  //.exec(flushSessionCookies)
  //.exec(flushCookieJar)
}
