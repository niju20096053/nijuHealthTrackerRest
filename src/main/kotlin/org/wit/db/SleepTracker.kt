package org.wit.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage meal of the users.
//       Database wise, this is the table object.

object SleepTracker : Table("sleepTimes") {
    val sleepTimeId = integer("sleepTimeId").autoIncrement().primaryKey()
    val sleepStart = datetime("sleepStart")
    val sleepEnd = datetime("sleepEnd")
    val sleepTimeInMinutes = integer("sleepTimeInMinutes")
    val userId = integer("userId").references(ref = Users.userId, onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)
}