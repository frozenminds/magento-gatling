package com.frozenminds.magento_gatling

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.gatling.http.HeaderNames._
import scala.concurrent.duration._

import com.frozenminds.magento_gatling._

class MagentoSimulation extends Simulation {

  // Scenario.
  val scn = scenario("Magento Simulation")
    // Browse website.
    .exec(Browse.actionBrowse)
    .pause(1 * Configuration.configRealtimeRatio seconds, 5 * Configuration.configRealtimeRatio seconds)

    // IF - add to cart form exists (on product or category page).
    .doIfOrElse("${formAction.exists()}") {
      randomSwitch(
        // Users skipping add to cart `percentcartaddskip`.
        Configuration.configPercentCartSkip -> exec(Browse.actionBrowse)
          .pause(1 * Configuration.configRealtimeRatio seconds, 5 * Configuration.configRealtimeRatio seconds),

        // Users adding to cart `percentcartadd`.
        Configuration.configPercentCartAdd ->

          // Repeat adding and browsing until enough items in cart.
          asLongAs((session => session("cartCount").as[String].toInt < Configuration.itemsInCart)) {
            exec(CartAdd.actionCartAdd)
              .exec(Browse.actionBrowse)
              .pause(1 * Configuration.configRealtimeRatio seconds, 5 * Configuration.configRealtimeRatio seconds)
          })
    } // ELSE - no add to cart form exists (CMS page?), continue browsing.
    {
      exec(Browse.actionBrowse)
        .pause(1 * Configuration.configRealtimeRatio seconds, 5 * Configuration.configRealtimeRatio seconds)
    }

    // IF - user has items in cart.
    .doIfOrElse(session => session("cartCount").as[String].toInt > 0) {
      randomSwitch(

        // Users ignoring going to checkout page `percentcheckoutskip`.
        Configuration.configPercentCheckoutSkip -> exec(Browse.actionBrowse)
          .pause(1 * Configuration.configRealtimeRatio seconds, 5 * Configuration.configRealtimeRatio seconds),

        // Simulates going to cart then through the checkout process `percentcheckoutcomplete`.
        Configuration.configPercentCheckoutComplete ->
          exec(Cart.actionCartBrowse)
          .pause(1 * Configuration.configRealtimeRatio seconds, 5 * Configuration.configRealtimeRatio seconds)
          .exec(Checkout.actionCheckout)
          .pause(1 * Configuration.configRealtimeRatio seconds, 5 * Configuration.configRealtimeRatio seconds))
    } // ELSE - no items in cart, continue browsing.
    {
      exec(Browse.actionBrowse)
        .pause(1 * Configuration.configRealtimeRatio seconds, 5 * Configuration.configRealtimeRatio seconds)
    }


  setUp(scn.inject(nothingFor(1 seconds),
    atOnceUsers(Configuration.configAtOnceUsers),
    rampUsers(Configuration.configRampUsers) over (Configuration.configRampSeconds seconds)))
    .protocols(Http.httpConf)
}
