package org.wit.helpers

import org.joda.time.DateTime
import org.wit.domain.ActivityDTO
import org.wit.domain.UserDTO
import org.wit.repository.user1
import org.wit.repository.user2
import org.wit.repository.user3
import org.wit.repository.user4

val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
val validName = "Test User 1"
val validEmail = "testuser1@test.com"

val users: ArrayList<UserDTO> = arrayListOf<UserDTO>(
    UserDTO(firstName = "Alice", lastName = "Wonderland", email = "alice@wonderland.com", gender ="Female", mobile ="1234567890", age = 31, address = "Hillcrest", height = 1.71, weight = 85.1, userName = "alice", password ="",  userId = 1),
    UserDTO(firstName = "Bob", lastName =  "Cat", email = "bob@cat.ie", gender = "Male", mobile = "1231231231", age = 29, address = "Waterford", height = 1.80, weight =78.9, userName = "bob", password="", userId = 2),
    UserDTO(firstName = "Mary", lastName = "Contrary", email = "mary@contrary.com", gender = "Female", mobile = "4564564566", age = 26, address = "Folly", height = 1.67, weight =92.4, userName = "mary", password="",userId = 3),
    UserDTO(firstName = "Carol", lastName = "Singer", email = "carol@singer.com",gender = "Female", mobile = "1231200000", age = 21, address = "Killkenny", height = 1.53, weight =56.2, userName = "carol", password="", userId = 4)
)


val activities: ArrayList<ActivityDTO> = arrayListOf<ActivityDTO>(
   /* ActivityDTO(description = "Running",duration = 12.5,calories = 112,started = DateTime.parse("2021-10-12T05:59:27.258Z"),userId = user1.userId, id =1),
    ActivityDTO(description = "Walking",duration = 20.2,calories = 230,started = DateTime.parse("2021-10-13T05:59:27.258Z"),userId = user1.userId, id=2),
    ActivityDTO(description = "Running",duration = 8.3,calories = 80,started = DateTime.parse("2021-10-12T05:59:27.258Z"),userId = user2.userId, id=3),
    ActivityDTO(description = "Running",duration = 11.0,calories = 101,started = DateTime.parse("2021-10-12T05:59:27.258Z"),userId = user3.userId, id=4),
    ActivityDTO(description = "Running",duration = 17.3,calories = 145,started = DateTime.parse("2021-10-12T05:59:27.258Z"),userId = user4.userId, id=5),
    ActivityDTO(description = "Jogging",duration = 18.6,calories = 120,started = DateTime.parse("2021-10-14T05:59:27.258Z"),userId = user4.userId, id=6)
*/
)