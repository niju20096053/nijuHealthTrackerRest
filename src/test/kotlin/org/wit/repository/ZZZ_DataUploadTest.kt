package org.wit.repository

import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.wit.config.DBConfig
import org.wit.helpers.*

class ZZZ_DataUploadTest {


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


        @Test
        fun `upload all the data into database`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                populateMealTable()
                populateSleepTable()
                populateYogaTable()
                populateWaterTable()
                populateGoalsTable()
                populateHealthConditionTrackerTable()
        }
        }
    }
