package org.wit.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage health condition tracker of the users.
//       Database wise, this is the table object.

object HealthConditionTracker : Table("healthConditions") {
    val healthConditionId = integer("healthConditionId").autoIncrement().primaryKey()
    val pulseRate = integer("pulseRate")
    val bloodPressure = varchar("bloodPressure",10)
    val cholesterol = integer("cholesterol")
    val bloodSugar = integer("bloodSugar")
    val bmi = double("BMI")
    val userId = integer("userId").references(ref = Users.userId, onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)
}