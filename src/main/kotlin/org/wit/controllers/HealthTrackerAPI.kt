package org.wit.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.javalin.http.Context
import org.wit.repository.UserDAO
import org.wit.domain.UserDTO



object HealthTrackerAPI {

    private val userDao = UserDAO()

    fun getAllUsers(ctx: Context) {
        ctx.json(userDao.getAll())
    }

    fun getUserByUserId(ctx: Context) {
        val user = userDao.findById(ctx.pathParam("user-id").toInt())
        if (user != null) {
            ctx.json(user)
        }
    }

    fun addUser(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val user = mapper.readValue<UserDTO>(ctx.body())
        userDao.save(user)
        ctx.json(user)
    }

    fun getUserByEmail(ctx: Context){
        val user = userDao.findByEmail(ctx.pathParam("email").toString())
        if (user != null) {
            ctx.json(user)
        }
    }

    fun deleteUser(ctx: Context){
        val user = userDao.findById(ctx.pathParam("user-id").toInt())
        if (user != null) {
            userDao.delete(ctx.pathParam("user-id").toInt())
        }
    }

    fun updateUser(ctx: Context){
        val user = userDao.findById(ctx.pathParam("user-id").toInt())
        val mapper = jacksonObjectMapper()
        val updatedUser = mapper.readValue<UserDTO>(ctx.body())
        if (user != null) {
            userDao.update(ctx.pathParam("user-id").toInt(), updatedUser)
        }
    }


}