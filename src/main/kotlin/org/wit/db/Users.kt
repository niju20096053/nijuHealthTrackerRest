package org.wit.db

import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage one user.
//       Database wise, this is the table object.

object Users : Table("users") {
    val userId = integer("userId").autoIncrement().primaryKey()
    val firstName = varchar("firstName", 100)
    val lastName = varchar("lastName",100)
    val gender = varchar("gender",10)
    val email = varchar("email", 255)
    val mobile = varchar("mobile",15)
    val age = integer("age")
    val address = varchar("address", 100)
    val height = double("height")
    val weight = double("weight")
    val userName = varchar("userName",100).uniqueIndex()
    val password = varchar("password",255)
}