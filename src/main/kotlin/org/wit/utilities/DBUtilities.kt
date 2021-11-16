import org.jetbrains.exposed.sql.ResultRow
import org.wit.db.Activities
import org.wit.db.Users
import org.wit.domain.ActivityDTO
import org.wit.domain.UserDTO


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
    id = it[Activities.id],
    description = it[Activities.description],
    duration = it[Activities.duration],
    started = it[Activities.started],
    calories = it[Activities.calories],
    userId = it[Activities.userId]
)