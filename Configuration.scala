package com.frozenminds.magento_gatling

/** Configuration object */
object Configuration {
  // Base URL
  val configBaseUrl = System.getProperty("baseurl", "http://localhost")

  // Real time ratio
  val configRealtimeRatio = System.getProperty("realtimeratio", "1.0").toFloat

  // At once users
  val configAtOnceUsers = Integer.getInteger("atonceusers", 5)

  // Ramp users...
  val configRampUsers = Integer.getInteger("rampusers", 25)
  // ...over period (seconds)
  val configRampSeconds = Integer.getInteger("rampseconds", 120)

  // simulates that addtocartskippercent will not add to cart,
  // only addtocartactionpercent will really add the product to cart.
  // IMPORTANT: addtocartactionpercent + addtocartskippercent <= 100%
  val configAddToCartActionPercent = System.getProperty("percentaddtocart", "30d").toDouble
  val configAddToCartSkipPercent = System.getProperty("percentaddtocartskip", "70d").toDouble
}
