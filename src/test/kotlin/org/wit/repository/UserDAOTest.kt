package org.wit.repository

import org.wit.helpers.users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.wit.config.DBConfig
import org.wit.db.Users
import org.wit.domain.UserDTO
import org.wit.helpers.ServerContainer
import org.wit.helpers.nonExistingEmail
import org.wit.helpers.populateUserTable

//retrieving some test data from Fixtures
val user1 = users.get(0)
val user2 = users.get(1)
val user3 = users.get(2)
val user4 = users.get(3)

class UserDAOTest {

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

    internal  fun emptyUserTable(){
        val userDAO = UserDAO()
        userDAO.emptyUserTable()
    }

   /* internal fun populateUserTable(): UserDAO{
        SchemaUtils.create(Users)
        val userDAO = UserDAO()
        userDAO.delete(user1.userId)
        userDAO.deleteByEmail(user1.email)
        userDAO.save(user1)
        userDAO.delete(user2.userId)
        userDAO.deleteByEmail(user2.email)
        userDAO.save(user2)
        userDAO.delete(user3.userId)
        userDAO.deleteByEmail(user3.email)
        userDAO.save(user3)
        userDAO.delete(user4.userId)
        userDAO.deleteByEmail(user4.email)
        userDAO.save(user4)
        return userDAO
    }

    */

    @Nested
    inner class CreateUsers {
        @Test
        fun `multiple users added to table can be retrieved successfully`() {

            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                assertEquals(4, userDAO.getAll().size)
                assertEquals(user1, userDAO.findById(user1.userId))
                assertEquals(user2, userDAO.findById(user2.userId))
                assertEquals(user3, userDAO.findById(user3.userId))
                assertEquals(user4, userDAO.findById(user4.userId))
            }
            emptyUserTable()
        }
    }

    @Nested
    inner class ReadUsers {
        @Test
        fun `getting all users from a populated table returns all rows`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                assertEquals(4, userDAO.getAll().size)
            }
            emptyUserTable()
        }

        @Test
        fun `get user by id that doesn't exist, results in no user returned`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                assertEquals(null, userDAO.findById(5))
            }
            emptyUserTable()
        }

        @Test
        fun `get user by id that exists, results in a correct user returned`() {
            transaction {
                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                assertEquals(user4, userDAO.findById(4))
            }
            emptyUserTable()
        }
        @Test
        fun `get all users over empty table returns none`() {
            transaction {

                //Arrange - create and setup userDAO object
                SchemaUtils.create(Users)
                val userDAO = UserDAO()

                //Act & Assert
                assertEquals(0, userDAO.getAll().size)
            }
            emptyUserTable()
        }

        @Test
        fun `get user by email that doesn't exist, results in no user returned`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                assertEquals(null, userDAO.findByEmail(nonExistingEmail))
            }
            emptyUserTable()
        }

        @Test
        fun `get user by email that exists, results in correct user returned`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                assertEquals(user2, userDAO.findByEmail(user2.email))
            }
            emptyUserTable()
        }
    }

    @Nested
    inner class DeleteUsers {
        @Test
        fun `deleting a non-existant user in table results in no deletion`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                assertEquals(4, userDAO.getAll().size)
                userDAO.delete(5)
                assertEquals(4, userDAO.getAll().size)
            }
            emptyUserTable()
        }

        @Test
        fun `deleting an existing user in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                assertEquals(4, userDAO.getAll().size)
                userDAO.delete(user4.userId)
                assertEquals(3, userDAO.getAll().size)
            }
            emptyUserTable()
        }

        @Test
        fun `deleting a non-existant user in table using email results in no deletion`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                assertEquals(4, userDAO.getAll().size)
                userDAO.deleteByEmail(nonExistingEmail)
                assertEquals(4, userDAO.getAll().size)
            }
            emptyUserTable()
        }

        @Test
        fun `deleting an existing user in table using email results in record being deleted`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                assertEquals(4, userDAO.getAll().size)
                userDAO.deleteByEmail(user4.email)
                assertEquals(3, userDAO.getAll().size)
            }
            emptyUserTable()
        }
    }

    @Nested
    inner class UpdateUsers {

        @Test
        fun `updating existing user in table results in successful update`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                val user4Updated = UserDTO(4, "new firstName", "new LastName", "male","new@email.ie","1234567890",31, "Hillcrest", 1.71,
                    80.51,"niju","abc")
                userDAO.update(user4.userId, user4Updated)
                assertEquals(user4Updated, userDAO.findById(4))
            }
            emptyUserTable()
        }

        @Test
        fun `updating non-existant user in table results in no updates`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                val user5Updated = UserDTO(5, "new firstName", "new LastName", "male","new@email.ie","1234567890",31,"Hillcrest", 1.71,
                    80.51,"niju","abc")
                userDAO.update(5, user5Updated)
                assertEquals(null, userDAO.findById(5))
                assertEquals(4, userDAO.getAll().size)
            }
            emptyUserTable()
        }
    }
}