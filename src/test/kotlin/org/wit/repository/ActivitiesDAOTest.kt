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
import org.wit.db.Activities
import org.wit.domain.ActivityDTO
import org.wit.helpers.activities
import org.wit.helpers.populateActivityTable
import org.wit.helpers.populateUserTable
import kotlin.test.assertEquals

//retrieving some test data from Fixtures
private val activity1 = activities.get(0)
private val activity2 = activities.get(1)
private val activity3 = activities.get(2)
private val activity4 = activities.get(3)
private val activity5 = activities.get(4)
private val activity6 = activities.get(5)

class ActivityDAOTest {


    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        //-------TO BE USED WITH INTERNAL DB----internal fun setupInMemoryDatabaseConnection() {
        internal fun setupDatabaseConnection() {
            val db = DBConfig().getDbConnection()
            val origin = "https://health-tracker-20096053.herokuapp.com"
            /* TO BE USED WITH INTERNAL APP
            val app = ServerContainer.instance
            val origin = "http://localhost:" + app.port()
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")  */
        }
    }


    internal  fun emptyActivitiesTable(){
        val activityDAO = ActivityDAO()
        activityDAO.emptyActivityTable()
    }

    @Nested
    inner class CreateActivities {

        @Test
        fun `multiple activities added to table can be retrieved successfully`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()
                //Act & Assert
                assertEquals(6, activityDAO.getAll().size)
                assertEquals(activity1.toString(), activityDAO.findByActivityId(activity1.activityId).toString())
                assertEquals(activity2.toString(), activityDAO.findByActivityId(activity2.activityId).toString())
                assertEquals(activity3.toString(), activityDAO.findByActivityId(activity3.activityId).toString())
                assertEquals(activity4.toString(), activityDAO.findByActivityId(activity4.activityId).toString())
                assertEquals(activity5.toString(), activityDAO.findByActivityId(activity5.activityId).toString())
                assertEquals(activity6.toString(), activityDAO.findByActivityId(activity6.activityId).toString())
            }
            emptyActivitiesTable()
        }
    }

    @Nested
    inner class ReadActivities {

        @Test
        fun `getting all activites from a populated table returns all rows`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()
                //Act & Assert
                assertEquals(6, activityDAO.getAll().size)
            }
            emptyActivitiesTable()
        }

        @Test
        fun `get activity by user id that has no activities, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()
                //Act & Assert
                assertEquals(0, activityDAO.findByUserId(5).size)
            }
            emptyActivitiesTable()
        }

        @Test
        fun `get activity by user id that exists, results in a correct activitie(s) returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()
                //Act & Assert
                assertEquals(activity1.toString(), activityDAO.findByUserId(1).get(0).toString())
                assertEquals(activity2.toString(), activityDAO.findByUserId(1).get(1).toString())
                assertEquals(activity3.toString(), activityDAO.findByUserId(2).get(0).toString())
            }
            emptyActivitiesTable()
        }

        @Test
        fun `get all activities over empty table returns none`() {
            transaction {

                //Arrange - create and setup activityDAO object
                SchemaUtils.create(Activities)
                val activityDAO = ActivityDAO()

                //Act & Assert
                assertEquals(0, activityDAO.getAll().size)
            }
            emptyActivitiesTable()
        }

        @Test
        fun `get activity by activity id that has no records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()
                //Act & Assert
                assertEquals(null, activityDAO.findByActivityId(7))
            }
            emptyActivitiesTable()
        }

        @Test
        fun `get activity by activity id that exists, results in a correct activity returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()
                //Act & Assert
                assertEquals(activity1.toString(), activityDAO.findByActivityId(1).toString())
                assertEquals(activity3.toString(), activityDAO.findByActivityId(3).toString())
            }
            emptyActivitiesTable()
        }
    }

    @Nested
    inner class UpdateActivities {

        @Test
        fun `updating existing activity in table results in successful update`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()

                //Act & Assert
                val activity3updated = ActivityDTO(activityId = 3, description = "Cardio", duration = 42.0,
                    calories = 220, started = DateTime.now(), userId = 2)
                activityDAO.updateByActivityId(activity3updated.activityId, activity3updated)
                assertEquals(activity3updated, activityDAO.findByActivityId(3))
            }
            emptyActivitiesTable()
        }

        @Test
        fun `updating non-existant activity in table results in no updates`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()

                //Act & Assert
                val activity7updated = ActivityDTO(activityId = 7, description = "Cardio", duration = 42.0,
                    calories = 220, started = DateTime.now(), userId = 2)
                activityDAO.updateByActivityId(7, activity7updated)
                assertEquals(null, activityDAO.findByActivityId(7))
                assertEquals(6, activityDAO.getAll().size)
            }
            emptyActivitiesTable()
        }
    }

    @Nested
    inner class DeleteActivities {

        @Test
        fun `deleting a non-existant activity (by id) in table results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()

                //Act & Assert
                assertEquals(6, activityDAO.getAll().size)
                activityDAO.deleteByActivityId(7)
                assertEquals(6, activityDAO.getAll().size)
            }
            emptyActivitiesTable()
        }

        @Test
        fun `deleting an existing activity (by id) in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()

                //Act & Assert
                assertEquals(6, activityDAO.getAll().size)
                activityDAO.deleteByActivityId(activity3.activityId)
                assertEquals(5, activityDAO.getAll().size)
            }
            emptyActivitiesTable()
        }


        @Test
        fun `deleting activities when none exist for user id results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()

                //Act & Assert
                assertEquals(6, activityDAO.getAll().size)
                activityDAO.deleteByUserId(5)
                assertEquals(6, activityDAO.getAll().size)
            }
            emptyActivitiesTable()
        }

        @Test
        fun `deleting activities when 1 or more exist for user id results in deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()

                //Act & Assert
                assertEquals(6, activityDAO.getAll().size)
                activityDAO.deleteByUserId(1)
                assertEquals(4, activityDAO.getAll().size)
            }
            emptyActivitiesTable()
        }
    }
}