package com.frozenminds.magento_gatling

import io.gatling.core.Predef._
import io.gatling.http.check.HttpCheck
import io.gatling.http.Predef._
import scala.concurrent.duration._

/** Add to cart checks */
object CartAdd {

  // Checks for preparing add to cart.
  val checksCardAddForm: Seq[HttpCheck] = Seq(
    // Add to cart form action
    css("form[id^=product_addtocart_form]", "action").find.optional.saveAs("formAction"),
    // Add to cart form method (GET/POST)
    css("form[id^=product_addtocart_form]", "method").find.optional.saveAs("formMethod"),
    // form key aKa CSRF token
    css("input[name=form_key]", "value").find.optional.saveAs("formKey"),
    // product ID
    css("input[name=product]", "value").find.optional.saveAs("product"),
    // Related products
    css("input[name=related_product]", "value").find.optional.saveAs("relatedProduct"),
    // Bundled options form element name
    css("[name^=bundle_option\\[]", "name").findAll.optional.saveAs("bundleOptionSelectName"),
    // Bundled options form element values
    css("[name^=bundle_option\\[]", "value").findAll.optional.saveAs("bundleOptionSelectValue"))

  // Action add to cart.
  val actionCartAdd =

    exec((session: Session) => {
      val formAction = session("formAction")
      val formKey = session("formKey")
      val relatedProduct = session("relatedProduct")

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
        .set("relatedProduct", "")
    })
      .exec(
        http("Add To Cart")
          .post("/checkout/cart/add")
          .headers(Headers.headersPost)
          .formParam("form_key", "${formKey}")
          .formParam("product", "${product}")
          .formParam("qty", "${qty}")
          .formParam("related_product", "${relatedProduct}")
          .formParamMap("${bundleParams}")
          .formParamMap("${configParams}")
          .formParamMap("${optionParams}"))
      .exitHereIfFailed
}
