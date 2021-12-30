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
import org.wit.db.Goals
import org.wit.domain.GoalsDTO
import org.wit.helpers.*
import kotlin.test.assertEquals

//retrieving some test data from Fixtures
private val goal1 = goals.get(0)
private val goal2 = goals.get(1)
private val goal3 = goals.get(2)
private val goal4 = goals.get(3)
class GoalsDAOTest {


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


    internal  fun emptyGoalsTable(){
        val goalsDAO = GoalsDAO()
        goalsDAO.emptyGoalsTable()
    }

    @Nested
    inner class CreateGoals {

        @Test
        fun `multiple goals added to table can be retrieved successfully`() {
            transaction {
                //Arrange - create and populate tables with users and goals
                val userDAO = populateUserTable()
                val goalDAO = populateGoalsTable()
                //Act & Assert
                assertEquals(4, goalDAO.getAll().size)
                assertEquals(goal1.toString(), goalDAO.findByGoalsId(goal1.goalId).toString())
                assertEquals(goal2.toString(), goalDAO.findByGoalsId(goal2.goalId).toString())
                assertEquals(goal3.toString(), goalDAO.findByGoalsId(goal3.goalId).toString())
                assertEquals(goal4.toString(), goalDAO.findByGoalsId(goal4.goalId).toString())
            }
            emptyGoalsTable()
        }
    }

    @Nested
    inner class ReadGoals {

        @Test
        fun `getting all activites from a populated table returns all rows`() {
            transaction {
                //Arrange - create and populate tables with three users and three goals
                val userDAO = populateUserTable()
                val goalDAO = populateGoalsTable()
                //Act & Assert
                assertEquals(4, goalDAO.getAll().size)
            }
            emptyGoalsTable()
        }

        @Test
        fun `get goal by user id that has no goals, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three goals
                val userDAO = populateUserTable()
                val goalDAO = populateGoalsTable()
                //Act & Assert
                assertEquals(0, goalDAO.findByUserId(5).size)
            }
            emptyGoalsTable()
        }

        @Test
        fun `get goal by user id that exists, results in a correct goal(s) returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three goals
                val userDAO = populateUserTable()
                val goalDAO = populateGoalsTable()
                //Act & Assert
                assertEquals(goal1.toString(), goalDAO.findByUserId(1).get(0).toString())
                assertEquals(goal2.toString(), goalDAO.findByUserId(2).get(0).toString())
                assertEquals(goal3.toString(), goalDAO.findByUserId(3).get(0).toString())
                assertEquals(goal4.toString(), goalDAO.findByUserId(4).get(0).toString())
            }
            emptyGoalsTable()
        }

        @Test
        fun `get all goals over empty table returns none`() {
            transaction {

                //Arrange - create and setup goalDAO object
                SchemaUtils.create(Goals)
                val goalDAO = GoalsDAO()

                //Act & Assert
                assertEquals(0, goalDAO.getAll().size)
            }
            emptyGoalsTable()
        }

        @Test
        fun `get goal by goal id that has no records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three goals
                val userDAO = populateUserTable()
                val goalDAO = populateGoalsTable()
                //Act & Assert
                assertEquals(null, goalDAO.findByGoalsId(5))
            }
            emptyGoalsTable()
        }

        @Test
        fun `get goal by goal id that exists, results in a correct goal returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three goals
                val userDAO = populateUserTable()
                val goalDAO = populateGoalsTable()
                //Act & Assert
                assertEquals(goal1.toString(), goalDAO.findByGoalsId(1).toString())
                assertEquals(goal3.toString(), goalDAO.findByGoalsId(3).toString())
            }
            emptyGoalsTable()
        }
    }

    @Nested
    inner class UpdateGoals {

        @Test
        fun `updating existing goal in table results in successful update`() {
            transaction {

                //Arrange - create and populate tables with three users and three goals
                val userDAO = populateUserTable()
                val goalDAO = populateGoalsTable()

                //Act & Assert
                val goal3updated = GoalsDTO( goalId =3, steps = 3000, heartPoints = 15, calories = 200, distance = 3, water = 3400, sleep = 360, userId = 2)
                goalDAO.updateByGoalsId(goal3updated.goalId, goal3updated)
                assertEquals(goal3updated, goalDAO.findByGoalsId(3))
            }
            emptyGoalsTable()
        }

        @Test
        fun `updating non-existant goal in table results in no updates`() {
            transaction {

                //Arrange - create and populate tables with three users and three goals
                val userDAO = populateUserTable()
                val goalDAO = populateGoalsTable()

                //Act & Assert
                val goal5updated = GoalsDTO( goalId =5, steps = 3000, heartPoints = 15, calories = 200, distance = 3, water = 3400, sleep = 360, userId = 3)
                goalDAO.updateByGoalsId(5, goal5updated)
                assertEquals(null, goalDAO.findByGoalsId(5))
                assertEquals(4, goalDAO.getAll().size)
            }
            emptyGoalsTable()
        }
    }

    @Nested
    inner class DeleteGoals {

        @Test
        fun `deleting a non-existant goal (by id) in table results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three goals
                val userDAO = populateUserTable()
                val goalDAO = populateGoalsTable()

                //Act & Assert
                assertEquals(4, goalDAO.getAll().size)
                goalDAO.deleteByGoalsId(5)
                assertEquals(4, goalDAO.getAll().size)
            }
            emptyGoalsTable()
        }

        @Test
        fun `deleting an existing goal (by id) in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate tables with three users and three goals
                val userDAO = populateUserTable()
                val goalDAO = populateGoalsTable()

                //Act & Assert
                assertEquals(4, goalDAO.getAll().size)
                goalDAO.deleteByGoalsId(goal3.goalId)
                assertEquals(3, goalDAO.getAll().size)
            }
            emptyGoalsTable()
        }


        @Test
        fun `deleting goals when none exist for user id results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three goals
                val userDAO = populateUserTable()
                val goalDAO = populateGoalsTable()

                //Act & Assert
                assertEquals(4, goalDAO.getAll().size)
                goalDAO.deleteByUserId(5)
                assertEquals(4, goalDAO.getAll().size)
            }
            emptyGoalsTable()
        }

        @Test
        fun `deleting goals when 1 or more exist for user id results in deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three goals
                val userDAO = populateUserTable()
                val goalDAO = populateGoalsTable()

                //Act & Assert
                assertEquals(4, goalDAO.getAll().size)
                goalDAO.deleteByUserId(1)
                assertEquals(3, goalDAO.getAll().size)
            }
            emptyGoalsTable()
        }
    }
}