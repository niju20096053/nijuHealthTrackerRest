package org.wit.repository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.wit.db.Activities
import org.wit.domain.ActivityDTO
import mapToActivityDTO
import org.wit.db.Users
import org.wit.domain.UserDTO

class ActivityDAO {


    //Get all the activities in the database regardless of user id
    fun getAll(): ArrayList<ActivityDTO> {
        val activitiesList: ArrayList<ActivityDTO> = arrayListOf()
        transaction {
            Activities.selectAll().map {
                activitiesList.add(mapToActivityDTO(it)) }
        }
        return activitiesList
    }

    //Find a specific activity by activity id
    fun findByActivityId(activityId: Int): ActivityDTO?{
        return transaction {
            Activities
                .select() { Activities.activityId eq activityId}
                .map{mapToActivityDTO(it)}
                .firstOrNull()
        }
    }

    //Find all activities for a specific user id
    fun findByUserId(userId: Int): List<ActivityDTO>{
        return transaction {
            Activities
                .select {Activities.userId eq userId}
                .map {mapToActivityDTO(it)}
        }
    }

    //Save an activity to the database
    fun save(activityDTO: ActivityDTO){
        return transaction {
            Activities.insert {
                it[activityId] = activityDTO.activityId
                it[description] = activityDTO.description
                it[duration] = activityDTO.duration
                it[started] = activityDTO.started
                it[calories] = activityDTO.calories
                it[userId] = activityDTO.userId
            } get Activities.activityId
        }
    }

    //Update activity
    fun updateByActivityId(activityId : Int, activityDTO : ActivityDTO){
        return transaction {
            Activities.update ({
                Activities.activityId eq activityId}){
                it[description] = activityDTO.description
                it[duration] = activityDTO.duration
                it[started] = activityDTO.started
                it[calories] = activityDTO.calories
                it[userId] = activityDTO.userId
            }
        }
    }

    //delete activity by activity id
    fun deleteByActivityId(activityId : Int){
        transaction {
            Activities.deleteWhere{
                Activities.activityId eq activityId
            }
        }
    }

    //Empty all the rows in the activity table
    fun emptyActivityTable(){
        return transaction {
            Activities.deleteAll()
        }
    }

    //delete activity by user id
    fun deleteByUserId(userId: Int){
        transaction {
            Activities.deleteWhere {
                Activities.userId eq userId
            }
        }
    }

}