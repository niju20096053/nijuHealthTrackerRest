package org.wit.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage yoga of the users.
//       Database wise, this is the table object.

object YogaTracker : Table("yoga") {
    val yogaId = integer("yogaId").autoIncrement().primaryKey()
    val yogaName = varchar("yogaName", 100)
    val yogaStart = datetime("yogaStart")
    val yogaEnd = datetime("yogaEnd")
    val yogaDurationInMinutes = integer("yogaDurationInMinutes")
    val userId = integer("userId").references(ref = Users.userId, onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)
}