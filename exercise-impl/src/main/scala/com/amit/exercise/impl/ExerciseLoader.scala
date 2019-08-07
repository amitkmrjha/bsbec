package com.amit.exercise.impl

import com.amit.exercise.api.ExerciseService
import com.amit.exercise.impl.store.daos.{BankInfoDao, BlackListIpDao, CategoryDao}
import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import com.softwaremill.macwire._

class ExerciseLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new ExerciseApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new ExerciseApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[ExerciseService])
}

abstract class ExerciseApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer: LagomServer = serverFor[ExerciseService](wire[ExerciseServiceImpl])

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry: JsonSerializerRegistry = ExerciseSerializeRegistry

  lazy val bankNameInfoDao: BankInfoDao = wire[BankInfoDao]
  lazy val blackListIpDao: BlackListIpDao = wire[BlackListIpDao]
  lazy val fileCategoryDao: CategoryDao = wire[CategoryDao]

}
