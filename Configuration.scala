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

  // simulates that `percentcartaddskip` will not add to cart,
  // only `percentcartadd` will really add the product to cart.
  // IMPORTANT: percentcartadd + percentcartaddskip <= 100%
  val configPercentCartAdd = System.getProperty("percentcartadd", "30d").toDouble
  val configPercentCartSkip = System.getProperty("percentcartaddskip", "70d").toDouble
}
