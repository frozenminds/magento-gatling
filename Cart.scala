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
    css(".count").saveAs("cartCount"))

  // Cart page checks
  val checksCartPage: Seq[HttpCheck] = Seq(
    // Shopping cart table
    css("table#shopping-cart-table").find.optional.saveAs("cartTable"))

  // Action browse cart
  val actionCartBrowse =
    exec((session: Session) => {
      val cartCount = session("cartCount")
      println(cartCount)

      session

    })
  doIf(true) {
    exec(
      http("Browse Cart")
        .get("/checkout/cart")
        .headers(Headers.headersGet)
        .check(checksCart: _*)
        .check(checksCartPage: _*))
      .exitHereIfFailed
      .pause(1 * Configuration.configRealtimeRatio seconds, 5 * Configuration.configRealtimeRatio seconds)
  }

  //.exec(flushSessionCookies)
  //.exec(flushCookieJar)
}
