package com.frozenminds.magento_gatling

import io.gatling.core.Predef._
import io.gatling.http.check.HttpCheck
import io.gatling.http.Predef._

/** Cart object */
object Cart {
  // Cart checks
  val checksCart: Seq[HttpCheck] = Seq(
    css("table#shopping-cart-table").find.saveAs("cartTable"))
}
