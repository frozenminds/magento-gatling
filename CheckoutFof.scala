package com.frozenminds.magento_gatling

import io.gatling.core.Predef._
import io.gatling.http.check.HttpCheck
import io.gatling.http.Predef._
import scala.concurrent.duration._

/** Checkout Fast Order Form object */
object CheckoutFof {

  // Checkout page checks.
  val checksCheckoutPage: Seq[HttpCheck] = Seq(
    // Cart in header
    css("form#sales-fastorder-form", "action").find.saveAs("formFofAction"))

  // Fast-Order-Form checks.
  val checksFof: Seq[HttpCheck] = Seq(
    // No error messages
    css(".error-msg").notExists,
    // Confirmation section
    css(".confirmation-section").exists)

  // Action checkout Fast-Order-Form.
  val actionCheckoutFof =

    exec(
      http("Browse Checkout")
        .get("/checkout/onepage")
        .headers(Headers.headersGet)
        .check(Cart.checksCart: _*)
        .check(checksCheckoutPage: _*)
        .check(status.not(302))
        .check(status.is(200)))
      .exitHereIfFailed
      .exec(
        http("Submit Fast-Order-Form")
          .post("${formFofAction}")
          .headers(Headers.headersPost)
          .formParam("fastorder[telephone]", "0123456789")
          .formParam("alternative-switch", "phone")
          .formParam("fastorder[contact_alternative]", "9876543210")
          .check(checksFof: _*)
          .check(currentLocationRegex("sales/fastorder/success/")))
  //.exec(flushSessionCookies)
  //.exec(flushCookieJar)
}
