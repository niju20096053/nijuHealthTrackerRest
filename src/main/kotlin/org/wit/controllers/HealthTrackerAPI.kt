package org.wit.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.javalin.http.Context
import org.wit.domain.ActivityDTO
import org.wit.domain.MealDTO
import org.wit.domain.UserDTO
import org.wit.repository.ActivityDAO
import org.wit.repository.MealDAO
import org.wit.repository.UserDAO
import org.wit.utilities.jsonToObject

// SRP - Responsibility of this API is to manage IO between the DAOs and JSON context

object HealthTrackerAPI {


    private val userDao = UserDAO()
    private val activityDAO = ActivityDAO()
    private val mealDAO = MealDAO()

    //--------------------------------------------------------------
    // UserDAO specifics
    //-------------------------------------------------------------
    fun getAllUsers(ctx: Context) {
        val users = userDao.getAll()
        if (users.size != 0) {
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
        ctx.json(users)
    }

    fun getUserByUserId(ctx: Context) {
        val user = userDao.findById(ctx.pathParam("user-id").toInt())
        if (user != null) {
            ctx.json(user)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    fun getUserByEmail(ctx: Context) {
        println("Context: $ctx")
        println("Email in path param : ${ctx.pathParam("email")}")
        val user = userDao.findByEmail(ctx.pathParam("email"))
        if (user != null) {
            ctx.json(user)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    fun addUser(ctx: Context) {
        val user : UserDTO = jsonToObject(ctx.body())
        println("Ctx in adduser: ${ctx.body()}")
        val userId = userDao.save(user)//.toString().toInt()
        if (userId.toString().toInt() != 0) {
            user.userId = userId.toString().toInt()
            ctx.json(user)
            ctx.status(201)
        }
    }

    fun deleteUser(ctx: Context){
        val id =ctx.pathParam("user-id").toInt()
        if (id != 0) {
            val returnVal = userDao.delete(id)
            if(returnVal != null){
                ctx.status(204)
            }
        }else
            ctx.status(404)
    }

    fun updateUser(ctx: Context){
        val user : UserDTO = jsonToObject(ctx.body())
        if ((userDao.update(id = ctx.pathParam("user-id").toInt(), userDTO=user)) != null)
            ctx.status(204)
        else
            ctx.status(404)
    }

    //--------------------------------------------------------------
    // ActivityDAO specifics
    //-------------------------------------------------------------

    fun getAllActivities(ctx: Context) {
        ctx.json(activityDAO.getAll())
    }

    fun getActivitiesByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val activities = activityDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (activities.size > 0) {
                ctx.json(activities)
                ctx.status(200)
            }
            else{
                ctx.status(404)
            }
        }
        else{
            ctx.status(404)
        }
    }

    fun getActivitiesByActivityId(ctx: Context) {
        val activity = activityDAO.findByActivityId((ctx.pathParam("activity-id").toInt()))
        if (activity != null){
            ctx.json(activity)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    fun addActivity(ctx: Context) {
        val activityDTO : ActivityDTO = jsonToObject(ctx.body())
        val userId = userDao.findById(activityDTO.userId)
        if (userId != null) {
            val activityId = (activityDAO.save(activityDTO)).toString().toInt()
            if (activityId != 0) {
                activityDTO.activityId = activityId
                ctx.json(activityDTO)
                ctx.status(201)
            }
        }
        else{
            ctx.status(404)
        }
    }

    fun deleteActivityByActivityId(ctx: Context){
        var id = ctx.pathParam("activity-id").toInt()
        if (id != 0) {
            activityDAO.deleteByActivityId(id)
            ctx.status(204)
        }else
            ctx.status(404)
    }

    fun deleteActivityByUserId(ctx: Context){
        var id = ctx.pathParam("user-id").toInt()
        if (id != 0) {
            activityDAO.deleteByUserId(id)
            ctx.status(204)
        }else
            ctx.status(404)
    }

    fun updateActivity(ctx: Context){
        val activity : ActivityDTO = jsonToObject(ctx.body())
        var activityId = ctx.pathParam("activity-id").toInt()
        if (activityId  != 0){
            activityDAO.updateByActivityId(activityId , activity)
            ctx.status(204)
        }
        else
            ctx.status(404)
    }


    //--------------------------------------------------------------
    // MealDAO specifics
    //-------------------------------------------------------------

    fun getAllMeals(ctx: Context) {
        ctx.json(mealDAO.getAll())
    }

    fun getMealsByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val meals = mealDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (meals.size > 0) {
                ctx.json(meals)
                ctx.status(200)
            }
            else{
                ctx.status(404)
            }
        }
        else{
            ctx.status(404)
        }
    }

    fun getMealsByMealId(ctx: Context) {
        val meal = mealDAO.findByMealId((ctx.pathParam("meal-id").toInt()))
        if (meal != null){
            ctx.json(meal)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    fun addMeal(ctx: Context) {
        val mealDTO : MealDTO = jsonToObject(ctx.body())
        val userId = userDao.findById(mealDTO.userId)
        if (userId != null) {
            val mealId = (mealDAO.save(mealDTO)).toString().toInt()
            if (mealId != 0) {
                mealDTO.mealId = mealId
                ctx.json(mealDTO)
                ctx.status(201)
            }
        }
        else{
            ctx.status(404)
        }
    }

    fun deleteMealByMealId(ctx: Context){
        var id = ctx.pathParam("meal-id").toInt()
        if (id != 0) {
            mealDAO.deleteByMealId(id)
            ctx.status(204)
        }else
            ctx.status(404)
    }

    fun deleteMealsByUserId(ctx: Context){
        var id = ctx.pathParam("user-id").toInt()
        if (id != 0) {
            mealDAO.deleteByUserId(id)
            ctx.status(204)
        }else
            ctx.status(404)
    }

    fun updateMeal(ctx: Context){
        val meal : MealDTO = jsonToObject(ctx.body())
        var mealId = ctx.pathParam("meal-id").toInt()
        if (mealId  != 0){
            mealDAO.updateByMealId(mealId , meal)
            ctx.status(204)
        }
        else
            ctx.status(404)
    }
}
