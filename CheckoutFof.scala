package com.frozenminds.magento_gatling

import io.gatling.core.Predef._
import io.gatling.http.check.HttpCheck
import io.gatling.http.Predef._
import scala.concurrent.duration._

/** Checkout Fast Order Form object */
object CheckoutFof {

  // Fast-Order-Form checks
  val checksFof: Seq[HttpCheck] = Seq(
    // Cart in header
    css("form#sales-fastorder-form", "action").find.saveAs("formFofAction"))

  // Action checkout
  val actionCheckoutFof =
    doIf(session => session("cartCount").as[Integer] > 0) {
      randomSwitch(
        Configuration.configPercentCheckoutSkip -> pause(1 * Configuration.configRealtimeRatio seconds, 5 * Configuration.configRealtimeRatio seconds),
        Configuration.configPercentCheckoutComplete ->
          exec(
            http("Browse Checkout")
              .get("/checkout/onepage")
              .headers(Headers.headersGet)
              .check(Cart.checksCart: _*)
              .check(checksFof: _*)
              .check(status.not(302))
              .check(status.is(200)))
          .exitHereIfFailed
          .pause(1 * Configuration.configRealtimeRatio seconds, 5 * Configuration.configRealtimeRatio seconds)
          .exec(
            http("Submit Fast-Order-Form")
              .post("${formFofAction}")
              .headers(Headers.headersPost)
              .formParam("fastorder[telephone]", "0123456789")
              .formParam("alternative-switch", "phone")
              .formParam("fastorder[contact_alternative]", "9876543210")
              .formParam("captcha[fastorder_form]", "ABCD")))
      //.exec(flushSessionCookies)
      //.exec(flushCookieJar)
    }
}
