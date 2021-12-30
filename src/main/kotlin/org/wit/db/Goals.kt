package org.wit.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage goals of the users.
//       Database wise, this is the table object.

object Goals : Table("goals") {
    val goalId = integer("goalId").autoIncrement().primaryKey()
    val steps = integer("steps")
    val heartPoints = integer("heartPoints")
    val calories = integer("calories")
    val distance = integer("distance(km)")
    val water = integer("quantity(ml)")
    val sleep = integer("sleep(minutes)")
    val userId = integer("userId").references(ref = Users.userId, onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)
}