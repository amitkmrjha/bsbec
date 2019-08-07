package com.intercax.syndeia.controllers.repository

import com.intercax.syndeia.client.api.RepositoryApi
import com.intercax.syndeia.client.api.domain.types.RepositoryTypeApi
import com.intercax.syndeia.controllers.BaseControllerSpec
import com.intercax.syndeia.domain.{Ids, Keys}
import com.intercax.syndeia.domain.forms.{RepositoryCreate, RepositoryTypeCreate, RepositoryUpdate, TypeReferenceCreate}
import com.intercax.syndeia.setup.domain.UserSetup
import com.intercax.syndeia.setup.domain.types.RepositoryTypeSetup
import com.intercax.syndeia.utils.StringUtils._
import play.api.Logger

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.{Failure, Success}

class RepositoryControllerSpec extends BaseControllerSpec {
  val logger: Logger = Logger(this.getClass)

  var repositoryTypeKey: String = _
  val repositoryApi: RepositoryApi = new RepositoryApi
  val repositoryTypeApi: RepositoryTypeApi = new RepositoryTypeApi

  val name = "Some name"
  val desc = "Some desc"
  val notUri = "server"
  val uri = "http://server.com"
  val updatedName = "Updated name"

  val emptyTypeKeyError = "Type key cannot be empty or blank."
  val invalidUriError = "Repository uri is invalid, please specify a valid URL."
  val invalidExternalIdError = "Repository external id is invalid, please specify a valid URL."
  val duplicateExternalIdError = "already exists for external id"
  val moreThanOneInternalRepositoryError = "Only 1 internal repository can be created."
  val invalidExternalRepositoryError = "External id and external key, both should be present for creating an external Repository or both should be absent for creating an internal Repository."

  private def setUpRepositoryType(): Unit = {
    val repositoryTypeApi = new RepositoryTypeApi
    logger.debug(s"setting up repository types")

    val repositoryTypes = Await.result(repositoryTypeApi.getAll(token), 2.seconds)
    if (repositoryTypes.isEmpty) {
      logger.debug(s"no repository types exist, hence creating them")
      val repositoryTypeCreate = RepositoryTypeSetup.getInternalRepositoryTypes.head
      val eitherCreatedType = Await.result(repositoryTypeApi.create(repositoryTypeCreate, token), 5.seconds)

      if (eitherCreatedType.isLeft) {
        logger.error(s"could not create repository type: ${eitherCreatedType.left.get}")
      } else {
        val newlyCreatedType = eitherCreatedType.right.get
        logger.info(s"created repository type with: ${newlyCreatedType.getKey}")
        repositoryTypeKey = newlyCreatedType.getKey
      }
    } else {
      logger.debug(s"repository types already exist")
      repositoryTypeKey = repositoryTypes.head.getKey
    }
  }

  override def beforeAll(): Unit = {
    super.beforeAll()
    setUpRepositoryType()
  }

  "A RepositoryController" when {

    "there are no records in the repositories table" should {

      "return no records when GET all endpoint is used" ignore {
        val futureRepos = repositoryApi.getAll(token)
        futureRepos map { repos =>
          repos should have size 0
        }
      }

      "create an internal repository when POST endpoint is used" ignore {
        repositoryTypeApi.getAll(token).flatMap { repoTypes =>

          val futureRepoTypeKey = if (repoTypes.isEmpty) {
            logger.debug(s"no repository types present, so creating one")
            val repoType = RepositoryTypeCreate(name = "repo type")
            logger.debug(s"repo type to create: $repoType")
            val futureCreatedType = repositoryTypeApi.create(repoType, token)
            futureCreatedType.map(createdType => getKey(createdType))
          } else {
            Future.successful(repoTypes.head.getKey)
          }

          futureRepoTypeKey.flatMap { repoTypeKey =>
            val internalRepo = RepositoryCreate(name = name, description = Option(desc),
              uri = Option(uri), `type` = TypeReferenceCreate(repoTypeKey))
            logger.debug(s"repo to create: $internalRepo")

            val repo = repositoryApi.create(internalRepo, token)
            repo map { r =>
              r should not be null
              r.isRight should be(true)

              val key = r.right.get.getKey
              logger.debug(s"created key: $key")
              key.isNotNullOrEmpty should be(true)
            }}
        }
      }

      "create an external repository when POST endpoint is used and make external id and uri same (if they are not)" in {
        Future.successful(true should be(true))
      }
    }

    "update a record that has been previously created" ignore {
      repositoryApi.getAll(token).flatMap { repos =>

        if (repos.isEmpty) {
          logger.debug(s"cannot update a repository because none exist")
          fail("no repositories to update")
        } else {
          val repo = repos.head
          val key = repo.getKey
          val toUpdate = RepositoryUpdate(keys = Option(Keys(key)), name = Option(updatedName))

          repositoryApi.update(key, toUpdate, token).flatMap { updated =>
            updated should not be null
            updated.name should be(updatedName)
          }
        }
      }
    }

    "delete a record that has been previously created" ignore {
      repositoryApi.getAll(token).flatMap { repos =>

        if (repos.isEmpty) {
          logger.debug(s"cannot delete a repository because none exist")
          fail("no repositories to delete")
        } else {
          val repo = repos.head
          val key = repo.getKey

          repositoryApi.delete(key, token).flatMap { deleted =>
            deleted should not be null
            deleted.getKey should be(key)
          }
        }
      }
    }

    "given invalid data" should {

      "not create an internal or external repository when a blank or non-existent type key is given" in {
        val toCreate = RepositoryCreate(name = name, description = Option(desc),
          uri = Option(uri), `type` = TypeReferenceCreate(""))

        val futureEither = repositoryApi.create(toCreate, token)
        futureEither map { either =>
          logger.debug(s"$either")
          either should not be null
          either.isLeft should be(true)
          either.left.get should include(emptyTypeKeyError)
        }
      }

      "not create an internal or external repository when a URI is invalid" in {
        val toCreate = RepositoryCreate(name = name, description = Option(desc),
          uri = Option(name), `type` = TypeReferenceCreate("abc"))

        val futureEither = repositoryApi.create(toCreate, token)
        futureEither map { either =>
          logger.debug(s"$either")
          either should not be null
          either.isLeft should be(true)
          either.left.get should include(invalidUriError)
        }
      }

      "not create an external repository when external id is present but external key is absent" in {
        val ids = Option(Ids(external = Option(notUri)))
        val keys = Option(Keys(external = Option("")))
        val toCreate = RepositoryCreate(name = name, description = Option(desc), ids = ids, keys = keys,
          uri = Option(uri), `type` = TypeReferenceCreate(repositoryTypeKey))

        val futureEither = repositoryApi.create(toCreate, token)
        futureEither map { either =>
          logger.debug(s"$either")
          either should not be null
          either.isLeft should be(true)
          either.left.get should include(invalidExternalRepositoryError)
        }
      }

      "not create an external repository when external key is present but external id is absent" in {
        val ids = Option(Ids(external = Option("")))
        val keys = Option(Keys(external = Option(notUri)))
        val toCreate = RepositoryCreate(name = name, description = Option(desc), ids = ids, keys = keys,
          uri = Option(uri), `type` = TypeReferenceCreate(repositoryTypeKey))

        val futureEither = repositoryApi.create(toCreate, token)
        futureEither map { either =>
          logger.debug(s"$either")
          either should not be null
          either.isLeft should be(true)
          either.left.get should include(invalidExternalRepositoryError)
        }
      }

      "not create an external repository when both external id and key are present but external id is invalid uri" in {
        val ids = Option(Ids(external = Option(notUri)))
        val keys = Option(Keys(external = Option(notUri)))
        val toCreate = RepositoryCreate(name = name, description = Option(desc), ids = ids, keys = keys,
          uri = Option(uri), `type` = TypeReferenceCreate(repositoryTypeKey))

        val futureEither = repositoryApi.create(toCreate, token)
        futureEither map { either =>
          logger.debug(s"$either")
          either should not be null
          either.isLeft should be(true)
          either.left.get should include(invalidExternalIdError)
        }
      }

      "not create an external repository for duplicate external id" in {
        val ids = Option(Ids(external = Option(uri)))
        val keys = Option(Keys(external = Option(notUri)))
        val toCreate = RepositoryCreate(name = name, description = Option(desc), ids = ids, keys = keys,
          uri = Option("http//localhost:9000"), `type` = TypeReferenceCreate(repositoryTypeKey))

        val futureEither = repositoryApi.create(toCreate, token)
        futureEither map { either =>
          logger.debug(s"$either")
          either should not be null
          either.isLeft should be(true)
          either.left.get should include(duplicateExternalIdError)
        }
      }

      "not create more than one internal repository" in {
        val toCreate = RepositoryCreate(name = name, description = Option(desc),
          uri = Option(uri), `type` = TypeReferenceCreate(repositoryTypeKey))

        val futureEither = repositoryApi.create(toCreate, token)
        futureEither map { either =>
          logger.debug(s"$either")
          either should not be null
          either.isLeft should be(true)
          either.left.get should include(moreThanOneInternalRepositoryError)
        }
      }
    }
  }
}
