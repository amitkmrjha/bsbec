package com.amit.exercise.impl.store

package object store {

  object ColumnFamilies {
    val BlackListIp: String = "black_list_ip"
    val Category: String = "category"
    val BankInfo: String = "bank_info"
  }

  object Columns {
    val Id: String = "id"

    val Ip: String = "ip"

    val Type: String = "type"
    val Title : String = "title"
    val KeyWords : String = "keywords"
    val CreationDate : String = "creation_date"

    val Name:String = "name"
    val Identifier:String = "identifier"
  }

}
