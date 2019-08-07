package com.amit.exercise.util

import java.time.{LocalDateTime, ZoneId, ZoneOffset, ZonedDateTime}
import java.util.Date

object LocalDateTimeConverters {

  implicit class DateOps(date: Date) {

    def toLocalDateTime: LocalDateTime = {
      // java.sql.Timestamp(dateToConvert.getTime()).toLocalDateTime()
//      date.toInstant.atZone(ZoneId.systemDefault()).toLocalDateTime
      date.toInstant.atZone(ZoneId.of(ZoneOffset.UTC.getId)).toLocalDateTime
    }

    def toZonedDateTime: ZonedDateTime = {
      /**
        * NOTE: since in Cassandra, the time zone information goes as +0000,
        * we must use UTC to convert the date back to a ZonedDateTime.
        */
      ZonedDateTime.ofInstant(date.toInstant, ZoneId.of(ZoneOffset.UTC.getId))
    }
  }

  implicit class LocalDateTimeOps(localDateTime: LocalDateTime) {

    def toDate: Date = {
      // java.sql.Timestamp.valueOf(localDateTime)
//      Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant)
      Date.from(localDateTime.atZone(ZoneId.of(ZoneOffset.UTC.getId)).toInstant)
    }
  }

  implicit class ZonedDateTimeOps(zonedDateTime: ZonedDateTime) {

    def toDate: Date = {
      /**
        * NOTE: a java.util.date does not have time zone information, hence
        * in Cassandra, the time zone information goes as +0000 which
        * corresponds to UTC time zone.
        */
      Date.from(zonedDateTime.toInstant)
    }
  }
}
