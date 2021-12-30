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
import org.wit.db.HealthConditionTracker
import org.wit.domain.HealthConditionDTO
import org.wit.helpers.*
import kotlin.test.assertEquals

//retrieving some test data from Fixtures
private val healthCondition1 = healthConditions.get(0)
private val healthCondition2 = healthConditions.get(1)
private val healthCondition3 = healthConditions.get(2)
private val healthCondition4 = healthConditions.get(3)
class HealthConditionDAOTest {


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


    internal  fun emptyHealthConditionTable(){
        val healthConditionsDAO = HealthConditionDAO()
        healthConditionsDAO.emptyHealthConditionTable()
    }

    @Nested
    inner class CreateHealthCondition {

        @Test
        fun `multiple healthConditions added to table can be retrieved successfully`() {
            transaction {
                //Arrange - create and populate tables with users and healthConditions
                val userDAO = populateUserTable()
                val healthConditionDAO = populateHealthConditionTrackerTable()
                //Act & Assert
                assertEquals(4, healthConditionDAO.getAll().size)
                assertEquals(healthCondition1.toString(), healthConditionDAO.findByHealthConditionId(healthCondition1.healthConditionId).toString())
                assertEquals(healthCondition2.toString(), healthConditionDAO.findByHealthConditionId(healthCondition2.healthConditionId).toString())
                assertEquals(healthCondition3.toString(), healthConditionDAO.findByHealthConditionId(healthCondition3.healthConditionId).toString())
                assertEquals(healthCondition4.toString(), healthConditionDAO.findByHealthConditionId(healthCondition4.healthConditionId).toString())
            }
            emptyHealthConditionTable()
        }
    }

    @Nested
    inner class ReadHealthCondition {

        @Test
        fun `getting all activites from a populated table returns all rows`() {
            transaction {
                //Arrange - create and populate tables with three users and three healthConditions
                val userDAO = populateUserTable()
                val healthConditionDAO = populateHealthConditionTrackerTable()
                //Act & Assert
                assertEquals(4, healthConditionDAO.getAll().size)
            }
            emptyHealthConditionTable()
        }

        @Test
        fun `get healthCondition by user id that has no healthConditions, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three healthConditions
                val userDAO = populateUserTable()
                val healthConditionDAO = populateHealthConditionTrackerTable()
                //Act & Assert
                assertEquals(0, healthConditionDAO.findByUserId(5).size)
            }
            emptyHealthConditionTable()
        }

        @Test
        fun `get healthCondition by user id that exists, results in a correct healthCondition(s) returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three healthConditions
                val userDAO = populateUserTable()
                val healthConditionDAO = populateHealthConditionTrackerTable()
                //Act & Assert
                assertEquals(healthCondition1.toString(), healthConditionDAO.findByUserId(1).get(0).toString())
                assertEquals(healthCondition2.toString(), healthConditionDAO.findByUserId(2).get(0).toString())
                assertEquals(healthCondition3.toString(), healthConditionDAO.findByUserId(3).get(0).toString())
                assertEquals(healthCondition4.toString(), healthConditionDAO.findByUserId(4).get(0).toString())
            }
            emptyHealthConditionTable()
        }

        @Test
        fun `get all healthConditions over empty table returns none`() {
            transaction {

                //Arrange - create and setup healthConditionDAO object
                SchemaUtils.create(HealthConditionTracker)
                val healthConditionDAO = HealthConditionDAO()

                //Act & Assert
                assertEquals(0, healthConditionDAO.getAll().size)
            }
            emptyHealthConditionTable()
        }

        @Test
        fun `get healthCondition by healthCondition id that has no records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three healthConditions
                val userDAO = populateUserTable()
                val healthConditionDAO = populateHealthConditionTrackerTable()
                //Act & Assert
                assertEquals(null, healthConditionDAO.findByHealthConditionId(5))
            }
            emptyHealthConditionTable()
        }

        @Test
        fun `get healthCondition by healthCondition id that exists, results in a correct healthCondition returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three healthConditions
                val userDAO = populateUserTable()
                val healthConditionDAO = populateHealthConditionTrackerTable()
                //Act & Assert
                assertEquals(healthCondition1.toString(), healthConditionDAO.findByHealthConditionId(1).toString())
                assertEquals(healthCondition3.toString(), healthConditionDAO.findByHealthConditionId(3).toString())
            }
            emptyHealthConditionTable()
        }
    }

    @Nested
    inner class UpdateHealthCondition {

        @Test
        fun `updating existing healthCondition in table results in successful update`() {
            transaction {

                //Arrange - create and populate tables with three users and three healthConditions
                val userDAO = populateUserTable()
                val healthConditionDAO = populateHealthConditionTrackerTable()

                //Act & Assert
                val healthCondition3updated = HealthConditionDTO( healthConditionId =3, pulseRate = 60, bloodPressure = "100/70", cholesterol = 200, bloodSugar = 100, bmi = 15.4, userId = 2)
                healthConditionDAO.updateByHealthConditionId(healthCondition3updated.healthConditionId, healthCondition3updated)
                assertEquals(healthCondition3updated, healthConditionDAO.findByHealthConditionId(3))
            }
            emptyHealthConditionTable()
        }

        @Test
        fun `updating non-existant healthCondition in table results in no updates`() {
            transaction {

                //Arrange - create and populate tables with three users and three healthConditions
                val userDAO = populateUserTable()
                val healthConditionDAO = populateHealthConditionTrackerTable()

                //Act & Assert
                val healthCondition5updated = HealthConditionDTO( healthConditionId =5, pulseRate = 60, bloodPressure = "100/70", cholesterol = 200, bloodSugar = 100, bmi = 15.4, userId = 3)
                healthConditionDAO.updateByHealthConditionId(5, healthCondition5updated)
                assertEquals(null, healthConditionDAO.findByHealthConditionId(5))
                assertEquals(4, healthConditionDAO.getAll().size)
            }
            emptyHealthConditionTable()
        }
    }

    @Nested
    inner class DeleteHealthCondition {

        @Test
        fun `deleting a non-existant healthCondition (by id) in table results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three healthConditions
                val userDAO = populateUserTable()
                val healthConditionDAO = populateHealthConditionTrackerTable()

                //Act & Assert
                assertEquals(4, healthConditionDAO.getAll().size)
                healthConditionDAO.deleteByHealthConditionId(5)
                assertEquals(4, healthConditionDAO.getAll().size)
            }
            emptyHealthConditionTable()
        }

        @Test
        fun `deleting an existing healthCondition (by id) in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate tables with three users and three healthConditions
                val userDAO = populateUserTable()
                val healthConditionDAO = populateHealthConditionTrackerTable()

                //Act & Assert
                assertEquals(4, healthConditionDAO.getAll().size)
                healthConditionDAO.deleteByHealthConditionId(healthCondition3.healthConditionId)
                assertEquals(3, healthConditionDAO.getAll().size)
            }
            emptyHealthConditionTable()
        }


        @Test
        fun `deleting healthConditions when none exist for user id results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three healthConditions
                val userDAO = populateUserTable()
                val healthConditionDAO = populateHealthConditionTrackerTable()

                //Act & Assert
                assertEquals(4, healthConditionDAO.getAll().size)
                healthConditionDAO.deleteByUserId(5)
                assertEquals(4, healthConditionDAO.getAll().size)
            }
            emptyHealthConditionTable()
        }

        @Test
        fun `deleting healthConditions when 1 or more exist for user id results in deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three healthConditions
                val userDAO = populateUserTable()
                val healthConditionDAO = populateHealthConditionTrackerTable()

                //Act & Assert
                assertEquals(4, healthConditionDAO.getAll().size)
                healthConditionDAO.deleteByUserId(1)
                assertEquals(3, healthConditionDAO.getAll().size)
            }
            emptyHealthConditionTable()
        }
    }
}