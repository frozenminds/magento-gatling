package com.frozenminds.magento_gatling

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.gatling.http.HeaderNames._
import scala.concurrent.duration._

import com.frozenminds.magento_gatling._

class MagentoSimulation extends Simulation {

  val feeder = sitemap("sitemap.xml").random

  val scn = scenario("Magento Simulation")
    .exec(Browse.actionBrowse) // browse
    .exec(CartAdd.actionCartAdd) // add to cart
    .exec(Cart.actionCartBrowse) // goto cart page

  setUp(scn.inject(nothingFor(1 seconds),
    atOnceUsers(Configuration.configAtOnceUsers),
    rampUsers(Configuration.configRampUsers) over (Configuration.configRampSeconds seconds))).protocols(Http.httpConf)
}
