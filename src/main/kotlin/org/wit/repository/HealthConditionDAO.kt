package org.wit.repository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

import mapToHealthConditionsDTO
import org.wit.db.HealthConditionTracker
import org.wit.db.Users
import org.wit.domain.HealthConditionDTO
import org.wit.domain.UserDTO

class HealthConditionDAO {


    //Get all the healthConsition in the database regardless of user id
    fun getAll(): ArrayList<HealthConditionDTO> {
        val healthConsitionList: ArrayList<HealthConditionDTO> = arrayListOf()
        transaction {
            HealthConditionTracker.selectAll().map {
                healthConsitionList.add(mapToHealthConditionsDTO(it)) }
        }
        return healthConsitionList
    }

    //Find a specific healthConsition by healthConsition id
    fun findByHealthConditionId(healthConditionId: Int): HealthConditionDTO?{
        return transaction {
            HealthConditionTracker
                .select() { HealthConditionTracker.healthConditionId eq healthConditionId}
                .map{mapToHealthConditionsDTO(it)}
                .firstOrNull()
        }
    }

    //Find all healthConsitions for a specific user id
    fun findByUserId(userId: Int): List<HealthConditionDTO>{
        return transaction {
            HealthConditionTracker
                .select {HealthConditionTracker.userId eq userId}
                .map {mapToHealthConditionsDTO(it)}
        }
    }

    //Save a healthConsition to the database
    fun save(healthConsitionDTO: HealthConditionDTO){
        return transaction {
            HealthConditionTracker.insert {
                it[healthConditionId] = healthConsitionDTO.healthConditionId
                it[pulseRate] = healthConsitionDTO.pulseRate
                it[bloodPressure] = healthConsitionDTO.bloodPressure
                it[cholesterol] = healthConsitionDTO.cholesterol
                it[bloodSugar] = healthConsitionDTO.bloodSugar
                it[bmi] = healthConsitionDTO.bmi
                it[userId] = healthConsitionDTO.userId
            } get HealthConditionTracker.healthConditionId
        }
    }

    //Update healthConsition
    fun updateByHealthConditionId(healthConditionId : Int, healthConsitionDTO : HealthConditionDTO){
        return transaction {
            HealthConditionTracker.update ({
                HealthConditionTracker.healthConditionId eq healthConditionId}){
                it[pulseRate] = healthConsitionDTO.pulseRate
                it[bloodPressure] = healthConsitionDTO.bloodPressure
                it[cholesterol] = healthConsitionDTO.cholesterol
                it[bloodSugar] = healthConsitionDTO.bloodSugar
                it[bmi] = healthConsitionDTO.bmi
                it[userId] = healthConsitionDTO.userId
            }
        }
    }

    //delete healthConsition by healthConsition id
    fun deleteByHealthConditionId(healthConditionId : Int){
        transaction {
            HealthConditionTracker.deleteWhere{
                HealthConditionTracker.healthConditionId eq healthConditionId
            }
        }
    }

    //Empty all the rows in the healthConsitions table
    fun emptyHealthConditionTable(){
        return transaction {
            HealthConditionTracker.deleteAll()
        }
    }

    //delete activity by user id
    fun deleteByUserId(userId: Int){
        transaction {
            HealthConditionTracker.deleteWhere {
                HealthConditionTracker.userId eq userId
            }
        }
    }

}