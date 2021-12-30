package org.wit.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.javalin.http.Context
import org.wit.domain.*
import org.wit.repository.*
import org.wit.utilities.jsonToObject

// SRP - Responsibility of this API is to manage IO between the DAOs and JSON context

object HealthTrackerAPI {


    private val userDao = UserDAO()
    private val activityDAO = ActivityDAO()
    private val mealDAO = MealDAO()
    private val sleepDAO = SleepDAO()
    private val yogaDAO = YogaDAO()
    private val waterDAO = WaterDAO()
    private val goalsDAO = GoalsDAO()


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


    //--------------------------------------------------------------
    // SleepDAO specifics
    //-------------------------------------------------------------

    fun getAllSleeps(ctx: Context) {
        ctx.json(sleepDAO.getAll())
    }

    fun getSleepsByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val sleeps = sleepDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (sleeps.size > 0) {
                ctx.json(sleeps)
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

    fun getSleepsBySleepId(ctx: Context) {
        val sleep = sleepDAO.findBySleepId((ctx.pathParam("sleep-id").toInt()))
        if (sleep != null){
            ctx.json(sleep)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    fun addSleep(ctx: Context) {
        val sleepDTO : SleepDTO = jsonToObject(ctx.body())
        val userId = userDao.findById(sleepDTO.userId)
        if (userId != null) {
            val sleepTimeId = (sleepDAO.save(sleepDTO)).toString().toInt()
            if (sleepTimeId != 0) {
                sleepDTO.sleepTimeId = sleepTimeId
                ctx.json(sleepDTO)
                ctx.status(201)
            }
        }
        else{
            ctx.status(404)
        }
    }

    fun deleteSleepBySleepId(ctx: Context){
        var id = ctx.pathParam("sleep-id").toInt()
        if (id != 0) {
            sleepDAO.deleteBySleepId(id)
            ctx.status(204)
        }else
            ctx.status(404)
    }

    fun deleteSleepsByUserId(ctx: Context){
        var id = ctx.pathParam("user-id").toInt()
        if (id != 0) {
            sleepDAO.deleteByUserId(id)
            ctx.status(204)
        }else
            ctx.status(404)
    }

    fun updateSleep(ctx: Context){
        val sleep : SleepDTO = jsonToObject(ctx.body())
        var sleepId = ctx.pathParam("sleep-id").toInt()
        if (sleepId  != 0){
            sleepDAO.updateBySleepId(sleepId , sleep)
            ctx.status(204)
        }
        else
            ctx.status(404)
    }


    //--------------------------------------------------------------
    // YogaDAO specifics
    //-------------------------------------------------------------

    fun getAllYogas(ctx: Context) {
        ctx.json(yogaDAO.getAll())
    }

    fun getYogasByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val yogas = yogaDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (yogas.size > 0) {
                ctx.json(yogas)
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

    fun getYogasByYogaId(ctx: Context) {
        val yogas = yogaDAO.findByYogaId((ctx.pathParam("yoga-id").toInt()))
        if (yogas != null){
            ctx.json(yogas)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    fun addYoga(ctx: Context) {
        val yogaDTO : YogaDTO = jsonToObject(ctx.body())
        val userId = userDao.findById(yogaDTO.userId)
        if (userId != null) {
            val yogaId = (yogaDAO.save(yogaDTO)).toString().toInt()
            if (yogaId != 0) {
                yogaDTO.yogaId = yogaId
                ctx.json(yogaDTO)
                ctx.status(201)
            }
        }
        else{
            ctx.status(404)
        }
    }

    fun deleteYogaByYogaId(ctx: Context){
        var id = ctx.pathParam("yoga-id").toInt()
        if (id != 0) {
            yogaDAO.deleteByYogaId(id)
            ctx.status(204)
        }else
            ctx.status(404)
    }

    fun deleteYogasByUserId(ctx: Context){
        var id = ctx.pathParam("user-id").toInt()
        if (id != 0) {
            yogaDAO.deleteByUserId(id)
            ctx.status(204)
        }else
            ctx.status(404)
    }

    fun updateYoga(ctx: Context){
        val yoga : YogaDTO = jsonToObject(ctx.body())
        var yogaId = ctx.pathParam("yoga-id").toInt()
        if (yogaId  != 0){
            yogaDAO.updateByYogaId(yogaId , yoga)
            ctx.status(204)
        }
        else
            ctx.status(404)
    }


    //--------------------------------------------------------------
    // WaterDAO specifics
    //-------------------------------------------------------------

    fun getAllWaters(ctx: Context) {
        ctx.json(waterDAO.getAll())
    }

    fun getWatersByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val waters = waterDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (waters.size > 0) {
                ctx.json(waters)
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

    fun getWaterByWaterId(ctx: Context) {
        val waters = waterDAO.findByWaterId((ctx.pathParam("water-id").toInt()))
        if (waters != null){
            ctx.json(waters)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    fun addWater(ctx: Context) {
        val waterDTO : WaterDTO = jsonToObject(ctx.body())
        val userId = userDao.findById(waterDTO.userId)
        if (userId != null) {
            val waterId = (waterDAO.save(waterDTO)).toString().toInt()
            if (waterId != 0) {
                waterDTO.waterId = waterId
                ctx.json(waterDTO)
                ctx.status(201)
            }
        }
        else{
            ctx.status(404)
        }
    }

    fun deleteWaterByWaterId(ctx: Context){
        var id = ctx.pathParam("water-id").toInt()
        if (id != 0) {
            waterDAO.deleteByWaterId(id)
            ctx.status(204)
        }else
            ctx.status(404)
    }

    fun deleteWatersByUserId(ctx: Context){
        var id = ctx.pathParam("user-id").toInt()
        if (id != 0) {
            waterDAO.deleteByUserId(id)
            ctx.status(204)
        }else
            ctx.status(404)
    }

    fun updateWater(ctx: Context){
        val water : WaterDTO = jsonToObject(ctx.body())
        var waterId = ctx.pathParam("water-id").toInt()
        if (waterId  != 0){
            waterDAO.updateByWaterId(waterId , water)
            ctx.status(204)
        }
        else
            ctx.status(404)
    }


    //--------------------------------------------------------------
    // GoalsDAO specifics
    //-------------------------------------------------------------

    fun getAllGoals(ctx: Context) {
        ctx.json(goalsDAO.getAll())
    }

    fun getGoalsByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val goals = goalsDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (goals.size > 0) {
                ctx.json(goals)
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

    fun getGoalByGoalId(ctx: Context) {
        val goals = goalsDAO.findByGoalsId((ctx.pathParam("goal-id").toInt()))
        if (goals != null){
            ctx.json(goals)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    fun addGoal(ctx: Context) {
        val goalsDTO : GoalsDTO = jsonToObject(ctx.body())
        val userId = userDao.findById(goalsDTO.userId)
        if (userId != null) {
            val goalId = (goalsDAO.save(goalsDTO)).toString().toInt()
            if (goalId != 0) {
                goalsDTO.goalId = goalId
                ctx.json(goalsDTO)
                ctx.status(201)
            }
        }
        else{
            ctx.status(404)
        }
    }

    fun deleteGoalByGoalId(ctx: Context){
        var id = ctx.pathParam("goal-id").toInt()
        if (id != 0) {
            goalsDAO.deleteByGoalsId(id)
            ctx.status(204)
        }else
            ctx.status(404)
    }

    fun deleteGoalsByUserId(ctx: Context){
        var id = ctx.pathParam("goal-id").toInt()
        if (id != 0) {
            goalsDAO.deleteByUserId(id)
            ctx.status(204)
        }else
            ctx.status(404)
    }

    fun updateGoal(ctx: Context){
        val goal : GoalsDTO = jsonToObject(ctx.body())
        var goalId = ctx.pathParam("goal-id").toInt()
        if (goalId  != 0){
            goalsDAO.updateByGoalsId(goalId , goal)
            ctx.status(204)
        }
        else
            ctx.status(404)
    }

}
