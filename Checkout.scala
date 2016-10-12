package com.frozenminds.magento_gatling

import io.gatling.core.Predef._
import io.gatling.http.check.HttpCheck
import io.gatling.http.Predef._
import scala.concurrent.duration._

/** Checkout object */
object Checkout {

  // Action checkout.
  val actionCheckout =

        exec(
          http("Goto Checkout")
            .get("/checkout/onepage")
            .headers(Headers.headersGet)
            .check(Cart.checksCart: _*)
            .check(status.is(200)))
        .exitHereIfFailed
      //.exec(flushSessionCookies)
      //.exec(flushCookieJar)
}
