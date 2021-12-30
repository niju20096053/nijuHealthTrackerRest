package org.wit.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage one activity.
//       Database wise, this is the table object.

object Activities : Table("activities") {
    val activityId = integer("activityId").autoIncrement().primaryKey()
    val description = varchar("description", 100)
    val duration = double("duration")
    val calories = integer("calories")
    val started = datetime("started")
    val userId = integer("userId").references(ref = Users.userId, onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)
}