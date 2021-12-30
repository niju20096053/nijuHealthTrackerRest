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
import org.wit.domain.MealDTO
import org.wit.helpers.*
import kotlin.test.assertEquals

//retrieving some test data from Fixtures
private val meal1 = meals.get(0)
private val meal2 = meals.get(1)
private val meal3 = meals.get(2)
private val meal4 = meals.get(3)
private val meal5 = meals.get(4)
private val meal6 = meals.get(5)

class MealDAOTest {


    companion object {
        //Make a connection to a local, in memory H2 database or Heroku postgres database.
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


    internal  fun emptyMealsTable(){
        val mealsDAO = MealDAO()
        mealsDAO.emptyMealsTable()
    }

    @Nested
    inner class CreateMealTracker {

        @Test
        fun `multiple meals added to table can be retrieved successfully`() {
            transaction {
                //Arrange - create and populate tables with users and meals
                val userDAO = populateUserTable()
                val mealDAO = populateMealTable()
                //Act & Assert
                assertEquals(6, mealDAO.getAll().size)
                assertEquals(meal1.toString(), mealDAO.findByMealId(meal1.mealId).toString())
                assertEquals(meal2.toString(), mealDAO.findByMealId(meal2.mealId).toString())
                assertEquals(meal3.toString(), mealDAO.findByMealId(meal3.mealId).toString())
                assertEquals(meal4.toString(), mealDAO.findByMealId(meal4.mealId).toString())
                assertEquals(meal5.toString(), mealDAO.findByMealId(meal5.mealId).toString())
                assertEquals(meal6.toString(), mealDAO.findByMealId(meal6.mealId).toString())
            }
            emptyMealsTable()
        }
    }

    @Nested
    inner class ReadMealTracker {

        @Test
        fun `getting all activites from a populated table returns all rows`() {
            transaction {
                //Arrange - create and populate tables with three users and three meals
                val userDAO = populateUserTable()
                val mealDAO = populateMealTable()
                //Act & Assert
                assertEquals(6, mealDAO.getAll().size)
            }
            emptyMealsTable()
        }

        @Test
        fun `get meal by user id that has no meals, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three meals
                val userDAO = populateUserTable()
                val mealDAO = populateMealTable()
                //Act & Assert
                assertEquals(0, mealDAO.findByUserId(5).size)
            }
            emptyMealsTable()
        }

        @Test
        fun `get meal by user id that exists, results in a correct meal(s) returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three meals
                val userDAO = populateUserTable()
                val mealDAO = populateMealTable()
                //Act & Assert
                assertEquals(meal1.toString(), mealDAO.findByUserId(1).get(0).toString())
                assertEquals(meal2.toString(), mealDAO.findByUserId(1).get(1).toString())
                assertEquals(meal3.toString(), mealDAO.findByUserId(2).get(0).toString())
            }
            emptyMealsTable()
        }

        @Test
        fun `get all meals over empty table returns none`() {
            transaction {

                //Arrange - create and setup mealDAO object
                SchemaUtils.create(MealTracker)
                val mealDAO = MealDAO()

                //Act & Assert
                assertEquals(0, mealDAO.getAll().size)
            }
            emptyMealsTable()
        }

        @Test
        fun `get meal by meal id that has no records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three meals
                val userDAO = populateUserTable()
                val mealDAO = populateMealTable()
                //Act & Assert
                assertEquals(null, mealDAO.findByMealId(7))
            }
            emptyMealsTable()
        }

        @Test
        fun `get meal by meal id that exists, results in a correct meal returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three meals
                val userDAO = populateUserTable()
                val mealDAO = populateMealTable()
                //Act & Assert
                assertEquals(meal1.toString(), mealDAO.findByMealId(1).toString())
                assertEquals(meal3.toString(), mealDAO.findByMealId(3).toString())
            }
            emptyMealsTable()
        }
    }

    @Nested
    inner class UpdateMealTracker {

        @Test
        fun `updating existing meal in table results in successful update`() {
            transaction {

                //Arrange - create and populate tables with three users and three meals
                val userDAO = populateUserTable()
                val mealDAO = populateMealTable()

                //Act & Assert
                val meal3updated = MealDTO(mealId =3, mealName = "Biriyani", mealType = "DinnerUpdated", dateTime = DateTime.now(), calories = 300.5, quantity = 500.0, userId = 2)
                mealDAO.updateByMealId(meal3updated.mealId, meal3updated)
                assertEquals(meal3updated, mealDAO.findByMealId(3))
            }
            emptyMealsTable()
        }

        @Test
        fun `updating non-existant meal in table results in no updates`() {
            transaction {

                //Arrange - create and populate tables with three users and three meals
                val userDAO = populateUserTable()
                val mealDAO = populateMealTable()

                //Act & Assert
                val meal7updated = MealDTO(mealId =7, mealName = "BiriyaniUpdated", mealType = "Lunch", dateTime = DateTime.parse("2021-11-12T05:59:27.258Z"), calories = 300.5, quantity = 500.0, userId = 3)
                mealDAO.updateByMealId(7, meal7updated)
                assertEquals(null, mealDAO.findByMealId(7))
                assertEquals(6, mealDAO.getAll().size)
            }
            emptyMealsTable()
        }
    }

    @Nested
    inner class DeleteMealTracker {

        @Test
        fun `deleting a non-existant meal (by id) in table results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three meals
                val userDAO = populateUserTable()
                val mealDAO = populateMealTable()

                //Act & Assert
                assertEquals(6, mealDAO.getAll().size)
                mealDAO.deleteByMealId(7)
                assertEquals(6, mealDAO.getAll().size)
            }
            emptyMealsTable()
        }

        @Test
        fun `deleting an existing meal (by id) in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate tables with three users and three meals
                val userDAO = populateUserTable()
                val mealDAO = populateMealTable()

                //Act & Assert
                assertEquals(6, mealDAO.getAll().size)
                mealDAO.deleteByMealId(meal3.mealId)
                assertEquals(5, mealDAO.getAll().size)
            }
            emptyMealsTable()
        }


        @Test
        fun `deleting meals when none exist for user id results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three meals
                val userDAO = populateUserTable()
                val mealDAO = populateMealTable()

                //Act & Assert
                assertEquals(6, mealDAO.getAll().size)
                mealDAO.deleteByUserId(5)
                assertEquals(6, mealDAO.getAll().size)
            }
            emptyMealsTable()
        }

        @Test
        fun `deleting meals when 1 or more exist for user id results in deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three meals
                val userDAO = populateUserTable()
                val mealDAO = populateMealTable()

                //Act & Assert
                assertEquals(6, mealDAO.getAll().size)
                mealDAO.deleteByUserId(1)
                assertEquals(4, mealDAO.getAll().size)
            }
            emptyMealsTable()
        }
    }
}