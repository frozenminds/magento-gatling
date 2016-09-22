package com.frozenminds.magento_gatling

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.gatling.http.HeaderNames._
import scala.concurrent.duration._

/** Scenario */
object Scenario {

  // sitemap feeder
  val feeder = sitemap("sitemap.xml").random

  val scenario =
    feed(feeder)
      .exec(
        http("Browse Pages")
          .get("${loc}")
          .headers(Headers.headersGet)
          .check(AddToCart.checksAddToCart: _*))
      .doIf("${formAction.exists()}") {
        randomSwitch(
          Configuration.configAddToCartSkipPercent -> pause(2 * Configuration.configRealtimeRatio seconds, 5 * Configuration.configRealtimeRatio seconds),
          Configuration.configAddToCartActionPercent ->
            exec((session: Session) => {
              val formAction = session("formAction")
              val formKey = session("formKey")
              //val relatedProduct = session("relatedProduct")

              val bundleParams = collection.mutable.Map.empty[String, String]
              val configParams = collection.mutable.Map.empty[String, String]
              val optionParams = collection.mutable.Map.empty[String, String]

              if (session.contains("bundleOptionSelectName") && session.contains("bundleOptionSelectValue")) {

                val bundleOptionSelectNames = session("bundleOptionSelectName").as[Seq[String]]
                val bundleOptionSelectValues = session("bundleOptionSelectValue").as[Seq[String]]

                val pattern = """(?s)(?<=bundle_option\[)([\d]+?)(?=\]\[\])""".r

                // build bundle options and qty maps
                val bundleOption = collection.mutable.Map.empty[Int, List[Any]]
                val bundleOptionQty = collection.mutable.Map.empty[Int, Map[Any, Int]]

                val i = 0;
                for (i <- 0 until bundleOptionSelectNames.length) {

                  val option = pattern.findFirstIn(bundleOptionSelectNames(i)).get.toInt

                  val items = collection.mutable.ListBuffer[Any]()
                  if (bundleOption.contains(option)) {
                    bundleOption.get(option).toList.head.copyToBuffer(items)
                  }

                  items += bundleOptionSelectValues(i)

                  bundleOption += option -> items.toList

                  val qty = collection.mutable.Map.empty[Any, Int]
                  for (bundle <- items.toList) {
                    qty += (bundle -> 1) // might want to add random qty
                  }

                  bundleOptionQty += option -> qty.toMap
                }

                // build bundle_option params
                for ((item, option) <- bundleOption) {

                  var i = 0
                  for ((bundle) <- option) {
                    bundleParams += s"bundle_option[$item][$i]" -> bundle.toString

                    i += 1
                  }
                }

                // build bundle_option_qty params
                for ((item, option) <- bundleOptionQty) {
                  for ((bundle, qty) <- option) {
                    //bundleParams += s"bundle_option_qty[$item][$bundle]" -> qty.toString
                  }
                }

              }

              session
                .set("qty", 1) // you may use a random number
                .set("bundleParams", bundleParams.toMap)
                .set("configParams", configParams.toMap)
                .set("optionParams", optionParams.toMap)
            })
            .exec(
              http("Add To Cart")
                .post("/checkout/cart/add")
                .headers(Headers.headersPost)
                .formParam("form_key", "${formKey}")
                .formParam("product", "${product}")
                .formParam("qty", "${qty}")
                //.formParam("related_product", "${relatedProduct}")
                .formParamMap("${bundleParams}")
                .formParamMap("${configParams}")
                .formParamMap("${optionParams}"))
            .exitHereIfFailed
            .pause(2 * Configuration.configRealtimeRatio seconds, 10 * Configuration.configRealtimeRatio seconds)
            .exec(
              http("Goto Cart")
                .get("/checkout/cart")
                .headers(Headers.headersGet)
                .check(Cart.checksCart: _*))
            .exitHereIfFailed
            .pause(2 * Configuration.configRealtimeRatio seconds, 10 * Configuration.configRealtimeRatio seconds)
            .exec(flushSessionCookies)
            .exec(flushCookieJar))
      }
}
