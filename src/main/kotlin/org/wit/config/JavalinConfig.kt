package org.wit.config

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import org.wit.controllers.HealthTrackerAPI

class JavalinConfig {

    fun startJavalinService(): Javalin {

        val app = Javalin.create().apply {
            exception(Exception::class.java) { e, ctx -> e.printStackTrace() }
            error(404) { ctx -> ctx.json("404 - Not Found") }
        }.start(getHerokuAssignedPort())

        registerRoutes(app)
        return app
    }

    private fun getHerokuAssignedPort(): Int {
        val herokuPort = System.getenv("PORT")
        return if (herokuPort != null) {
            Integer.parseInt(herokuPort)
        } else 7000
    }

    private fun registerRoutes(app: Javalin) {
        app.routes {
            //Users
            get("/api/users", HealthTrackerAPI::getAllUsers)
            get("/api/users/:user-id", HealthTrackerAPI::getUserByUserId)
            post("/api/users", HealthTrackerAPI::addUser)
            get("/api/users/email/:email", HealthTrackerAPI::getUserByEmail)
            delete("/api/users/:user-id", HealthTrackerAPI::deleteUser)
            patch("/api/users/:user-id", HealthTrackerAPI::updateUser)

            //Activities
            get("/api/users/:user-id/activities", HealthTrackerAPI::getActivitiesByUserId)
            get("/api/activities/:activity-id", HealthTrackerAPI::getActivitiesByActivityId)
            get("/api/activities", HealthTrackerAPI::getAllActivities)
            post("/api/activities", HealthTrackerAPI::addActivity)
            delete("/api/activities/:activity-id", HealthTrackerAPI::deleteActivityByActivityId)
            patch( "/api/activities/:activity-id", HealthTrackerAPI::updateActivity)
            delete("/api/activities/users/:user-id", HealthTrackerAPI::deleteActivityByUserId)

            //Meals
            get("/api/meals/:meal-id", HealthTrackerAPI::getMealsByMealId)
            get("/api/users/:user-id/meals",HealthTrackerAPI::getMealsByUserId)
            get("/api/meals", HealthTrackerAPI::getAllMeals)
            post("/api/meals",HealthTrackerAPI::addMeal)
            delete("/api/meals/:meal-id",HealthTrackerAPI::deleteMealByMealId)
            patch("/api/meals/:meal-id",HealthTrackerAPI::updateMeal)
            delete("/api/meals/users/:user-id", HealthTrackerAPI::deleteMealsByUserId)

            //Sleeps
            get("/api/sleeps/:sleep-id", HealthTrackerAPI::getSleepsBySleepId)
            get("/api/users/:user-id/sleeps",HealthTrackerAPI::getSleepsByUserId)
            get("/api/sleeps", HealthTrackerAPI::getAllSleeps)
            post("/api/sleeps",HealthTrackerAPI::addSleep)
            delete("/api/sleeps/:sleep-id",HealthTrackerAPI::deleteSleepBySleepId)
            patch("/api/sleeps/:sleep-id",HealthTrackerAPI::updateSleep)
            delete("/api/sleeps/users/:user-id", HealthTrackerAPI::deleteSleepsByUserId)

            //Yoga
            get("/api/yogas/:yoga-id", HealthTrackerAPI::getYogasByYogaId)
            get("/api/users/:user-id/yogas",HealthTrackerAPI::getYogasByUserId)
            get("/api/yogas", HealthTrackerAPI::getAllYogas)
            post("/api/yogas",HealthTrackerAPI::addYoga)
            delete("/api/yogas/:yoga-id",HealthTrackerAPI::deleteYogaByYogaId)
            patch("/api/sleeps/:sleep-id",HealthTrackerAPI::updateSleep)
            delete("/api/yogas/users/:user-id", HealthTrackerAPI::deleteYogasByUserId)

            //Water
            get("/api/waters/:water-id", HealthTrackerAPI::getWaterByWaterId)
            get("/api/users/:user-id/waters",HealthTrackerAPI::getWatersByUserId)
            get("/api/waters", HealthTrackerAPI::getAllWaters)
            post("/api/waters",HealthTrackerAPI::addWater)
            delete("/api/waters/:water-id",HealthTrackerAPI::deleteWaterByWaterId)
            patch("/api/waters/:water-id",HealthTrackerAPI::updateWater)
            delete("/api/waters/users/:user-id", HealthTrackerAPI::deleteWatersByUserId)

            //Goals
            get("/api/goals/:goal-id", HealthTrackerAPI::getGoalByGoalId)
            get("/api/users/:user-id/goals",HealthTrackerAPI::getGoalsByUserId)
            get("/api/goals", HealthTrackerAPI::getAllGoals)
            post("/api/goals",HealthTrackerAPI::addGoal)
            delete("/api/goals/:goal-id",HealthTrackerAPI::deleteGoalByGoalId)
            patch("/api/goals/:goal-id",HealthTrackerAPI::updateGoal)
            delete("/api/goals/users/:user-id", HealthTrackerAPI::deleteGoalsByUserId)

        }
    }
}
