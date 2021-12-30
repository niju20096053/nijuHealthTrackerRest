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
import org.wit.db.SleepTracker
import org.wit.domain.SleepDTO
import org.wit.helpers.*
import kotlin.test.assertEquals

//retrieving some test data from Fixtures
private val sleep1 = sleeps.get(0)
private val sleep2 = sleeps.get(1)
private val sleep3 = sleeps.get(2)
private val sleep4 = sleeps.get(3)
private val sleep5 = sleeps.get(4)
private val sleep6 = sleeps.get(5)
private val sleep7 = sleeps.get(6)
private val sleep8 = sleeps.get(7)

class SleepDAOTest {


    companion object {
        //Make a connection to a local, in memory H2 database or Heroku postgres database.
        @BeforeAll
        @JvmStatic
        //-------TO BE USED WITH INTERNAL DB----internal fun setupInMemoryDatabaseConnection() {
        internal fun setupDatabaseConnection() {
            val db = DBConfig().getDbConnection()
            val origin = "https://health-tracker-20098053.herokuapp.com"
            /* TO BE USED WITH INTERNAL APP
            val app = ServerContainer.instance
            val origin = "http://localhost:" + app.port()
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")  */
        }
    }


    internal  fun emptySleepsTable(){
        val sleepsDAO = SleepDAO()
        sleepsDAO.emptySleepsTable()
    }

    @Nested
    inner class CreateSleepTracker {

        @Test
        fun `multiple sleeps added to table can be retrieved successfully`() {
            transaction {
                //Arrange - create and populate tables with users and sleeps
                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()
                //Act & Assert
                assertEquals(8, sleepDAO.getAll().size)
                assertEquals(sleep1.toString(), sleepDAO.findBySleepId(sleep1.sleepTimeId).toString())
                assertEquals(sleep2.toString(), sleepDAO.findBySleepId(sleep2.sleepTimeId).toString())
                assertEquals(sleep3.toString(), sleepDAO.findBySleepId(sleep3.sleepTimeId).toString())
                assertEquals(sleep4.toString(), sleepDAO.findBySleepId(sleep4.sleepTimeId).toString())
                assertEquals(sleep5.toString(), sleepDAO.findBySleepId(sleep5.sleepTimeId).toString())
                assertEquals(sleep8.toString(), sleepDAO.findBySleepId(sleep8.sleepTimeId).toString())
            }
            emptySleepsTable()
        }
    }

    @Nested
    inner class ReadSleepTracker {

        @Test
        fun `getting all activites from a populated table returns all rows`() {
            transaction {
                //Arrange - create and populate tables with three users and three sleeps
                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()
                //Act & Assert
                assertEquals(8, sleepDAO.getAll().size)
            }
            emptySleepsTable()
        }

        @Test
        fun `get sleep by user id that has no sleeps, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three sleeps
                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()
                //Act & Assert
                assertEquals(0, sleepDAO.findByUserId(5).size)
            }
            emptySleepsTable()
        }

        @Test
        fun `get sleep by user id that exists, results in a correct sleep(s) returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three sleeps
                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()
                //Act & Assert
                assertEquals(sleep1.toString(), sleepDAO.findByUserId(1).get(0).toString())
                assertEquals(sleep2.toString(), sleepDAO.findByUserId(1).get(1).toString())
                assertEquals(sleep3.toString(), sleepDAO.findByUserId(2).get(0).toString())
            }
            emptySleepsTable()
        }

        @Test
        fun `get all sleeps over empty table returns none`() {
            transaction {

                //Arrange - create and setup sleepDAO object
                SchemaUtils.create(SleepTracker)
                val sleepDAO = SleepDAO()

                //Act & Assert
                assertEquals(0, sleepDAO.getAll().size)
            }
            emptySleepsTable()
        }

        @Test
        fun `get sleep by sleep id that has no records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three sleeps
                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()
                //Act & Assert
                assertEquals(null, sleepDAO.findBySleepId(9))
            }
            emptySleepsTable()
        }

        @Test
        fun `get sleep by sleep id that exists, results in a correct sleep returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three sleeps
                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()
                //Act & Assert
                assertEquals(sleep1.toString(), sleepDAO.findBySleepId(1).toString())
                assertEquals(sleep3.toString(), sleepDAO.findBySleepId(3).toString())
            }
            emptySleepsTable()
        }
    }

    @Nested
    inner class UpdateSleepTracker {

        @Test
        fun `updating existing sleep in table results in successful update`() {
            transaction {

                //Arrange - create and populate tables with three users and three sleeps
                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()

                //Act & Assert
                val sleep3updated = SleepDTO(sleepTimeId =3, sleepStart = DateTime.now(), sleepEnd = DateTime.now(), sleepTimeInMinutes = 0 ,userId = 2)
                sleepDAO.updateBySleepId(sleep3updated.sleepTimeId, sleep3updated)
                assertEquals(sleep3updated, sleepDAO.findBySleepId(3))
            }
            emptySleepsTable()
        }

        @Test
        fun `updating non-existant sleep in table results in no updates`() {
            transaction {

                //Arrange - create and populate tables with three users and three sleeps
                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()

                //Act & Assert
                val sleep9updated = SleepDTO(sleepTimeId =9, sleepStart = DateTime.now(), sleepEnd = DateTime.now(), sleepTimeInMinutes = 0,  userId = 3)
                sleepDAO.updateBySleepId(9, sleep9updated)
                assertEquals(null, sleepDAO.findBySleepId(9))
                assertEquals(8, sleepDAO.getAll().size)
            }
            emptySleepsTable()
        }
    }

    @Nested
    inner class DeleteSleepTracker {

        @Test
        fun `deleting a non-existant sleep (by id) in table results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three sleeps
                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()

                //Act & Assert
                assertEquals(8, sleepDAO.getAll().size)
                sleepDAO.deleteBySleepId(9)
                assertEquals(8, sleepDAO.getAll().size)
            }
            emptySleepsTable()
        }

        @Test
        fun `deleting an existing sleep (by id) in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate tables with three users and three sleeps
                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()

                //Act & Assert
                assertEquals(8, sleepDAO.getAll().size)
                sleepDAO.deleteBySleepId(sleep3.sleepTimeId)
                assertEquals(7, sleepDAO.getAll().size)
            }
            emptySleepsTable()
        }


        @Test
        fun `deleting sleeps when none exist for user id results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three sleeps
                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()

                //Act & Assert
                assertEquals(8, sleepDAO.getAll().size)
                sleepDAO.deleteByUserId(5)
                assertEquals(8, sleepDAO.getAll().size)
            }
            emptySleepsTable()
        }

        @Test
        fun `deleting sleeps when 1 or more exist for user id results in deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three sleeps
                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()

                //Act & Assert
                assertEquals(8, sleepDAO.getAll().size)
                sleepDAO.deleteByUserId(1)
                assertEquals(6, sleepDAO.getAll().size)
            }
            emptySleepsTable()
        }
    }
}