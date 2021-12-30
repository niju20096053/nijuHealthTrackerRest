package org.wit.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.wit.config.DBConfig
import org.wit.db.MealTracker
import org.wit.domain.YogaDTO
import org.wit.helpers.*
import kotlin.test.assertEquals

//retrieving some test data from Fixtures
private val yoga1 = yogas.get(0)
private val yoga2 = yogas.get(1)
private val yoga3 = yogas.get(2)
private val yoga4 = yogas.get(3)
class YogaDAOTest {


    companion object {
        //Make a connection to a local, in memory H2 database or Heroku postgres database.
        @BeforeAll
        @JvmStatic
        //-------TO BE USED WITH INTERNAL DB----internal fun setupInMemoryDatabaseConnection() {
        internal fun setupDatabaseConnection() {
            val db = DBConfig().getDbConnection()
            val origin = "https://health-tracker-20094053.herokuapp.com"
            /* TO BE USED WITH INTERNAL APP
            val app = ServerContainer.instance
            val origin = "http://localhost:" + app.port()
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")  */
        }
    }


    internal  fun emptyYogasTable(){
        val yogasDAO = YogaDAO()
        yogasDAO.emptyYogasTable()
    }

    @Nested
    inner class CreateYogaTracker {

        @Test
        fun `multiple yogas added to table can be retrieved successfully`() {
            transaction {
                //Arrange - create and populate tables with users and yogas
                val userDAO = populateUserTable()
                val yogaDAO = populateYogaTable()
                //Act & Assert
                assertEquals(4, yogaDAO.getAll().size)
                assertEquals(yoga1.toString(), yogaDAO.findByYogaId(yoga1.yogaId).toString())
                assertEquals(yoga2.toString(), yogaDAO.findByYogaId(yoga2.yogaId).toString())
                assertEquals(yoga3.toString(), yogaDAO.findByYogaId(yoga3.yogaId).toString())
                assertEquals(yoga4.toString(), yogaDAO.findByYogaId(yoga4.yogaId).toString())
            }
            emptyYogasTable()
        }
    }

    @Nested
    inner class ReadYogaTracker {

        @Test
        fun `getting all activites from a populated table returns all rows`() {
            transaction {
                //Arrange - create and populate tables with three users and three yogas
                val userDAO = populateUserTable()
                val yogaDAO = populateYogaTable()
                //Act & Assert
                assertEquals(4, yogaDAO.getAll().size)
            }
            emptyYogasTable()
        }

        @Test
        fun `get yoga by user id that has no yogas, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three yogas
                val userDAO = populateUserTable()
                val yogaDAO = populateYogaTable()
                //Act & Assert
                assertEquals(0, yogaDAO.findByUserId(5).size)
            }
            emptyYogasTable()
        }

        @Test
        fun `get yoga by user id that exists, results in a correct yoga(s) returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three yogas
                val userDAO = populateUserTable()
                val yogaDAO = populateYogaTable()
                //Act & Assert
                assertEquals(yoga1.toString(), yogaDAO.findByUserId(1).get(0).toString())
                assertEquals(yoga2.toString(), yogaDAO.findByUserId(2).get(0).toString())
                assertEquals(yoga3.toString(), yogaDAO.findByUserId(3).get(0).toString())
                assertEquals(yoga4.toString(), yogaDAO.findByUserId(4).get(0).toString())
            }
            emptyYogasTable()
        }

        @Test
        fun `get all yogas over empty table returns none`() {
            transaction {

                //Arrange - create and setup yogaDAO object
                SchemaUtils.create(MealTracker)
                val yogaDAO = YogaDAO()

                //Act & Assert
                assertEquals(0, yogaDAO.getAll().size)
            }
            emptyYogasTable()
        }

        @Test
        fun `get yoga by yoga id that has no records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three yogas
                val userDAO = populateUserTable()
                val yogaDAO = populateYogaTable()
                //Act & Assert
                assertEquals(null, yogaDAO.findByYogaId(5))
            }
            emptyYogasTable()
        }

        @Test
        fun `get yoga by yoga id that exists, results in a correct yoga returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three yogas
                val userDAO = populateUserTable()
                val yogaDAO = populateYogaTable()
                //Act & Assert
                assertEquals(yoga1.toString(), yogaDAO.findByYogaId(1).toString())
                assertEquals(yoga3.toString(), yogaDAO.findByYogaId(3).toString())
            }
            emptyYogasTable()
        }
    }

    @Nested
    inner class UpdateYogaTracker {

        @Test
        fun `updating existing yoga in table results in successful update`() {
            transaction {

                //Arrange - create and populate tables with three users and three yogas
                val userDAO = populateUserTable()
                val yogaDAO = populateYogaTable()

                //Act & Assert
                val yoga3updated = YogaDTO(yogaId =3, yogaName= "Yoga4", yogaStart = DateTime.now(), yogaEnd = DateTime.now(), yogaDurationInMinutes = 0 ,userId = 2)
                yogaDAO.updateByYogaId(yoga3updated.yogaId, yoga3updated)
                assertEquals(yoga3updated, yogaDAO.findByYogaId(3))
            }
            emptyYogasTable()
        }

        @Test
        fun `updating non-existant yoga in table results in no updates`() {
            transaction {

                //Arrange - create and populate tables with three users and three yogas
                val userDAO = populateUserTable()
                val yogaDAO = populateYogaTable()

                //Act & Assert
                val yoga5updated = YogaDTO(yogaId =5, yogaName = "Yoga2", yogaStart = DateTime.now(), yogaEnd = DateTime.now(), yogaDurationInMinutes = 0,  userId = 3)
                yogaDAO.updateByYogaId(5, yoga5updated)
                assertEquals(null, yogaDAO.findByYogaId(5))
                assertEquals(4, yogaDAO.getAll().size)
            }
            emptyYogasTable()
        }
    }

    @Nested
    inner class DeleteYogaTracker {

        @Test
        fun `deleting a non-existant yoga (by id) in table results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three yogas
                val userDAO = populateUserTable()
                val yogaDAO = populateYogaTable()

                //Act & Assert
                assertEquals(4, yogaDAO.getAll().size)
                yogaDAO.deleteByYogaId(5)
                assertEquals(4, yogaDAO.getAll().size)
            }
            emptyYogasTable()
        }

        @Test
        fun `deleting an existing yoga (by id) in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate tables with three users and three yogas
                val userDAO = populateUserTable()
                val yogaDAO = populateYogaTable()

                //Act & Assert
                assertEquals(4, yogaDAO.getAll().size)
                yogaDAO.deleteByYogaId(yoga3.yogaId)
                assertEquals(3, yogaDAO.getAll().size)
            }
            emptyYogasTable()
        }


        @Test
        fun `deleting yogas when none exist for user id results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three yogas
                val userDAO = populateUserTable()
                val yogaDAO = populateYogaTable()

                //Act & Assert
                assertEquals(4, yogaDAO.getAll().size)
                yogaDAO.deleteByUserId(5)
                assertEquals(4, yogaDAO.getAll().size)
            }
            emptyYogasTable()
        }

        @Test
        fun `deleting yogas when 1 or more exist for user id results in deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three yogas
                val userDAO = populateUserTable()
                val yogaDAO = populateYogaTable()

                //Act & Assert
                assertEquals(4, yogaDAO.getAll().size)
                yogaDAO.deleteByUserId(1)
                assertEquals(3, yogaDAO.getAll().size)
            }
            emptyYogasTable()
        }
    }
}