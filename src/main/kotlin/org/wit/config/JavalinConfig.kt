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

            //Meals
            get("/api/meals/:meal-id", HealthTrackerAPI::getMealsByMealId)
            get("/api/users/:user-id/meals",HealthTrackerAPI::getMealsByUserId)
            get("/api/meals", HealthTrackerAPI::getAllMeals)
            post("/api/meals",HealthTrackerAPI::addMeal)
            delete("/api/meals/:meal-id",HealthTrackerAPI::deleteMealByMealId)
            patch("/api/meals/:meal-id",HealthTrackerAPI::updateMeal)
        }
    }
}
