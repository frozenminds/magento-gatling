package com.frozenminds.magento_gatling

import io.gatling.core.Predef._
import io.gatling.http.check.HttpCheck
import io.gatling.http.Predef._
import scala.concurrent.duration._

/** Checkout object */
object Checkout {

  // Action checkout
  val actionCheckout =
    randomSwitch(
      Configuration.configPercentCheckoutSkip -> pause(1 * Configuration.configRealtimeRatio seconds, 5 * Configuration.configRealtimeRatio seconds),
      Configuration.configPercentCheckoutComplete ->
        exec(
          http("Goto Checkout")
            .get("/checkout/onepage")
            .headers(Headers.headersGet)
            .check(Cart.checksCart: _*)
            .check(status.is(200)))
        .exitHereIfFailed
        .pause(1 * Configuration.configRealtimeRatio seconds, 5 * Configuration.configRealtimeRatio seconds))
      //.exec(flushSessionCookies)
      //.exec(flushCookieJar)
}
