package com.amit.bsbec.filters

import com.intercax.syndeia.filters.LoggingFilter
import play.api.mvc.EssentialFilter

trait CustomHttpFilterComponents extends play.filters.HttpFiltersComponents {
  val allowedIpFilter: AllowedIpFilter
  override def httpFilters: Seq[EssentialFilter] = {
    super.httpFilters :+ allowedIpFilter
  }
}
