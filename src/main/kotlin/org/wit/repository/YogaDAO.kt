package org.wit.repository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

import mapToYogaDTO
import org.wit.db.YogaTracker
import org.wit.db.Users
import org.wit.domain.YogaDTO
import org.wit.domain.UserDTO

class YogaDAO {


    //Get all the yoga in the database regardless of user id
    fun getAll(): ArrayList<YogaDTO> {
        val yogaList: ArrayList<YogaDTO> = arrayListOf()
        transaction {
            YogaTracker.selectAll().map {
                yogaList.add(mapToYogaDTO(it)) }
        }
        return yogaList
    }

    //Find a specific yoga by yoga id
    fun findByYogaId(yogaId: Int): YogaDTO?{
        return transaction {
            YogaTracker
                .select() { YogaTracker.yogaId eq yogaId}
                .map{mapToYogaDTO(it)}
                .firstOrNull()
        }
    }

    //Find all yogas for a specific user id
    fun findByUserId(userId: Int): List<YogaDTO>{
        return transaction {
            YogaTracker
                .select {YogaTracker.userId eq userId}
                .map {mapToYogaDTO(it)}
        }
    }

    //Save a yoga to the database
    fun save(yogaDTO: YogaDTO){
        return transaction {
            YogaTracker.insert {
                it[yogaId] = yogaDTO.yogaId
                it[yogaName] = yogaDTO.yogaName
                it[yogaStart] = yogaDTO.yogaStart
                it[yogaEnd] = yogaDTO.yogaEnd
                it[yogaDurationInMinutes] = yogaDTO.yogaDurationInMinutes
                it[userId] = yogaDTO.userId
            } get YogaTracker.yogaId
        }
    }

    //Update yoga
    fun updateByYogaId(yogaId : Int, yogaDTO : YogaDTO){
        return transaction {
            YogaTracker.update ({
                YogaTracker.yogaId eq yogaId}){
                it[yogaName] = yogaDTO.yogaName
                it[yogaStart] = yogaDTO.yogaStart
                it[yogaEnd] = yogaDTO.yogaEnd
                it[yogaDurationInMinutes] = yogaDTO.yogaDurationInMinutes
                it[userId] = yogaDTO.userId
            }
        }
    }

    //delete yoga by yoga id
    fun deleteByYogaId(yogaId : Int){
        transaction {
            YogaTracker.deleteWhere{
                YogaTracker.yogaId eq yogaId
            }
        }
    }

    //Empty all the rows in the yogas table
    fun emptyYogasTable(){
        return transaction {
            YogaTracker.deleteAll()
        }
    }

    //delete activity by user id
    fun deleteByUserId(userId: Int){
        transaction {
            YogaTracker.deleteWhere {
                YogaTracker.userId eq userId
            }
        }
    }

}