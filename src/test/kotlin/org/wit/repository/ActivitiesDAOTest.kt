package org.wit.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.wit.db.Activities
import org.wit.helpers.activities


//retrieving some test data from Fixtures
val activity1 = activities.get(0)
val activity2 = activities.get(1)
val activity3 = activities.get(2)
val activity4 = activities.get(3)
val activity5 = activities.get(4)
val activity6 = activities.get(5)

class ActivitiesDAOTest {


    companion object {

        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    internal fun populateUserTable(): ActivityDAO{
        SchemaUtils.create(Activities)
        val activityDAO = ActivityDAO()
        println("Activity 1 values: "+ activity1 +" , User 1 : " + user1)
        activityDAO.save(activity1)
        activityDAO.save(activity2)
        activityDAO.save(activity3)
        activityDAO.save(activity4)
        activityDAO.save(activity5)
        activityDAO.save(activity6)
        return activityDAO
    }

    @Test
    fun `multiple activities added to table can be retrieved successfully`() {
        transaction {

            //Arrange - create and populate table with three users
            val activityDAO = populateUserTable()

            //Act & Assert
            Assertions.assertEquals(6, activityDAO.getAll().size)
            Assertions.assertEquals(activity1, activityDAO.findByActivityId(activity1.id))
            Assertions.assertEquals(activity2, activityDAO.findByActivityId(activity2.id))
            Assertions.assertEquals(activity3, activityDAO.findByActivityId(activity3.id))
            Assertions.assertEquals(activity4, activityDAO.findByActivityId(activity4.id))
            Assertions.assertEquals(activity5, activityDAO.findByActivityId(activity5.id))
            Assertions.assertEquals(activity6, activityDAO.findByActivityId(activity6.id))

        }
    }
}