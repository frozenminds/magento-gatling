package com.frozenminds.magento_gatling

import io.gatling.core.Predef._
import io.gatling.http.check.HttpCheck
import io.gatling.http.Predef._

/** Add to cart checks */
object AddToCart {

  val checksAddToCart: Seq[HttpCheck] = Seq(
    // Add to cart form action
    css("form[id^=product_addtocart_form]", "action").find.optional.saveAs("formAction"),
    // form key aKa CSRF token
    css("input[name=form_key]", "value").find.optional.saveAs("formKey"),
    // product ID
    css("input[name=product]", "value").find.optional.saveAs("product"),
    // Related products
    //css("input[name=related_product]", "value").find.optional.saveAs("relatedProduct"),
    // Bundled options form element name
    css("[name^=bundle_option\\[]", "name").findAll.optional.saveAs("bundleOptionSelectName"),
    // Bundled options form element values
    css("[name^=bundle_option\\[]", "value").findAll.optional.saveAs("bundleOptionSelectValue"))
}
