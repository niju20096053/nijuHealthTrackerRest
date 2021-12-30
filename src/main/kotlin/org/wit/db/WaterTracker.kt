package org.wit.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage yoga of the users.
//       Database wise, this is the table object.

object WaterTracker : Table("water") {
    val waterId = integer("waterId").autoIncrement().primaryKey()
    val dateTimeOfDrinking = datetime("dateTimeOfDrinking")
    val quantity = integer("quantity(ml)")
    val userId = integer("userId").references(ref = Users.userId, onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)
}