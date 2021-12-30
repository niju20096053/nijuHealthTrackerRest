import org.jetbrains.exposed.sql.ResultRow
import org.wit.db.*
import org.wit.domain.*


fun mapToUserDTO(it: ResultRow) = UserDTO(
    userId = it[Users.userId],
    firstName =  it[Users.firstName],
    lastName = it[Users.lastName],
    email = it[Users.email],
    gender = it[Users.gender],
    mobile = it[Users.mobile],
    address = it[Users.address],
    age = it[Users.age],
    height = it[Users.height],
    weight = it[Users.weight],
    userName = it[Users.userName],
    password = it[Users.password]
)

fun mapToActivityDTO(it: ResultRow) = ActivityDTO(
    activityId = it[Activities.activityId],
    description = it[Activities.description],
    duration = it[Activities.duration],
    started = it[Activities.started],
    calories = it[Activities.calories],
    userId = it[Activities.userId]
)

fun mapToMealDTO(it: ResultRow) = MealDTO(
    mealId = it[MealTracker.mealId],
    mealName = it[MealTracker.mealName],
    mealType = it[MealTracker.mealType],
    dateTime = it[MealTracker.dateTime],
    calories = it[MealTracker.calories],
    quantity = it[MealTracker.quantity],
    userId = it[MealTracker.userId]
)

fun mapToSleepDTO(it: ResultRow) = SleepDTO(
    sleepTimeId = it[SleepTracker.sleepTimeId],
    sleepStart = it[SleepTracker.sleepStart],
    sleepEnd = it[SleepTracker.sleepEnd],
    sleepTimeInMinutes = it[SleepTracker.sleepTimeInMinutes],
    userId = it[SleepTracker.userId]
)

fun mapToYogaDTO(it: ResultRow) = YogaDTO(
    yogaId = it[YogaTracker.yogaId],
    yogaName = it[YogaTracker.yogaName],
    yogaStart = it[YogaTracker.yogaStart],
    yogaEnd = it[YogaTracker.yogaEnd],
    yogaDurationInMinutes = it[YogaTracker.yogaDurationInMinutes],
    userId = it[YogaTracker.userId]
)

fun mapToWaterDTO(it: ResultRow) = WaterDTO(
    waterId = it[WaterTracker.waterId],
    dateTimeOfDrinking = it[WaterTracker.dateTimeOfDrinking],
    quantity = it[WaterTracker.quantity],
    userId = it[WaterTracker.userId]
)

fun mapToGoalsDTO(it: ResultRow) = GoalsDTO(
    goalId = it[Goals.goalId],
    steps = it[Goals.steps],
    heartPoints = it[Goals.heartPoints],
    calories = it[Goals.calories],
    distance = it[Goals.distance],
    water = it[Goals.water],
    sleep = it[Goals.sleep],
    userId = it[Goals.userId]
)