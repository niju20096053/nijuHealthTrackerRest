package org.wit.config

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.plugin.rendering.vue.VueComponent
import org.wit.controllers.HealthTrackerAPI

class JavalinConfig {

    fun startJavalinService(): Javalin {

        val app = Javalin.create { config ->
            config.enableWebjars()
        }.start(getHerokuAssignedPort())

        registerRoutes(app)
        return app
    }

    private fun getHerokuAssignedPort(): Int {
        val herokuPort = System.getenv("PORT")
        return if (herokuPort != null) {
            Integer.parseInt(herokuPort)
        } else 7001
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
            delete("/api/users/email/:email", HealthTrackerAPI::deleteUserByEmail)

            //Activities
            get("/api/users/:user-id/activities", HealthTrackerAPI::getActivitiesByUserId)
            get("/api/activities/:activity-id", HealthTrackerAPI::getActivitiesByActivityId)
            get("/api/activities", HealthTrackerAPI::getAllActivities)
            post("/api/activities", HealthTrackerAPI::addActivity)
            delete("/api/activities/:activity-id", HealthTrackerAPI::deleteActivityByActivityId)
            patch( "/api/activities/:activity-id", HealthTrackerAPI::updateActivity)
            delete("/api/users/:user-id/activities", HealthTrackerAPI::deleteActivityByUserId)

            //Meals
            get("/api/meals/:meal-id", HealthTrackerAPI::getMealsByMealId)
            get("/api/users/:user-id/meals",HealthTrackerAPI::getMealsByUserId)
            get("/api/meals", HealthTrackerAPI::getAllMeals)
            post("/api/meals",HealthTrackerAPI::addMeal)
            delete("/api/meals/:meal-id",HealthTrackerAPI::deleteMealByMealId)
            patch("/api/meals/:meal-id",HealthTrackerAPI::updateMeal)
            delete("/api/users/:user-id/meals", HealthTrackerAPI::deleteMealsByUserId)

            //Sleeps
            get("/api/sleeps/:sleep-id", HealthTrackerAPI::getSleepsBySleepId)
            get("/api/users/:user-id/sleeps",HealthTrackerAPI::getSleepsByUserId)
            get("/api/sleeps", HealthTrackerAPI::getAllSleeps)
            post("/api/sleeps",HealthTrackerAPI::addSleep)
            delete("/api/sleeps/:sleep-id",HealthTrackerAPI::deleteSleepBySleepId)
            patch("/api/sleeps/:sleep-id",HealthTrackerAPI::updateSleep)
            delete("/api/users/:user-id/sleeps", HealthTrackerAPI::deleteSleepsByUserId)

            //Yoga
            get("/api/yogas/:yoga-id", HealthTrackerAPI::getYogasByYogaId)
            get("/api/users/:user-id/yogas",HealthTrackerAPI::getYogasByUserId)
            get("/api/yogas", HealthTrackerAPI::getAllYogas)
            post("/api/yogas",HealthTrackerAPI::addYoga)
            delete("/api/yogas/:yoga-id",HealthTrackerAPI::deleteYogaByYogaId)
            patch("/api/yogas/:yoga-id",HealthTrackerAPI::updateYoga)
            delete("/api/users/:user-id/yogas", HealthTrackerAPI::deleteYogasByUserId)

            //Water
            get("/api/waters/:water-id", HealthTrackerAPI::getWaterByWaterId)
            get("/api/users/:user-id/waters",HealthTrackerAPI::getWatersByUserId)
            get("/api/waters", HealthTrackerAPI::getAllWaters)
            post("/api/waters",HealthTrackerAPI::addWater)
            delete("/api/waters/:water-id",HealthTrackerAPI::deleteWaterByWaterId)
            patch("/api/waters/:water-id",HealthTrackerAPI::updateWater)
            delete("/api/users/:user-id/waters", HealthTrackerAPI::deleteWatersByUserId)

            //Goals
            get("/api/goals/:goal-id", HealthTrackerAPI::getGoalByGoalId)
            get("/api/users/:user-id/goals",HealthTrackerAPI::getGoalsByUserId)
            get("/api/goals", HealthTrackerAPI::getAllGoals)
            post("/api/goals",HealthTrackerAPI::addGoal)
            delete("/api/goals/:goal-id",HealthTrackerAPI::deleteGoalByGoalId)
            patch("/api/goals/:goal-id",HealthTrackerAPI::updateGoal)
            delete("/api/users/:user-id/goals", HealthTrackerAPI::deleteGoalsByUserId)

            //Health Conditions
            get("/api/healths/:health-condition-id", HealthTrackerAPI::getHealthConditionByHealthConditionId)
            get("/api/healths/:user-id/healths",HealthTrackerAPI::getHealthCondtionByUserId)
            get("/api/healths", HealthTrackerAPI::getAllHealthCondtions)
            post("/api/healths",HealthTrackerAPI::addHealthCondition)
            delete("/api/healths/:health-condition-id",HealthTrackerAPI::deleteHealthConditionByHealthConditionId)
            patch("/api/healths/:health-condition-id",HealthTrackerAPI::updateHealthCondition)
            delete("/api/healths/:user-id/healths", HealthTrackerAPI::deleteHealthConditionByUserId)

            // The @routeComponent that we added in layout.html earlier will be replaced
            // by the String inside of VueComponent. This means a call to / will load
            // the layout and display our <home-page> component.

            //**** USERS ******
            get("/", VueComponent("<home-page></home-page>"))
            get("/users", VueComponent("<user-overview></user-overview>"))
            get("/users/:user-id", VueComponent("<user-profile></user-profile>"))

            //**** ACTIVITIES ******
            get("/activities", VueComponent("<activity-overview></activity-overview>"))
            get("/activities/:activity-id", VueComponent("<activity-each-view></activity-each-view>"))
            get("/users/:user-id/activities", VueComponent("<user-activity-overview></user-activity-overview>"))

            //**** MEALS ******
            get("/meals", VueComponent("<meal-overview></meal-overview>"))
            get("/meals/:meal-id", VueComponent("<meal-each-view></meal-each-view>"))
            get("/users/:user-id/meals", VueComponent("<user-meal-overview></user-meal-overview>"))

            //**** SLEEPS ******
            get("/sleeps", VueComponent("<sleep-overview></sleep-overview>"))
            get("/sleeps/:sleep-id", VueComponent("<sleep-each-view></sleep-each-view>"))
            get("/users/:user-id/sleeps", VueComponent("<user-sleep-overview></user-sleep-overview>"))




        }
    }
}
