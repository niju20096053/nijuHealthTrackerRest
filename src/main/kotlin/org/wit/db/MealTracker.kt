package org.wit.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage meal of the users.
//       Database wise, this is the table object.

object MealTracker : Table("meals") {
    val mealId = integer("mealId").autoIncrement().primaryKey()
    val mealName = varchar("mealName", 100)
    val mealType = varchar("mealType",100)
    val dateTime = datetime("dateTime")
    val calories = double("calories")
    val quantity = double("quantity(g)")
    val userId = integer("userId").references(ref = Users.userId, onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)
}