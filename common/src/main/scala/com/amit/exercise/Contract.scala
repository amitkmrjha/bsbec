package com.amit.exercise

object Contract extends Enumeration {
  type Contract = Value
  val ELECTRICITY = Value("electricity")
  val DSL = Value("dsl")
  val APARTMENT_RENT = Value("appartment_rent")

  def processContract(`type`:String):String =  Contract.values.find(x => x.toString == `type`) match {
      case Some(contract) => s"Processed ${contract}"
      case None =>
        s"No valid contract type to process."
    }

}
