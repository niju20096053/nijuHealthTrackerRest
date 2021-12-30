package org.wit.repository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

import mapToMealDTO
import org.wit.db.MealTracker
import org.wit.db.Users
import org.wit.domain.MealDTO
import org.wit.domain.UserDTO

class MealDAO {


    //Get all the meal in the database regardless of user id
    fun getAll(): ArrayList<MealDTO> {
        val mealList: ArrayList<MealDTO> = arrayListOf()
        transaction {
            MealTracker.selectAll().map {
                mealList.add(mapToMealDTO(it)) }
        }
        return mealList
    }

    //Find a specific meal by meal id
    fun findByMealId(mealId: Int): MealDTO?{
        return transaction {
            MealTracker
                .select() { MealTracker.mealId eq mealId}
                .map{mapToMealDTO(it)}
                .firstOrNull()
        }
    }

    //Find all meals for a specific user id
    fun findByUserId(userId: Int): List<MealDTO>{
        return transaction {
            MealTracker
                .select {MealTracker.userId eq userId}
                .map {mapToMealDTO(it)}
        }
    }

    //Save a meal to the database
    fun save(mealDTO: MealDTO){
        return transaction {
            MealTracker.insert {
                it[mealId] = mealDTO.mealId
                it[mealName] = mealDTO.mealName
                it[mealType] = mealDTO.mealType
                it[dateTime] = mealDTO.dateTime
                it[calories] = mealDTO.calories
                it[quantity] = mealDTO.quantity
                it[userId] = mealDTO.userId
            } get MealTracker.mealId
        }
    }

    //Update meal
    fun updateByMealId(mealId : Int, mealDTO : MealDTO){
        return transaction {
            MealTracker.update ({
                MealTracker.mealId eq mealId}){
                it[mealName] = mealDTO.mealName
                it[mealType] = mealDTO.mealType
                it[dateTime] = mealDTO.dateTime
                it[calories] = mealDTO.calories
                it[quantity] = mealDTO.quantity
                it[userId] = mealDTO.userId
            }
        }
    }

    //delete meal by meal id
    fun deleteByMealId(mealId : Int){
        transaction {
            MealTracker.deleteWhere{
                MealTracker.mealId eq mealId
            }
        }
    }

    //Empty all the rows in the meals table
    fun emptyMealsTable(){
        return transaction {
            MealTracker.deleteAll()
        }
    }

    //delete activity by user id
    fun deleteByUserId(userId: Int){
        transaction {
            MealTracker.deleteWhere {
                MealTracker.userId eq userId
            }
        }
    }

}