package org.wit.repository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

import mapToSleepDTO
import org.wit.db.SleepTracker
import org.wit.db.Users
import org.wit.domain.SleepDTO
import org.wit.domain.UserDTO

class SleepDAO {


    //Get all the sleep in the database regardless of user id
    fun getAll(): ArrayList<SleepDTO> {
        val sleepList: ArrayList<SleepDTO> = arrayListOf()
        transaction {
            SleepTracker.selectAll().map {
                sleepList.add(mapToSleepDTO(it)) }
        }
        return sleepList
    }

    //Find a specific sleep by sleep id
    fun findBySleepId(sleepTimeId: Int): SleepDTO?{
        return transaction {
            SleepTracker
                .select() { SleepTracker.sleepTimeId eq sleepTimeId}
                .map{mapToSleepDTO(it)}
                .firstOrNull()
        }
    }

    //Find all sleeps for a specific user id
    fun findByUserId(userId: Int): List<SleepDTO>{
        return transaction {
            SleepTracker
                .select {SleepTracker.userId eq userId}
                .map {mapToSleepDTO(it)}
        }
    }

    //Save a sleep to the database
    fun save(sleepDTO: SleepDTO){
        return transaction {
            SleepTracker.insert {
                it[sleepTimeId] = sleepDTO.sleepTimeId
                it[sleepStart] = sleepDTO.sleepStart
                it[sleepEnd] = sleepDTO.sleepEnd
                it[sleepTimeInMinutes] = sleepDTO.sleepTimeInMinutes
                it[userId] = sleepDTO.userId
            } get SleepTracker.sleepTimeId
        }
    }

    //Update sleep
    fun updateBySleepId(sleepTimeId : Int, sleepDTO : SleepDTO){
        return transaction {
            SleepTracker.update ({
                SleepTracker.sleepTimeId eq sleepTimeId}){
                it[sleepStart] = sleepDTO.sleepStart
                it[sleepEnd] = sleepDTO.sleepEnd
                it[sleepTimeInMinutes] = sleepDTO.sleepTimeInMinutes
                it[userId] = sleepDTO.userId
            }
        }
    }

    //delete sleep by sleep id
    fun deleteBySleepId(sleepTimeId : Int){
        transaction {
            SleepTracker.deleteWhere{
                SleepTracker.sleepTimeId eq sleepTimeId
            }
        }
    }

    //Empty all the rows in the sleeps table
    fun emptySleepsTable(){
        return transaction {
            SleepTracker.deleteAll()
        }
    }

    //delete activity by user id
    fun deleteByUserId(userId: Int){
        transaction {
            SleepTracker.deleteWhere {
                SleepTracker.userId eq userId
            }
        }
    }

}