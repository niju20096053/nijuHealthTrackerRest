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
import org.wit.db.WaterTracker
import org.wit.domain.WaterDTO
import org.wit.helpers.*
import kotlin.test.assertEquals

//retrieving some test data from Fixtures
private val water1 = waters.get(0)
private val water2 = waters.get(1)
private val water3 = waters.get(2)
private val water4 = waters.get(3)
class WaterDAOTest {


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


    internal  fun emptyWatersTable(){
        val watersDAO = WaterDAO()
        watersDAO.emptyWatersTable()
    }

    @Nested
    inner class CreateWaterTracker {

        @Test
        fun `multiple waters added to table can be retrieved successfully`() {
            transaction {
                //Arrange - create and populate tables with users and waters
                val userDAO = populateUserTable()
                val waterDAO = populateWaterTable()
                //Act & Assert
                assertEquals(4, waterDAO.getAll().size)
                assertEquals(water1.toString(), waterDAO.findByWaterId(water1.waterId).toString())
                assertEquals(water2.toString(), waterDAO.findByWaterId(water2.waterId).toString())
                assertEquals(water3.toString(), waterDAO.findByWaterId(water3.waterId).toString())
                assertEquals(water4.toString(), waterDAO.findByWaterId(water4.waterId).toString())
            }
            emptyWatersTable()
        }
    }

    @Nested
    inner class ReadWaterTracker {

        @Test
        fun `getting all activites from a populated table returns all rows`() {
            transaction {
                //Arrange - create and populate tables with three users and three waters
                val userDAO = populateUserTable()
                val waterDAO = populateWaterTable()
                //Act & Assert
                assertEquals(4, waterDAO.getAll().size)
            }
            emptyWatersTable()
        }

        @Test
        fun `get water by user id that has no waters, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three waters
                val userDAO = populateUserTable()
                val waterDAO = populateWaterTable()
                //Act & Assert
                assertEquals(0, waterDAO.findByUserId(5).size)
            }
            emptyWatersTable()
        }

        @Test
        fun `get water by user id that exists, results in a correct water(s) returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three waters
                val userDAO = populateUserTable()
                val waterDAO = populateWaterTable()
                //Act & Assert
                assertEquals(water1.toString(), waterDAO.findByUserId(1).get(0).toString())
                assertEquals(water2.toString(), waterDAO.findByUserId(2).get(0).toString())
                assertEquals(water3.toString(), waterDAO.findByUserId(3).get(0).toString())
                assertEquals(water4.toString(), waterDAO.findByUserId(4).get(0).toString())
            }
            emptyWatersTable()
        }

        @Test
        fun `get all waters over empty table returns none`() {
            transaction {

                //Arrange - create and setup waterDAO object
                SchemaUtils.create(WaterTracker)
                val waterDAO = WaterDAO()

                //Act & Assert
                assertEquals(0, waterDAO.getAll().size)
            }
            emptyWatersTable()
        }

        @Test
        fun `get water by water id that has no records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three waters
                val userDAO = populateUserTable()
                val waterDAO = populateWaterTable()
                //Act & Assert
                assertEquals(null, waterDAO.findByWaterId(5))
            }
            emptyWatersTable()
        }

        @Test
        fun `get water by water id that exists, results in a correct water returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three waters
                val userDAO = populateUserTable()
                val waterDAO = populateWaterTable()
                //Act & Assert
                assertEquals(water1.toString(), waterDAO.findByWaterId(1).toString())
                assertEquals(water3.toString(), waterDAO.findByWaterId(3).toString())
            }
            emptyWatersTable()
        }
    }

    @Nested
    inner class UpdateWaterTracker {

        @Test
        fun `updating existing water in table results in successful update`() {
            transaction {

                //Arrange - create and populate tables with three users and three waters
                val userDAO = populateUserTable()
                val waterDAO = populateWaterTable()

                //Act & Assert
                val water3updated = WaterDTO(waterId =3, dateTimeOfDrinking= DateTime.now(), quantity = 1000 ,userId = 2)
                waterDAO.updateByWaterId(water3updated.waterId, water3updated)
                assertEquals(water3updated, waterDAO.findByWaterId(3))
            }
            emptyWatersTable()
        }

        @Test
        fun `updating non-existant water in table results in no updates`() {
            transaction {

                //Arrange - create and populate tables with three users and three waters
                val userDAO = populateUserTable()
                val waterDAO = populateWaterTable()

                //Act & Assert
                val water5updated = WaterDTO(waterId =5, dateTimeOfDrinking= DateTime.now(), quantity = 1000 ,userId = 3)
                waterDAO.updateByWaterId(5, water5updated)
                assertEquals(null, waterDAO.findByWaterId(5))
                assertEquals(4, waterDAO.getAll().size)
            }
            emptyWatersTable()
        }
    }

    @Nested
    inner class DeleteWaterTracker {

        @Test
        fun `deleting a non-existant water (by id) in table results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three waters
                val userDAO = populateUserTable()
                val waterDAO = populateWaterTable()

                //Act & Assert
                assertEquals(4, waterDAO.getAll().size)
                waterDAO.deleteByWaterId(5)
                assertEquals(4, waterDAO.getAll().size)
            }
            emptyWatersTable()
        }

        @Test
        fun `deleting an existing water (by id) in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate tables with three users and three waters
                val userDAO = populateUserTable()
                val waterDAO = populateWaterTable()

                //Act & Assert
                assertEquals(4, waterDAO.getAll().size)
                waterDAO.deleteByWaterId(water3.waterId)
                assertEquals(3, waterDAO.getAll().size)
            }
            emptyWatersTable()
        }


        @Test
        fun `deleting waters when none exist for user id results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three waters
                val userDAO = populateUserTable()
                val waterDAO = populateWaterTable()

                //Act & Assert
                assertEquals(4, waterDAO.getAll().size)
                waterDAO.deleteByUserId(5)
                assertEquals(4, waterDAO.getAll().size)
            }
            emptyWatersTable()
        }

        @Test
        fun `deleting waters when 1 or more exist for user id results in deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three waters
                val userDAO = populateUserTable()
                val waterDAO = populateWaterTable()

                //Act & Assert
                assertEquals(4, waterDAO.getAll().size)
                waterDAO.deleteByUserId(1)
                assertEquals(3, waterDAO.getAll().size)
            }
            emptyWatersTable()
        }
    }
}