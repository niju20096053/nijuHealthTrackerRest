package org.wit.helpers

import org.jetbrains.exposed.sql.SchemaUtils
import org.joda.time.DateTime
import org.wit.db.*
import org.wit.domain.*
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

val meals: ArrayList<MealDTO> = arrayListOf<MealDTO>(
    MealDTO(mealId =1, mealName = "Pasta", mealType = "Breakfast", dateTime = DateTime.parse("2021-11-12T05:59:27.258Z"), calories = 80.5, quantity = 210.5, userId = users[0].userId),
    MealDTO(mealId =2, mealName = "Coffee", mealType = "Drink", dateTime = DateTime.parse("2021-11-12T05:59:27.258Z"), calories = 20.5, quantity = 30.0, userId = users[0].userId),
    MealDTO(mealId =3, mealName = "Biriyani", mealType = "Lunch", dateTime = DateTime.parse("2021-11-12T05:59:27.258Z"), calories = 300.5, quantity = 500.0, userId = users[1].userId),
    MealDTO(mealId =4, mealName = "Dosa", mealType = "Dinner", dateTime = DateTime.parse("2021-11-12T05:59:27.258Z"), calories = 80.5, quantity = 210.5, userId = users[2].userId),
    MealDTO(mealId =5, mealName = "Pasta", mealType = "Breakfast", dateTime = DateTime.parse("2021-11-12T05:59:27.258Z"), calories = 80.5, quantity = 210.5, userId = users[3].userId),
    MealDTO(mealId =6, mealName = "Chips", mealType = "Snack", dateTime = DateTime.parse("2021-11-12T05:59:27.258Z"), calories = 50.5, quantity = 100.0, userId = users[3].userId),
)

val sleeps: ArrayList<SleepDTO> = arrayListOf<SleepDTO>(
    SleepDTO(sleepTimeId =1, sleepStart = DateTime.parse("2021-11-12T22:59:27.258Z"), sleepEnd = DateTime.parse("2021-11-13T05:59:27.258Z"), sleepTimeInMinutes = 420, userId = users[0].userId),
    SleepDTO(sleepTimeId =2, sleepStart = DateTime.parse("2021-11-13T22:59:27.258Z"), sleepEnd = DateTime.parse("2021-11-14T05:59:27.258Z"), sleepTimeInMinutes = 420, userId = users[0].userId),
    SleepDTO(sleepTimeId =3, sleepStart = DateTime.parse("2021-11-12T22:59:27.258Z"), sleepEnd = DateTime.parse("2021-11-13T05:59:27.258Z"), sleepTimeInMinutes = 420, userId = users[1].userId),
    SleepDTO(sleepTimeId =4, sleepStart = DateTime.parse("2021-11-13T22:59:27.258Z"), sleepEnd = DateTime.parse("2021-11-14T05:59:27.258Z"), sleepTimeInMinutes = 420, userId = users[1].userId),
    SleepDTO(sleepTimeId =5, sleepStart = DateTime.parse("2021-11-12T22:59:27.258Z"), sleepEnd = DateTime.parse("2021-11-13T05:59:27.258Z"), sleepTimeInMinutes = 420, userId = users[2].userId),
    SleepDTO(sleepTimeId =6, sleepStart = DateTime.parse("2021-11-13T22:59:27.258Z"), sleepEnd = DateTime.parse("2021-11-14T05:59:27.258Z"), sleepTimeInMinutes = 420, userId = users[2].userId),
    SleepDTO(sleepTimeId =7, sleepStart = DateTime.parse("2021-11-12T22:59:27.258Z"), sleepEnd = DateTime.parse("2021-11-13T05:59:27.258Z"), sleepTimeInMinutes = 420, userId = users[3].userId),
    SleepDTO(sleepTimeId =8, sleepStart = DateTime.parse("2021-11-13T22:59:27.258Z"), sleepEnd = DateTime.parse("2021-11-14T05:59:27.258Z"), sleepTimeInMinutes = 420, userId = users[3].userId),
)


val yogas: ArrayList<YogaDTO> = arrayListOf<YogaDTO>(
    YogaDTO(yogaId =1, yogaName = "Yoga1", yogaStart = DateTime.parse("2021-11-12T22:59:27.258Z"), yogaEnd = DateTime.parse("2021-11-13T00:59:27.258Z"), yogaDurationInMinutes = 120, userId = users[0].userId),
    YogaDTO(yogaId =2, yogaName = "Yoga2", yogaStart = DateTime.parse("2021-11-12T22:59:27.258Z"), yogaEnd = DateTime.parse("2021-11-13T00:59:27.258Z"), yogaDurationInMinutes = 120, userId = users[1].userId),
    YogaDTO(yogaId =3, yogaName = "Yoga3", yogaStart = DateTime.parse("2021-11-12T22:59:27.258Z"), yogaEnd = DateTime.parse("2021-11-13T00:59:27.258Z"), yogaDurationInMinutes = 120, userId = users[2].userId),
    YogaDTO(yogaId =4, yogaName = "Yoga4", yogaStart = DateTime.parse("2021-11-12T22:59:27.258Z"), yogaEnd = DateTime.parse("2021-11-13T00:59:27.258Z"), yogaDurationInMinutes = 120, userId = users[3].userId),
)

val waters: ArrayList<WaterDTO> = arrayListOf<WaterDTO>(
    WaterDTO(waterId =1, dateTimeOfDrinking = DateTime.parse("2021-11-13T00:59:27.258Z"), quantity = 350, userId = users[0].userId),
    WaterDTO(waterId =2, dateTimeOfDrinking = DateTime.parse("2021-11-13T00:59:27.258Z"), quantity = 500, userId = users[1].userId),
    WaterDTO(waterId =3, dateTimeOfDrinking = DateTime.parse("2021-11-13T00:59:27.258Z"), quantity = 270, userId = users[2].userId),
    WaterDTO(waterId =4, dateTimeOfDrinking = DateTime.parse("2021-11-13T00:59:27.258Z"), quantity = 1200, userId = users[3].userId),
   )

val goals: ArrayList<GoalsDTO> = arrayListOf<GoalsDTO>(
    GoalsDTO(goalId = 1, steps = 3000, heartPoints = 20, calories = 600, distance = 5, water = 3000, sleep = 450, userId = users[0].userId),
    GoalsDTO(goalId = 2, steps = 3500, heartPoints = 30, calories = 400, distance = 3, water = 2500, sleep = 500, userId = users[1].userId),
    GoalsDTO(goalId = 3, steps = 3300, heartPoints = 15, calories = 700, distance = 6, water = 3500, sleep = 400, userId = users[2].userId),
    GoalsDTO(goalId = 4, steps = 3100, heartPoints = 18, calories = 450, distance = 4, water = 3200, sleep = 420, userId = users[3].userId)
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
    activityDAO.save(activities.get(0))
    activityDAO.save(activities.get(1))
    activityDAO.save(activities.get(2))
    activityDAO.save(activities.get(3))
    activityDAO.save(activities.get(4))
    activityDAO.save(activities.get(5))
    return activityDAO
}

fun populateMealTable(): MealDAO{
    SchemaUtils.create(MealTracker)
    val mealDAO = MealDAO()
    mealDAO.emptyMealsTable()
    mealDAO.save(meals.get(0))
    mealDAO.save(meals.get(1))
    mealDAO.save(meals.get(2))
    mealDAO.save(meals.get(3))
    mealDAO.save(meals.get(4))
    mealDAO.save(meals.get(5))
    return mealDAO
}

fun populateSleepTable(): SleepDAO{
    SchemaUtils.create(SleepTracker)
    val sleepDAO = SleepDAO()
    sleepDAO.emptySleepsTable()
    sleepDAO.save(sleeps.get(0))
    sleepDAO.save(sleeps.get(1))
    sleepDAO.save(sleeps.get(2))
    sleepDAO.save(sleeps.get(3))
    sleepDAO.save(sleeps.get(4))
    sleepDAO.save(sleeps.get(5))
    sleepDAO.save(sleeps.get(6))
    sleepDAO.save(sleeps.get(7))
    return sleepDAO
}

fun populateYogaTable(): YogaDAO{
    SchemaUtils.create(YogaTracker)
    val yogaDAO = YogaDAO()
    yogaDAO.emptyYogasTable()
    yogaDAO.save(yogas.get(0))
    yogaDAO.save(yogas.get(1))
    yogaDAO.save(yogas.get(2))
    yogaDAO.save(yogas.get(3))
    return yogaDAO
}

fun populateWaterTable(): WaterDAO{
    SchemaUtils.create(WaterTracker)
    val waterDAO = WaterDAO()
    waterDAO.emptyWatersTable()
    waterDAO.save(waters.get(0))
    waterDAO.save(waters.get(1))
    waterDAO.save(waters.get(2))
    waterDAO.save(waters.get(3))
    return waterDAO
}

fun populateGoalsTable(): GoalsDAO{
    SchemaUtils.create(Goals)
    val goalsDAO = GoalsDAO()
    goalsDAO.emptyGoalsTable()
    goalsDAO.save(goals.get(0))
    goalsDAO.save(goals.get(1))
    goalsDAO.save(goals.get(2))
    goalsDAO.save(goals.get(3))
    return goalsDAO
}