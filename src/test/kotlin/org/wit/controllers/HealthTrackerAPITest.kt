package org.wit.controllers

import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import org.junit.jupiter.api.TestInstance
import org.wit.helpers.ServerContainer
import kong.unirest.Unirest
import org.joda.time.DateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.wit.config.DBConfig
import org.wit.domain.ActivityDTO
import org.wit.domain.UserDTO
import org.wit.helpers.*
import org.wit.utilities.jsonToArrayWithDate
import org.wit.utilities.jsonToObject
import org.wit.utilities.jsonToObjectWithDate

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HealthTrackerAPITest {

    private val db = DBConfig().getDbConnection()
    //private val app = ServerContainer.instance
    private val origin = "http://localhost:7001" //+ app.port()

    @Nested
    inner class CreateUsers {
        @Test
        fun `add a user with correct details returns a 201 response`() {

            //Arrange & Act & Assert
            //    add the user and verify return code (using fixture data)

            try {
                println("Valid Email : " + validEmail)
                val retrievedUserExistingResponse = retrieveUserByEmail("testuser1@test.com")
                println("Status : ${retrievedUserExistingResponse.status} , body: ${retrievedUserExistingResponse.body}")
                val retrievedUserExisting: UserDTO = jsonToObject(retrievedUserExistingResponse.body.toString())
println("User: "+retrievedUserExisting+"  , user id: "+retrievedUserExisting.userId)
                val deleteResponse = deleteUser(retrievedUserExisting.userId)

                assertEquals(204, deleteResponse.status)
            }catch (e: Exception){

            }

            println("Valid fields: $validFirstName, $validLastName, $validGender, $validEmail, $validMobile,$validAge, $validAddress, $validHeight, $validWeight, $validuserName, $validPassword")
            val addResponse = addUser(validFirstName, validLastName, validGender, validEmail, validMobile,
                validAge,
                validAddress,
                validHeight,
                validWeight,
                validuserName,
                validPassword)
            println("Response message : " + addResponse.body + ", code : " + addResponse.status)
            assertEquals(201, addResponse.status)

            //Assert - retrieve the added user from the database and verify return code
            val retrieveResponse= retrieveUserByEmail(validEmail)
            assertEquals(200, retrieveResponse.status)

            //Assert - verify the contents of the retrieved user
             val retrievedUser : UserDTO = jsonToObject(addResponse.body.toString())
            assertEquals(validEmail, retrievedUser.email)
            assertEquals(validFirstName, retrievedUser.firstName)
            assertEquals(validLastName,retrievedUser.lastName)
            assertEquals(validAddress,retrievedUser.address)
            assertEquals(validAge,retrievedUser.age)
            assertEquals(validMobile,retrievedUser.mobile)
            assertEquals(validGender,retrievedUser.gender)
            assertEquals(validHeight,retrievedUser.height)
            assertEquals(validWeight,retrievedUser.weight)
            assertEquals(validuserName,retrievedUser.userName)
            assertEquals(validPassword,retrievedUser.password)

            //After - restore the db to previous state by deleting the added user
            val deleteResponse2 = deleteUser(retrievedUser.userId)
            assertEquals(204, deleteResponse2.status)
        }
    }

    @Nested
    inner class ReadUsers {
        @Test
        fun `get all users from the database returns 200 or 404 response`() {
            val response = Unirest.get(origin + "/api/users/").asString()
            if (response.status == 200) {
                val retrievedUsers: ArrayList<UserDTO> =
                    jsonToObject(response.body.toString())
                assertNotEquals(0, retrievedUsers.size)
            }
            else {
                assertEquals(404, response.status)
            }
        }

        @Test
        fun `get user by id when user does not exist returns 404 response`() {

            //Arrange & Act - attempt to retrieve the non-existent user from the database
            val retrieveResponse = retrieveUserById(Integer.MIN_VALUE)

            // Assert -  verify return code
            assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `get user by email when user does not exist returns 404 response`() {
            // Arrange & Act - attempt to retrieve the non-existent user from the database
            val retrieveResponse = retrieveUserByEmail(nonExistingEmail)
            // Assert -  verify return code
            assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `getting a user by id when id exists, returns a 200 response`() {

            //Arrange - add the user
            val addResponse = addUser(validFirstName, validLastName, validGender, validEmail, validMobile,
                validAge,
                validAddress,
                validHeight,
                validWeight,
                validuserName,
                validPassword)
            val addedUser : UserDTO = jsonToObject(addResponse.body.toString())

            //Assert - retrieve the added user from the database and verify return code
            val retrieveResponse = retrieveUserById(addedUser.userId)
            assertEquals(200, retrieveResponse.status)

            //After - restore the db to previous state by deleting the added user
            deleteUser(addedUser.userId)
        }

        @Test
        fun `getting a user by email when email exists, returns a 200 response`() {

            //Arrange - add the user
            addUser(validFirstName, validLastName, validGender, validEmail, validMobile,
                validAge,
                validAddress,
                validHeight,
                validWeight,
                validuserName,
                validPassword)

            //Assert - retrieve the added user from the database and verify return code
            val retrieveResponse = retrieveUserByEmail(validEmail)
            assertEquals(200, retrieveResponse.status)

            //After - restore the db to previous state by deleting the added user
            val retrievedUser : UserDTO = jsonToObject(retrieveResponse.body.toString())
            deleteUser(retrievedUser.userId)
        }
    }

    @Nested
    inner class UpdateUsers {
        @Test
        fun `updating a user when it exists, returns a 204 response`() {

            //Arrange - add the user that we plan to do an update on
            val addedResponse = addUser(validFirstName, validLastName, validGender, validEmail, validMobile,
                validAge,
                validAddress,
                validHeight,
                validWeight,
                validuserName,
                validPassword)
            val addedUser : UserDTO = jsonToObject(addedResponse.body.toString())

            //Act & Assert - update the email and name of the retrieved user and assert 204 is returned
            assertEquals(204, updateUser(addedUser.userId, updatedFirstName, updatedLastName, updatedGender, updatedEmail, updatedMobile,
                updatedAge,
                updatedAddress,
                updatedHeight,
                updatedWeight,
                updatedUserName,
                updatedPassword).status)

            //Act & Assert - retrieve updated user and assert details are correct
            val updatedUserResponse = retrieveUserById(addedUser.userId)
            val updatedUser : UserDTO = jsonToObject(updatedUserResponse.body.toString())
            assertEquals(updatedFirstName, updatedUser.firstName)
            assertEquals(updatedEmail, updatedUser.email)

            //After - restore the db to previous state by deleting the added user
            deleteUser(addedUser.userId)
        }

        @Test
        fun `updating a user when it doesn't exist, returns a 404 response`() {

            //Act & Assert - attempt to update the email and name of user that doesn't exist
            assertEquals(404, updateUser(-1, updatedFirstName, updatedLastName, updatedGender, updatedEmail,
                updatedMobile, updatedAge, updatedAddress, updatedHeight, updatedWeight, updatedUserName,
                updatedPassword).status)
        }
    }

    @Nested
    inner class DeleteUsers {
        @Test
        fun `deleting a user when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteUser(-1).status)
        }

        @Test
        fun `deleting a user when it exists, returns a 204 response`() {

            //Arrange - add the user that we plan to do a delete on
            val addedResponse = addUser(validFirstName, validLastName, validGender, validEmail, validMobile,
                validAge,
                validAddress,
                validHeight,
                validWeight,
                validuserName,
                validPassword)
            val addedUser : UserDTO = jsonToObject(addedResponse.body.toString())

            //Act & Assert - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.userId).status)

            //Act & Assert - attempt to retrieve the deleted user --> 404 response
            assertEquals(404, retrieveUserById(addedUser.userId).status)
        }
    }


    @Nested
    inner class CreateActivities {
        //   post(  "/api/activities", HealthTrackerAPI::addActivity)

        @Test
        fun `add an activity when a user exists for it, returns a 201 response`() {

            //Arrange - add a user and an associated activity that we plan to do a delete on
            val addedUser: UserDTO = jsonToObject(addUser(validFirstName, validLastName, validGender, validEmail, validMobile,
                validAge,
                validAddress,
                validHeight,
                validWeight,
                validuserName,
                validPassword).body.toString())

            val addActivityResponse = addActivity(
                activities.get(0).description,
                activities.get(0).duration, activities.get(0).calories, activities.get(0).started, addedUser.userId
            )
            assertEquals(201, addActivityResponse.status)

            //After - delete the user (Activity will cascade delete in the database)
            deleteUser(addedUser.userId)
        }

        @Test
        fun `add an activity when no user exists for it, returns a 404 response`() {

            //Arrange - check there is no user for -1 id
            val userId = -1
            assertEquals(404, retrieveUserById(userId).status)

            val addActivityResponse = addActivity(
                activities.get(0).description, activities.get(0).duration,
                activities.get(0).calories, activities.get(0).started, userId
            )
            assertEquals(404, addActivityResponse.status)
        }
    }

    @Nested
    inner class ReadActivities {
        //   get(   "/api/users/:user-id/activities", HealthTrackerAPI::getActivitiesByUserId)
        //   get(   "/api/activities", HealthTrackerAPI::getAllActivities)
        //   get(   "/api/activities/:activity-id", HealthTrackerAPI::getActivitiesByActivityId)
        @Test
        fun `get all activities from the database returns 200 or 404 response`() {
            val response = retrieveAllActivities()
            if (response.status == 200){
                val retrievedActivities = jsonToArrayWithDate(response, Array<ActivityDTO>::class.java)
                assertNotEquals(0, retrievedActivities.size)
            }
            else{
                assertEquals(404, response.status)
            }
        }

        @Test
        fun `get all activities by user id when user and activities exists returns 200 response`() {
            //Arrange - add a user and 3 associated activities that we plan to retrieve
            val addedUser : UserDTO = jsonToObject(addUser(validFirstName, validLastName, validGender, validEmail, validMobile,
                validAge,
                validAddress,
                validHeight,
                validWeight,
                validuserName,
                validPassword).body.toString())
            addActivity(activities.get(0).description, activities.get(0).duration,
                activities.get(0).calories, activities.get(0).started, addedUser.userId)
            addActivity(activities.get(1).description, activities.get(1).duration,
                activities.get(1).calories, activities.get(1).started, addedUser.userId)
            addActivity(activities.get(2).description, activities.get(2).duration,
                activities.get(2).calories, activities.get(2).started, addedUser.userId)

            //Assert and Act - retrieve the three added activities by user id
            val response = retrieveActivitiesByUserId(addedUser.userId)
            assertEquals(200, response.status)
            val retrievedActivities = jsonToArrayWithDate(response, Array<ActivityDTO>::class.java)
            assertEquals(3, retrievedActivities.size)

            //After - delete the added user and assert a 204 is returned (activities are cascade deleted)
            assertEquals(204, deleteUser(addedUser.userId).status)
        }

        @Test
        fun `get all activities by user id when no activities exist returns 404 response`() {
            //Arrange - add a user
            val addedUser : UserDTO = jsonToObject(addUser(validFirstName, validLastName, validGender, validEmail, validMobile,
                validAge,
                validAddress,
                validHeight,
                validWeight,
                validuserName,
                validPassword).body.toString())

            //Assert and Act - retrieve the activities by user id
            val response = retrieveActivitiesByUserId(addedUser.userId)
            assertEquals(404, response.status)

            //After - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.userId).status)
        }

        @Test
        fun `get all activities by user id when no user exists returns 404 response`() {
            //Arrange
            val userId = -1

            //Assert and Act - retrieve activities by user id
            val response = retrieveActivitiesByUserId(userId)
            assertEquals(404, response.status)
        }

        @Test
        fun `get activity by activity id when no activity exists returns 404 response`() {
            //Arrange
            val activityId = -1
            //Assert and Act - attempt to retrieve the activity by activity id
            val response = retrieveActivityByActivityId(activityId)
            assertEquals(404, response.status)
        }


        @Test
        fun `get activity by activity id when activity exists returns 200 response`() {
            //Arrange - add a user and associated activity
            val addedUser : UserDTO = jsonToObject(addUser(validFirstName, validLastName, validGender, validEmail, validMobile,
                validAge,
                validAddress,
                validHeight,
                validWeight,
                validuserName,
                validPassword).body.toString())
            val addActivityResponse = addActivity(activities.get(0).description,
                activities.get(0).duration, activities.get(0).calories,
                activities.get(0).started, addedUser.userId)
            assertEquals(201, addActivityResponse.status)
            val addedActivity = jsonToObjectWithDate(addActivityResponse, ActivityDTO::class.java)

            //Act & Assert - retrieve the activity by activity id
            val response = retrieveActivityByActivityId(addedActivity.activityId)
            assertEquals(200, response.status)

            //After - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.userId).status)
        }

    }

    @Nested
    inner class UpdateActivities {
        //  patch( "/api/activities/:activity-id", HealthTrackerAPI::updateActivity)
        @Test
        fun `updating an activity by activity id when it doesn't exist, returns a 404 response`() {
            val userId = -1
            val activityID = -1

            //Arrange - check there is no user for -1 id
            assertEquals(404, retrieveUserById(userId).status)

            //Act & Assert - attempt to update the details of an activity/user that doesn't exist
            assertEquals(404, updateActivity(activityID, updatedDescription, updatedDuration,
                updatedCalories, updatedStarted, userId).status)
        }

        @Test
        fun `updating an activity by activity id when it exists, returns 204 response`() {

            //Arrange - add a user and an associated activity that we plan to do an update on
            val addedUser : UserDTO = jsonToObject(addUser(validFirstName, validLastName, validGender, validEmail, validMobile,
                validAge,
                validAddress,
                validHeight,
                validWeight,
                validuserName,
                validPassword).body.toString())
            val addActivityResponse = addActivity(activities.get(0).description,
                activities.get(0).duration, activities.get(0).calories,
                activities.get(0).started, addedUser.userId)
            assertEquals(201, addActivityResponse.status)
            val addedActivity = jsonToObjectWithDate(addActivityResponse, ActivityDTO::class.java)

            //Act & Assert - update the added activity and assert a 204 is returned
            val updatedActivityResponse = updateActivity(addedActivity.activityId, updatedDescription,
                updatedDuration, updatedCalories, updatedStarted, addedUser.userId)
            assertEquals(204, updatedActivityResponse.status)

            //Assert that the individual fields were all updated as expected
            val retrievedActivityResponse = retrieveActivityByActivityId(addedActivity.activityId)
            val updatedActivity = jsonToObjectWithDate(retrievedActivityResponse, ActivityDTO::class.java)
            assertEquals(updatedDescription,updatedActivity.description)
            assertEquals(updatedDuration, updatedActivity.duration, 0.1)
            assertEquals(updatedCalories, updatedActivity.calories)
            //TODO updatedActivity object has current timestamp even though database has the right timestamp
            //TODO assertEquals(updatedStarted, updatedActivity.started )

            //After - delete the user
            deleteUser(addedUser.userId)
        }
    }

    @Nested
    inner class DeleteActivities {
        //   delete("/api/activities/:activity-id", HealthTrackerAPI::deleteActivityByActivityId)
        //   delete("/api/users/:user-id/activities", HealthTrackerAPI::deleteActivityByUserId)
        @Test
        fun `deleting an activity by activity id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteActivityByActivityId(-1).status)
        }

        @Test
        fun `deleting activities by user id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteActivitiesByUserId(-1).status)
        }

        @Test
        fun `deleting an activity by id when it exists, returns a 204 response`() {

            //Arrange - add a user and an associated activity that we plan to do a delete on
            val addedUser : UserDTO = jsonToObject(addUser(validFirstName, validLastName, validGender, validEmail, validMobile,
                validAge,
                validAddress,
                validHeight,
                validWeight,
                validuserName,
                validPassword).body.toString())
            val addActivityResponse = addActivity(activities.get(0).description,
                activities.get(0).duration, activities.get(0).calories, activities.get(0).started, addedUser.userId)
            assertEquals(201, addActivityResponse.status)

            //Act & Assert - delete the added activity and assert a 204 is returned
            val addedActivity = jsonToObjectWithDate(addActivityResponse, ActivityDTO::class.java)
            assertEquals(204, deleteActivityByActivityId(addedActivity.activityId).status)

            //After - delete the user
            deleteUser(addedUser.userId)
        }

        @Test
        fun `deleting all activities by userid when it exists, returns a 204 response`() {

            //Arrange - add a user and 3 associated activities that we plan to do a cascade delete
            val addedUser : UserDTO = jsonToObject(addUser(validFirstName, validLastName, validGender, validEmail, validMobile,
                validAge,
                validAddress,
                validHeight,
                validWeight,
                validuserName,
                validPassword).body.toString())
            val addActivityResponse1 = addActivity(activities.get(0).description, activities.get(0).duration,
                activities.get(0).calories, activities.get(0).started, addedUser.userId)
            assertEquals(201, addActivityResponse1.status)
            val addActivityResponse2 = addActivity(activities.get(1).description, activities.get(1).duration,
                activities.get(1).calories, activities.get(1).started, addedUser.userId)
            assertEquals(201, addActivityResponse2.status)
            val addActivityResponse3 = addActivity(activities.get(2).description, activities.get(2).duration,
                activities.get(2).calories, activities.get(2).started, addedUser.userId)
            assertEquals(201, addActivityResponse3.status)

            //Act & Assert - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.userId).status)

            //Act & Assert - attempt to retrieve the deleted activities
            val addedActivity1 = jsonToObjectWithDate(addActivityResponse1, ActivityDTO::class.java)
            val addedActivity2 = jsonToObjectWithDate(addActivityResponse2, ActivityDTO::class.java)
            val addedActivity3 = jsonToObjectWithDate(addActivityResponse3, ActivityDTO::class.java)
            assertEquals(404, retrieveActivityByActivityId(addedActivity1.activityId).status)
            assertEquals(404, retrieveActivityByActivityId(addedActivity2.activityId).status)
            assertEquals(404, retrieveActivityByActivityId(addedActivity3.activityId).status)
        }
    }

    //--------------------------------------------------------------------------------------
    // HELPER METHODS - could move them into a test utility class when submitting assignment
    //--------------------------------------------------------------------------------------

    //helper function to add a test user to the database
    private fun addUser (firstName: String, lastName: String, gender : String, email: String, mobile : String, age: Int, address: String, height: Double, weight: Double, userName: String, password: String): HttpResponse<JsonNode> {
        return Unirest.post(origin + "/api/users")
            .body("{\"firstName\":\"$firstName\",\"firstName\":\"$firstName\", \"lastName\":\"$lastName\", \"gender\":\"$gender\", \"email\":\"$email\", \"mobile\":\"$mobile\", \"age\":\"$age\", \"address\":\"$address\", \"height\":\"$height\", \"weight\":\"$weight\", \"userName\":\"$userName\", \"password\":\"$password\"}")
            .asJson()
    }

    //helper function to delete a test user from the database
    private fun deleteUser (id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/users/$id").asString()
    }

    //helper function to retrieve a test user from the database by email
    private fun retrieveUserByEmail(email : String) : HttpResponse<String> {
        println("API path URL: "+origin + "/api/users/email/${email}")
        return Unirest.get(origin + "/api/users/email/${email}").asString()
    }

    //helper function to retrieve a test user from the database by id
    private fun retrieveUserById(id: Int) : HttpResponse<String> {
        return Unirest.get(origin + "/api/users/${id}").asString()
    }

    //helper function to add a test user to the database
    private fun updateUser (id: Int, firstName: String, lastName: String, gender : String, email: String, mobile : String, age: Int, address: String, height: Double, weight: Double, userName: String, password: String): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/users/$id")
            .body("{\"firstName\":\"$firstName\", \"lastName\":\"$lastName\", \"gender\":\"$gender\", \"email\":\"$email\", \"mobile\":\"$mobile\", \"age\":\"$age\", \"address\":\"$address\", \"height\":\"$height\", \"weight\":\"$weight\", \"userName\":\"$userName\", \"password\":\"$password\"}")
            .asJson()
    }

    //helper function to retrieve all activities
    private fun retrieveAllActivities(): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/activities").asJson()
    }

    //helper function to retrieve activities by user id
    private fun retrieveActivitiesByUserId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/users/${id}/activities").asJson()
    }

    //helper function to retrieve activity by activity id
    private fun retrieveActivityByActivityId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/activities/${id}").asJson()
    }

    //helper function to delete an activity by activity id
    private fun deleteActivityByActivityId(id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/activities/$id").asString()
    }

    //helper function to delete an activity by activity id
    private fun deleteActivitiesByUserId(id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/users/$id/activities").asString()
    }

    //helper function to add a test user to the database
    private fun updateActivity(id: Int, description: String, duration: Double, calories: Int,
                               started: DateTime, userId: Int): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/activities/$id")
            .body("""
                {
                  "description":"$description",
                  "duration":$duration,
                  "calories":$calories,
                  "started":"$started",
                  "userId":$userId
                }
            """.trimIndent()).asJson()
    }

    //helper function to add an activity
    private fun addActivity(description: String, duration: Double, calories: Int,
                            started: DateTime, userId: Int): HttpResponse<JsonNode> {
        return Unirest.post(origin + "/api/activities")
            .body("""
                {
                   "description":"$description",
                   "duration":$duration,
                   "calories":$calories,
                   "started":"$started",
                   "userId":$userId
                }
            """.trimIndent())
            .asJson()
    }


}