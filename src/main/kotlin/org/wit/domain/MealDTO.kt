package org.wit.domain

import org.joda.time.DateTime

data class MealDTO (var mealId: Int,
                    var mealName:String,
                    var mealType: String,
                    var dateTime: DateTime,
                    var calories: Double,
                    var quantity: Double,
                    var userId: Int)