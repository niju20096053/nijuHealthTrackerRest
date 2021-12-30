package org.wit.repository

import mapToUserDTO
import org.jetbrains.exposed.sql.*
import org.wit.domain.UserDTO
import org.jetbrains.exposed.sql.transactions.transaction
import org.wit.db.Users

class UserDAO {

    fun getAll() : ArrayList<UserDTO>{
        val userList: ArrayList<UserDTO> = arrayListOf()
        transaction {
            Users.selectAll().map {
                userList.add(mapToUserDTO(it)) }
        }
        return userList
    }

    fun findById(id: Int): UserDTO?{
        return transaction {
            Users.select() {
                Users.userId eq id}
                .map{mapToUserDTO(it)}
                .firstOrNull()
        }
    }

    fun save(userDTO: UserDTO){
        return transaction {
            Users.insert {
                it[userId] = userDTO.userId
                it[firstName] = userDTO.firstName
                it[lastName] = userDTO.lastName
                it[gender] = userDTO.gender
                it[email] = userDTO.email
                it[mobile] = userDTO.mobile
                it[age] = userDTO.age
                it[address] = userDTO.address
                it[height] = userDTO.height
                it[weight] = userDTO.weight
                it[userName] = userDTO.userName
                it[password] = userDTO.password
            } get Users.userId
            println("UserId in save: "+Users.userId)
        }
    }

    fun findByEmail(email: String) :UserDTO?{
        println("Email in findByEmail: $email")
        return transaction {
            Users.select() {
                Users.email eq email}
                .map{mapToUserDTO(it)}
                .firstOrNull()
        }
    }

    fun emptyUserTable(){
        return transaction {
            Users.deleteAll()
        }
    }

    fun delete(id: Int){
        return transaction {
            Users.deleteWhere {
                Users.userId eq id
            }
        }
    }

    fun deleteByEmail(email: String){
        return transaction {
            Users.deleteWhere {
                Users.email eq email
            }
        }
    }

    fun update(id: Int, userDTO: UserDTO){
        return transaction {
            Users.update({
                Users.userId eq id}){
                it[firstName] = userDTO.firstName
                it[lastName] = userDTO.lastName
                it[gender] = userDTO.gender
                it[email] = userDTO.email
                it[mobile] = userDTO.mobile
                it[age] = userDTO.age
                it[address] = userDTO.address
                it[height] = userDTO.height
                it[weight] = userDTO.weight
                it[userName] = userDTO.userName
                it[password] = userDTO.password
            }
            }
            }
        }


