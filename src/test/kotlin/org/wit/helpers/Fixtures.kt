package org.wit.helpers

import org.jetbrains.exposed.sql.SchemaUtils
import org.joda.time.DateTime
import org.wit.db.Activities
import org.wit.db.Users
import org.wit.domain.ActivityDTO
import org.wit.domain.UserDTO
import org.wit.repository.*

val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
val validFirstName = "Test User First Name 1"
val validLastName = "Test User Last Name 1"
val validGender = "Male"
val validEmail = "testuser1@test.com"
val validMobile = "+353123456789"
val validAge = 31
val validAddress = "Waterford"
val validHeight = 1.74
val validWeight = 87.3
val validuserName = "testUser1"
val validPassword = ""

val updatedFirstName = "Updated First Name"
val updatedLastName = "Updated Last Name"
val updatedGender = "Updated Gender"
val updatedEmail = "Updated Email"
val updatedMobile = "+353123123123"
val updatedAge = 30
val updatedAddress = "Waterford Co Updated"
val updatedHeight = 1.70
val updatedWeight = 80.2
val updatedUserName = "UpdatedUserName"
val updatedPassword=""

val users: ArrayList<UserDTO> = arrayListOf<UserDTO>(
    UserDTO(userId = 1, firstName = "Alice", lastName = "Wonderland", email = "alice@wonderland.com", gender ="Female", mobile ="1234567890", age = 31, address = "Hillcrest", height = 1.71, weight = 85.1, userName = "alice", password =""),
    UserDTO(userId = 2, firstName = "Bob", lastName =  "Cat", email = "bob@cat.ie", gender = "Male", mobile = "1231231231", age = 29, address = "Waterford", height = 1.80, weight =78.9, userName = "bob", password=""),
    UserDTO(userId = 3, firstName = "Mary", lastName = "Contrary", email = "mary@contrary.com", gender = "Female", mobile = "4564564566", age = 26, address = "Folly", height = 1.67, weight =92.4, userName = "mary", password=""),
    UserDTO(userId = 4, firstName = "Carol", lastName = "Singer", email = "carol@singer.com",gender = "Female", mobile = "1231200000", age = 21, address = "Killkenny", height = 1.53, weight =56.2, userName = "carol", password="")
)

val validDescription = "Walking"
val validDuration = 65.00
val validCalories = 230
val validStarted = DateTime.parse("2021-11-12T05:59:27.258Z")
val validUserId = 1
val updatedDescription = "Updated Walking"
val updatedDuration = 100.0
val updatedCalories = 500
val updatedStarted = DateTime.parse("2021-11-12T05:10:27.258Z")
val updatedUserId = 2



val activities: ArrayList<ActivityDTO> = arrayListOf<ActivityDTO>(
    ActivityDTO(activityId =1, description = "Running",duration = 12.5,calories = 112,started = DateTime.parse("2021-11-12T05:59:27.258Z"),userId = users[0].userId),
    ActivityDTO(activityId=2, description = "Walking",duration = 20.2,calories = 230,started = DateTime.parse("2021-11-13T05:59:27.258Z"),userId = users[0].userId),
    ActivityDTO(activityId=3, description = "Running",duration = 8.3,calories = 80,started = DateTime.parse("2021-11-12T05:59:27.258Z"),userId = users[1].userId),
    ActivityDTO(activityId=4, description = "Running",duration = 11.0,calories = 101,started = DateTime.parse("2021-11-12T05:59:27.258Z"),userId = users[2].userId),
    ActivityDTO(activityId=5, description = "Running",duration = 17.3,calories = 145,started = DateTime.parse("2021-11-12T05:59:27.258Z"),userId = users[3].userId),
    ActivityDTO(activityId=6, description = "Jogging",duration = 18.6,calories = 120,started = DateTime.parse("2021-11-14T05:59:27.258Z"),userId = users[3].userId)

)


fun populateUserTable(): UserDAO {
    SchemaUtils.create(Users)
    val userDAO = UserDAO()
    userDAO.emptyUserTable()
    userDAO.save(users.get(0))
    userDAO.save(users.get(1))
    userDAO.save(users.get(2))
    userDAO.save(users.get(3))
    return userDAO
}

fun populateActivityTable(): ActivityDAO {
    SchemaUtils.create(Activities)
    val activityDAO = ActivityDAO()
    activityDAO.emptyActivityTable()
    println("Activity 1 : ${activities.get(0)}")
    activityDAO.save(activities.get(0))
    println("Activity 2 : ${activities.get(1)}")
    activityDAO.save(activities.get(1))
    activityDAO.save(activities.get(2))
    activityDAO.save(activities.get(3))
    activityDAO.save(activities.get(4))
    activityDAO.save(activities.get(5))
    return activityDAO
}