package org.wit.repository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

import mapToGoalsDTO
import org.wit.db.Goals
import org.wit.db.Users
import org.wit.domain.GoalsDTO
import org.wit.domain.UserDTO

class GoalsDAO {


    //Get all the goals in the database regardless of user id
    fun getAll(): ArrayList<GoalsDTO> {
        val goalsList: ArrayList<GoalsDTO> = arrayListOf()
        transaction {
            Goals.selectAll().map {
                goalsList.add(mapToGoalsDTO(it)) }
        }
        return goalsList
    }

    //Find a specific goals by goals id
    fun findByGoalsId(goalId: Int): GoalsDTO?{
        return transaction {
            Goals
                .select() { Goals.goalId eq goalId}
                .map{mapToGoalsDTO(it)}
                .firstOrNull()
        }
    }

    //Find all goalss for a specific user id
    fun findByUserId(userId: Int): List<GoalsDTO>{
        return transaction {
            Goals
                .select {Goals.userId eq userId}
                .map {mapToGoalsDTO(it)}
        }
    }

    //Save a goals to the database
    fun save(goalsDTO: GoalsDTO){
        return transaction {
            Goals.insert {
                it[goalId] = goalsDTO.goalId
                it[steps] = goalsDTO.steps
                it[heartPoints] = goalsDTO.heartPoints
                it[calories] = goalsDTO.calories
                it[distance] = goalsDTO.distance
                it[water] = goalsDTO.water
                it[sleep] = goalsDTO.sleep
                it[userId] = goalsDTO.userId
            } get Goals.goalId
        }
    }

    //Update goals
    fun updateByGoalsId(goalId : Int, goalsDTO : GoalsDTO){
        return transaction {
            Goals.update ({
                Goals.goalId eq goalId}){
                it[steps] = goalsDTO.steps
                it[heartPoints] = goalsDTO.heartPoints
                it[calories] = goalsDTO.calories
                it[distance] = goalsDTO.distance
                it[water] = goalsDTO.water
                it[sleep] = goalsDTO.sleep
                it[userId] = goalsDTO.userId
            }
        }
    }

    //delete goals by goals id
    fun deleteByGoalsId(goalId : Int){
        transaction {
            Goals.deleteWhere{
                Goals.goalId eq goalId
            }
        }
    }

    //Empty all the rows in the goalss table
    fun emptyGoalsTable(){
        return transaction {
            Goals.deleteAll()
        }
    }

    //delete activity by user id
    fun deleteByUserId(userId: Int){
        transaction {
            Goals.deleteWhere {
                Goals.userId eq userId
            }
        }
    }

}