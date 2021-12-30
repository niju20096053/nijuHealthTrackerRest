package org.wit.repository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

import mapToWaterDTO
import org.wit.db.WaterTracker
import org.wit.db.Users
import org.wit.domain.WaterDTO
import org.wit.domain.UserDTO

class WaterDAO {


    //Get all the water in the database regardless of user id
    fun getAll(): ArrayList<WaterDTO> {
        val waterList: ArrayList<WaterDTO> = arrayListOf()
        transaction {
            WaterTracker.selectAll().map {
                waterList.add(mapToWaterDTO(it)) }
        }
        return waterList
    }

    //Find a specific water by water id
    fun findByWaterId(waterId: Int): WaterDTO?{
        return transaction {
            WaterTracker
                .select() { WaterTracker.waterId eq waterId}
                .map{mapToWaterDTO(it)}
                .firstOrNull()
        }
    }

    //Find all waters for a specific user id
    fun findByUserId(userId: Int): List<WaterDTO>{
        return transaction {
            WaterTracker
                .select {WaterTracker.userId eq userId}
                .map {mapToWaterDTO(it)}
        }
    }

    //Save a water to the database
    fun save(waterDTO: WaterDTO){
        return transaction {
            WaterTracker.insert {
                it[waterId] = waterDTO.waterId
                it[dateTimeOfDrinking] = waterDTO.dateTimeOfDrinking
                it[quantity] = waterDTO.quantity
                it[userId] = waterDTO.userId
            } get WaterTracker.waterId
        }
    }

    //Update water
    fun updateByWaterId(waterId : Int, waterDTO : WaterDTO){
        return transaction {
            WaterTracker.update ({
                WaterTracker.waterId eq waterId}){
                it[dateTimeOfDrinking] = waterDTO.dateTimeOfDrinking
                it[quantity] = waterDTO.quantity
                it[userId] = waterDTO.userId
            }
        }
    }

    //delete water by water id
    fun deleteByWaterId(waterId : Int){
        transaction {
            WaterTracker.deleteWhere{
                WaterTracker.waterId eq waterId
            }
        }
    }

    //Empty all the rows in the waters table
    fun emptyWatersTable(){
        return transaction {
            WaterTracker.deleteAll()
        }
    }

    //delete activity by user id
    fun deleteByUserId(userId: Int){
        transaction {
            WaterTracker.deleteWhere {
                WaterTracker.userId eq userId
            }
        }
    }

}